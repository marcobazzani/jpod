/*
 * Copyright (c) 2007, intarsys consulting GmbH
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * - Neither the name of intarsys nor the names of its contributors may be used
 *   to endorse or promote products derived from this software without specific
 *   prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package de.intarsys.tools.dom;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.intarsys.tools.attribute.AttributeMap;
import de.intarsys.tools.attribute.IAttributeSupport;
import de.intarsys.tools.bean.BeanContainer;
import de.intarsys.tools.component.IInitializeable;
import de.intarsys.tools.enumeration.EnumItem;
import de.intarsys.tools.enumeration.EnumMeta;
import de.intarsys.tools.factory.IFactory;
import de.intarsys.tools.factory.Outlet;
import de.intarsys.tools.functor.Args;
import de.intarsys.tools.functor.FunctorCall;
import de.intarsys.tools.functor.FunctorFieldHandler;
import de.intarsys.tools.functor.FunctorInvocationException;
import de.intarsys.tools.functor.IArgs;
import de.intarsys.tools.functor.IArgsConfigurable;
import de.intarsys.tools.functor.IFunctor;
import de.intarsys.tools.functor.IFunctorCall;
import de.intarsys.tools.reflect.FieldException;
import de.intarsys.tools.reflect.IClassLoaderAccess;
import de.intarsys.tools.reflect.IFieldHandler;
import de.intarsys.tools.reflect.ObjectCreationException;
import de.intarsys.tools.reflect.ObjectTools;
import de.intarsys.tools.string.StringTools;

/**
 * 
 */
public class ElementTools {

	static class ElementProxyInvocationHandler implements InvocationHandler,
			IAttributeSupport {

		/*
		 * this a a singleton (one handler per proxy) - so we can afford a
		 * single attributes map.
		 */
		private AttributeMap attributes;

		private Map functors = new HashMap();

		public ElementProxyInvocationHandler(Element element, Class[] clazzes,
				ClassLoader loader) throws ObjectCreationException {
			createFunctors(element, clazzes, loader);
		}

		protected void createFunctors(Element element, Class[] clazzes,
				ClassLoader loader) throws ObjectCreationException {
			Element implementation = getFirstElement(element, "implementation"); //$NON-NLS-1$
			NodeList nodes = implementation.getElementsByTagName("method");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element child = (Element) nodes.item(i);
				String name = child.getAttribute("name"); //$NON-NLS-1$
				IFunctor functor = ElementTools.createFunctor(child, this,
						loader);
				functors.put(name, functor);
			}
			IFunctor tempFunctor;
			tempFunctor = new IFunctor() {
				public Object perform(IFunctorCall call)
						throws FunctorInvocationException {
					return getAttribute(call.getArgs().get(0));
				}
			};
			functors.put("getAttribute", tempFunctor); //$NON-NLS-1$
			tempFunctor = new IFunctor() {
				public Object perform(IFunctorCall call)
						throws FunctorInvocationException {
					return setAttribute(call.getArgs().get(0), call.getArgs()
							.get(1));
				}
			};
			functors.put("setAttribute", tempFunctor); //$NON-NLS-1$
			tempFunctor = new IFunctor() {
				public Object perform(IFunctorCall call)
						throws FunctorInvocationException {
					return removeAttribute(call.getArgs().get(0));
				}
			};
			functors.put("removeAttribute", tempFunctor); //$NON-NLS-1$
		}

		public Object getAttribute(Object key) {
			if (attributes == null) {
				return null;
			}
			return attributes.get(key);
		}

		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			String name = method.getName();
			IFunctor functor = (IFunctor) functors.get(name);
			if (functor == null) {
				return null;
			}
			Args functorArgs = new Args(args);
			FunctorCall call = new FunctorCall(proxy, functorArgs);
			return functor.perform(call);
		}

		public Object removeAttribute(Object key) {
			if (attributes == null) {
				return null;
			}
			return attributes.remove(key);
		}

		public Object setAttribute(Object key, Object o) {
			if (attributes == null) {
				attributes = new AttributeMap();
			}
			return attributes.put(key, o);
		}
	}

	private static final Logger Log = PACKAGE.Log;

	private static Pattern splitPattern = Pattern.compile("\\."); //$NON-NLS-1$

	protected static InvocationHandler basicCreateInvocationHandler(
			Element element, Class[] clazzes, ClassLoader loader)
			throws ObjectCreationException {
		return new ElementProxyInvocationHandler(element, clazzes, loader);
	}

	protected static Object basicCreateProxy(Element element, Class[] clazzes,
			ClassLoader loader) throws ObjectCreationException {
		// always implement IAttributeSupport
		Class[] extendedClasses = new Class[clazzes.length + 1];
		System.arraycopy(clazzes, 0, extendedClasses, 0, clazzes.length);
		extendedClasses[extendedClasses.length - 1] = IAttributeSupport.class;
		InvocationHandler handler = basicCreateInvocationHandler(element,
				extendedClasses, loader);
		return Proxy.newProxyInstance(loader, extendedClasses, handler);
	}

	protected static Class basicCreateProxyClass(Element element,
			Class[] clazzes, ClassLoader loader) throws ObjectCreationException {
		Class[] extendedClasses = new Class[clazzes.length + 1];
		System.arraycopy(clazzes, 0, extendedClasses, 0, clazzes.length);
		extendedClasses[extendedClasses.length - 1] = IAttributeSupport.class;
		return Proxy.getProxyClass(loader, extendedClasses);
	}

	public static <T> Class<T> createClass(Element element,
			String classAttribute, Class<T> expectedClass,
			ClassLoader classLoader) throws ObjectCreationException {
		String className = getString(element, classAttribute, null);
		if (className == null) {
			throw new ObjectCreationException("class name missing"); //$NON-NLS-1$
		}
		if (classLoader == null) {
			classLoader = Thread.currentThread().getContextClassLoader();
		}
		if (classLoader == null) {
			// as good as any
			classLoader = expectedClass.getClassLoader();
		}
		String[] classNames = className.split("\\;"); //$NON-NLS-1$
		Class[] clazzes = new Class[classNames.length];
		for (int i = 0; i < classNames.length; i++) {
			String tempName = classNames[i].trim();
			try {
				clazzes[i] = Class.forName(tempName, true, classLoader);
			} catch (ClassNotFoundException e) {
				throw new ObjectCreationException("class '" + className //$NON-NLS-1$
						+ "' not found", e); //$NON-NLS-1$
			}
		}
		Class<T> clazz;
		if (clazzes.length > 1) {
			clazz = basicCreateProxyClass(element, clazzes, classLoader);
		} else {
			clazz = clazzes[0];
		}
		return clazz;
	}

	protected static IFieldHandler createFieldHandler(Element element,
			Object owner, ClassLoader classLoader)
			throws ObjectCreationException {
		if (element == null) {
			return null;
		}
		if (getString(element, "class", null) != null //$NON-NLS-1$
				|| getString(element, "factory", null) != null) { //$NON-NLS-1$
			return ElementTools.createObject(element, IFieldHandler.class,
					classLoader);
		} else {
			FunctorFieldHandler tempAccessor = new FunctorFieldHandler();
			Element getElement = getFirstElement(element, "get"); //$NON-NLS-1$
			if (getElement != null) {
				tempAccessor.setGetter(createFunctor(getElement, owner,
						classLoader));
			}
			Element setElement = getFirstElement(element, "set"); //$NON-NLS-1$
			if (setElement != null) {
				tempAccessor.setSetter(createFunctor(setElement, owner,
						classLoader));
			}
			tempAccessor.setName(getString(element, "name", "unknown"));
			return tempAccessor;
		}
	}

	public static IFunctor createFunctor(Element element, Object owner,
			ClassLoader classLoader) throws ObjectCreationException {
		if (element == null) {
			return null;
		}
		IFunctor functor = null;
		if (getString(element, "class", null) != null //$NON-NLS-1$
				|| getString(element, "factory", null) != null) { //$NON-NLS-1$
			functor = ElementTools.createObject(element, IFunctor.class,
					classLoader);
		} else {
			// todo
			//			Element codeExitElement = getFirstElement(element, "perform"); //$NON-NLS-1$
			// if (codeExitElement != null) {
			// functor = CodeExit.createFromElement(codeExitElement);
			// ((CodeExit) functor).setOwner(owner);
			// ((CodeExit) functor).setClassLoader(classLoader);
			// }
		}
		return functor;
	}

	public static <T> T createObject(Element element, Class<T> expectedClass,
			ClassLoader classLoader) throws ObjectCreationException {
		return createObject(element, (String) null, expectedClass, classLoader);
	}

	public static <T> T createObject(Element element, String role,
			Class<T> expectedClass, ClassLoader classLoader)
			throws ObjectCreationException {
		String attributeName;
		String target;
		if (StringTools.isEmpty(role)) {
			attributeName = "ref"; //$NON-NLS-1$
			target = getString(element, attributeName, null);
			if (target != null) {
				return createObjectFromContainer(element, target,
						expectedClass, classLoader);
			}
			attributeName = "class"; //$NON-NLS-1$
			target = getString(element, attributeName, null);
			if (target != null) {
				return createObjectFromClass(element, target, expectedClass,
						classLoader);
			}
			attributeName = "factory"; //$NON-NLS-1$
			target = getString(element, attributeName, null);
			if (target != null) {
				return createObjectFromFactory(element, target, expectedClass,
						classLoader);
			}
			Iterator it = getElementsIterator(element);
			if (it.hasNext()) {
				return createObject(null, (Element) it.next(), expectedClass,
						classLoader);
			}
			throw new ObjectCreationException(
					"can't create object (no 'ref', 'class' or 'factory'"); //$NON-NLS-1$
		}
		attributeName = role;
		target = getString(element, attributeName, null);
		if (target != null) {
			return createObjectFromClass(element, target, expectedClass,
					classLoader);
		}
		attributeName = role + "ref"; //$NON-NLS-1$
		target = getString(element, attributeName, null);
		if (target != null) {
			return createObjectFromContainer(element, target, expectedClass,
					classLoader);
		}
		attributeName = role + "class"; //$NON-NLS-1$
		target = getString(element, attributeName, null);
		if (target != null) {
			return createObjectFromClass(element, target, expectedClass,
					classLoader);
		}
		attributeName = role + "factory"; //$NON-NLS-1$
		target = getString(element, attributeName, null);
		if (target != null) {
			return createObjectFromFactory(element, target, expectedClass,
					classLoader);
		}
		throw new ObjectCreationException("can't create object"); //$NON-NLS-1$
	}

	public static <T> T createObject(Object owner, Element element,
			Class<T> expectedClass, ClassLoader classLoader)
			throws ObjectCreationException {
		String name = element.getTagName();
		if ("object".equals(name)) { //$NON-NLS-1$
			return createObject(element, expectedClass, classLoader);
		} else if ("value".equals(name)) { //$NON-NLS-1$
			Object value;
			value = element.getTextContent();
			String typeName = getString(element, "type", null);
			return (T) ObjectTools.convert(value, typeName, classLoader);
		} else if ("args".equals(name)) { //$NON-NLS-1$
			// todo
			return null;
			// DeclarationBlock block = new DeclarationBlock(owner);
			// new DeclarationIO().deserializeDeclarationElements(block,
			// element,
			// false);
			// Args value = Args.create();
			// try {
			// new ArgumentDeclarator().apply(block, value);
			// } catch (DeclarationException e) {
			// throw new ObjectCreationException(e);
			// }
			// String typeName = getString(element, "type", null);
			// return (T) ObjectTools.convert(value, typeName, classLoader);
		} else if ("null".equals(name)) { //$NON-NLS-1$
			return null;
		} else if ("perform".equals(name)) { //$NON-NLS-1$
			// todo
			return null;
			// CodeExit functor = CodeExit.createFromElement(element);
			// functor.setOwner(owner);
			// functor.setClassLoader(classLoader);
			// try {
			// return (T) functor.perform(FunctorCall.noargs(owner));
			// } catch (FunctorInvocationException e) {
			// throw new ObjectCreationException(e);
			// }
		} else if ("accessor".equals(name)) { //$NON-NLS-1$
			return (T) createFieldHandler(element, owner, classLoader);
		} else {
			throw new ObjectCreationException("unknown value element '" + name //$NON-NLS-1$
					+ "'"); //$NON-NLS-1$
		}
	}

	protected static <T> T createObjectFromClass(Element element,
			String className, Class<T> expectedClass, ClassLoader classLoader)
			throws ObjectCreationException {
		if (className == null) {
			throw new ObjectCreationException("class name missing"); //$NON-NLS-1$
		}
		if (classLoader == null) {
			classLoader = Thread.currentThread().getContextClassLoader();
		}
		if (classLoader == null) {
			// as good as any
			classLoader = expectedClass.getClassLoader();
		}
		String[] classNames = className.split("\\;"); //$NON-NLS-1$
		Class[] clazzes = new Class[classNames.length];
		for (int i = 0; i < classNames.length; i++) {
			String tempName = classNames[i].trim();
			try {
				clazzes[i] = Class.forName(tempName, false, classLoader);
			} catch (ClassNotFoundException e) {
				throw new ObjectCreationException("class '" + className //$NON-NLS-1$
						+ "' not found", e); //$NON-NLS-1$
			}
		}
		Object object;
		if (clazzes.length > 1 || clazzes[0].isInterface()) {
			object = basicCreateProxy(element, clazzes, classLoader);
		} else {
			object = ObjectTools.createObject(clazzes[0], expectedClass);
		}
		try {
			if (object instanceof IClassLoaderAccess) {
				((IClassLoaderAccess) object).setClassLoader(classLoader);
			}
			if (object instanceof IElementConfigurable) {
				((IElementConfigurable) object).configure(element);
			}
			if (object instanceof IArgsConfigurable) {
				((IArgsConfigurable) object).configure(new ElementArgsAdapter(
						element));
			}
			setProperties(object, element, classLoader);
			if (object instanceof IInitializeable) {
				((IInitializeable) object).initializeAfterConstruction();
			}
		} catch (ObjectCreationException e) {
			throw e;
		} catch (Exception e) {
			throw new ObjectCreationException(e);
		}
		return (T) object;
	}

	protected static <T> T createObjectFromContainer(Element element,
			String refName, Class<T> expectedClass, ClassLoader classLoader)
			throws ObjectCreationException {
		T object = BeanContainer.get().lookupBean(refName, expectedClass);
		// reconfigure object...
		try {
			if (object instanceof IElementConfigurable) {
				((IElementConfigurable) object).configure(element);
			}
			setProperties(object, element, classLoader);
		} catch (ObjectCreationException e) {
			throw e;
		} catch (Exception e) {
			throw new ObjectCreationException(e);
		}
		return object;
	}

	protected static <T> T createObjectFromFactory(Element element,
			String factoryName, Class<T> expectedClass, ClassLoader classLoader)
			throws ObjectCreationException {
		if (factoryName == null) {
			throw new ObjectCreationException("factory name missing"); //$NON-NLS-1$
		}
		if (classLoader == null) {
			classLoader = Thread.currentThread().getContextClassLoader();
		}
		if (classLoader == null) {
			// as good as any
			classLoader = expectedClass.getClassLoader();
		}
		IFactory<T> factory = (IFactory<T>) Outlet.get().lookupFactory(
				factoryName);
		if (factory == null) {
			try {
				factory = ObjectTools.createObject(factoryName, IFactory.class,
						classLoader);
				Outlet.get().registerFactory(factory);
				Log.log(Level.INFO, "created default factory '" + factoryName //$NON-NLS-1$
						+ "'"); //$NON-NLS-1$
			} catch (Exception e) {
				throw new ObjectCreationException(
						"factory '" + factoryName + "' missing"); //$NON-NLS-1$
			}
		}
		Object object;
		IArgs args = new ElementArgsAdapter(element);
		object = factory.createInstance(args);
		try {
			if (object instanceof IElementConfigurable) {
				((IElementConfigurable) object).configure(element);
			} else if (factory instanceof IElementConfigurator) {
				((IElementConfigurator) factory).configure(object, element);
			}
			if (object instanceof IClassLoaderAccess) {
				((IClassLoaderAccess) object).setClassLoader(classLoader);
			}
			setProperties(object, element, classLoader);
		} catch (Exception e) {
			throw new ObjectCreationException(e);
		}
		return (T) object;
	}

	public static <T> T createPropertyValue(Object owner, Element element,
			Class<T> expectedClass, ClassLoader classLoader)
			throws ObjectCreationException {
		Element valueElement = null;
		Iterator<Element> itElement = getElementsIterator(element);
		if (itElement.hasNext()) {
			valueElement = itElement.next();
			if (itElement.hasNext()) {
				throw new ObjectCreationException("too many children");
			}
			return createObject(owner, valueElement, expectedClass, classLoader);
		} else {
			String ref = getString(element, "ref", null);
			if (ref != null) {
				return BeanContainer.get().lookupBean(ref, expectedClass);
			} else {
				String value = getString(element, "value", null);
				String typeName = getString(element, "type", null);
				return (T) ObjectTools.convert(value, typeName, classLoader);
			}
		}
	}

	public static boolean getBoolean(Element element, String attributeName,
			boolean defaultValue) {
		String value = null;
		if (element != null) {
			value = getString(element, attributeName, null);
		}
		if (value != null) {
			try {
				return Boolean.parseBoolean(value);
			} catch (Exception e) {
				//
			}
		}
		return defaultValue;
	}

	public static double getDouble(Element element, String attributeName,
			double defaultValue) {
		String value = null;
		if (element != null) {
			value = getString(element, attributeName, null);
		}
		if (value != null) {
			try {
				return Double.parseDouble(value);
			} catch (Exception e) {
				//
			}
		}
		return defaultValue;
	}

	public static Iterator<Element> getElementsIterator(final Element parent) {
		return new Iterator<Element>() {
			private int i = 0;
			private NodeList nodes = parent.getChildNodes();

			@Override
			public boolean hasNext() {
				return i < nodes.getLength();
			}

			@Override
			public Element next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				return (Element) nodes.item(i++);
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public static Iterator<Element> getElementsIterator(final Element parent,
			final String name) {
		return new Iterator<Element>() {
			private int i = 0;
			private NodeList nodes = parent.getElementsByTagName(name);

			@Override
			public boolean hasNext() {
				return i < nodes.getLength();
			}

			@Override
			public Element next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				return (Element) nodes.item(i++);
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public static <T extends EnumItem> T getEnumItem(Element element,
			String attributeName, EnumMeta<T> meta) {
		String id = getString(element, attributeName, null);
		return meta.getItemOrDefault(id);
	}

	public static <T extends EnumItem> T getEnumItem(Element element,
			String attributeName, EnumMeta<T> meta, T defaultValue) {
		String id = getString(element, attributeName, null);
		T result = null;
		if (id != null) {
			result = meta.getItem(id);
		}
		if (result == null) {
			result = defaultValue;
		}
		return result;
	}

	static public Element getFirstElement(Element element, String name) {
		NodeList nodes = element.getElementsByTagName(name);
		if (nodes == null || nodes.getLength() == 0) {
			return null;
		}
		return (Element) nodes.item(0);
	}

	public static float getFloat(Element element, String attributeName,
			float defaultValue) {
		String value = null;
		if (element != null) {
			value = getString(element, attributeName, null);
		}
		if (value != null) {
			try {
				return Float.parseFloat(value);
			} catch (Exception e) {
				//
			}
		}
		return defaultValue;
	}

	public static int getInt(Element element, String attributeName,
			int defaultValue) {
		String value = null;
		if (element != null) {
			Attr attr = element.getAttributeNode(attributeName);
			if (attr != null) {
				value = attr.getValue();
			}
		}
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException e) {
				//
			}
		}
		return defaultValue;
	}

	public static boolean getPathBoolean(Element element, String path,
			boolean defaultValue) {
		Element nextElement = element;
		String[] split = splitPattern.split(path, 0);
		int count = split.length - 1;
		int i = 0;
		while (nextElement != null && i < count) {
			nextElement = getFirstElement(nextElement, split[i]);
			i++;
		}
		return getBoolean(nextElement, split[i], defaultValue);
	}

	public static double getPathDouble(Element element, String path,
			double defaultValue) {
		Element nextElement = element;
		String[] split = splitPattern.split(path, 0);
		int count = split.length - 1;
		int i = 0;
		while (nextElement != null && i < count) {
			nextElement = getFirstElement(nextElement, split[i]);
			i++;
		}
		return getDouble(nextElement, split[i], defaultValue);
	}

	public static float getPathFloat(Element element, String path,
			float defaultValue) {
		Element nextElement = element;
		String[] split = splitPattern.split(path, 0);
		int count = split.length - 1;
		int i = 0;
		while (nextElement != null && i < count) {
			nextElement = getFirstElement(nextElement, split[i]);
			i++;
		}
		return getFloat(nextElement, split[i], defaultValue);
	}

	public static int getPathInt(Element element, String path, int defaultValue) {
		Element nextElement = element;
		String[] split = splitPattern.split(path, 0);
		int count = split.length - 1;
		int i = 0;
		while (nextElement != null && i < count) {
			nextElement = getFirstElement(nextElement, split[i]);
			i++;
		}
		return getInt(nextElement, split[i], defaultValue);
	}

	public static String getPathString(Element element, String path,
			String defaultValue) {
		Element nextElement = element;
		String[] split = splitPattern.split(path, 0);
		int count = split.length - 1;
		int i = 0;
		while (nextElement != null && i < count) {
			nextElement = getFirstElement(nextElement, split[i]);
			i++;
		}
		return getString(nextElement, split[i], defaultValue);
	}

	public static String getString(Element element, String name,
			String defaultValue) {
		if (element != null) {
			Attr attr = element.getAttributeNode(name);
			if (attr == null) {
				return defaultValue;
			}
			return attr.getValue();
		}
		return defaultValue;
	}

	/**
	 * Set properties in object based on the list of property information
	 * contained in element.
	 * <p>
	 * <code>
	 * ...
	 * <object ...>
	 *    <property name="foo" ...
	 *    <property name="bar" ...
	 * </object>
	 * ...
	 * </code>
	 * 
	 * @param object
	 * @param element
	 * @param classLoader
	 * @throws FieldException
	 * @throws ObjectCreationException
	 * @throws FunctorInvocationException
	 */
	public static void setProperties(Object object, Element element,
			ClassLoader classLoader) throws FieldException,
			ObjectCreationException, FunctorInvocationException {
		Iterator<Element> it = getElementsIterator(element, "property"); //$NON-NLS-1$
		while (it.hasNext()) {
			Element propertyElement = it.next();
			setProperty(object, propertyElement, classLoader);
		}
	}

	/**
	 * Set a property in object based on the property information contained in
	 * element.
	 * <p>
	 * Long form <code>
	 * ...
	 * <property name="foo" [operation="set|insert|remove"]>
	 *    <object .../>
	 * </property>
	 * ...
	 * </code>
	 * 
	 * Short form <code>
	 * ...
	 * <property name="foo" value="bar" [type="classname"] />
	 * ...
	 * </code>
	 * 
	 * @param object
	 * @param element
	 * @param classLoader
	 * @throws FieldException
	 * @throws ObjectCreationException
	 * @throws FunctorInvocationException
	 */
	public static void setProperty(Object object, Element element,
			ClassLoader classLoader) throws FieldException,
			ObjectCreationException, FunctorInvocationException {
		String property = getString(element, "name", null); //$NON-NLS-1$
		String operation = getString(element, "operation", "set"); //$NON-NLS-1$
		Object value = createPropertyValue(object, element, Object.class,
				classLoader);
		if ("set".equals(operation)) {
			ObjectTools.set(object, property, value);
		} else if ("insert".equals(operation)) {
			ObjectTools.insert(object, property, value);
		} else if ("remove".equals(operation)) {
			ObjectTools.remove(object, property, value);
		} else {
			throw new ObjectCreationException("unknown property operation '"
					+ operation + "'");
		}
	}
}
