/*
 * CABAReT solutions
 * all rights reserved
 *
 */
package de.intarsys.tools.dom;

import org.w3c.dom.Element;

import de.intarsys.tools.exception.TunnelingException;
import de.intarsys.tools.proxy.IProxy;
import de.intarsys.tools.reflect.ObjectCreationException;

/**
 * A generic implementation to ease implementation of "deferred" objects
 * declared via an {@link Element}.
 * <p>
 * This object encapsulates the {@link Element}, preparing for realization of
 * the intended object on demand. Two common scenarios are supported. In the
 * first, the provider of the extension point itself creates the
 * {@link ElementObjectProxy} directly to avoid the cost of reflective class
 * access. In the second, a concrete factory object may be derived from
 * {@link ElementObjectProxy} to inherit its lazyness with regard to hosting an
 * object to be realized. The concrete proxy class name may be declared in an
 * another element attribute than "class".
 * 
 */
public class ElementObjectProxy implements IElementConfigurable, IProxy {

	/**
	 * The link to the definition element in the extension
	 */
	private Element element;

	private Object realized;

	final private Class proxyClass;

	final private String proxyClassAttribute;

	final private ClassLoader classLoader;

	public ElementObjectProxy() {
		// reflective use
		// extension and element are set via "configure"
		proxyClass = Object.class;
		proxyClassAttribute = "class";
		classLoader = getClass().getClassLoader();
	}

	/**
	 * 
	 */
	public ElementObjectProxy(Class pProxyClass, Element pElement,
			ClassLoader pClassLoader) {
		element = pElement;
		proxyClass = pProxyClass;
		classLoader = pClassLoader;
		proxyClassAttribute = "class";
	}

	public ElementObjectProxy(Class pProxyClass, Element pElement,
			String classAttribute, ClassLoader pClassLoader) {
		element = pElement;
		proxyClass = pProxyClass;
		classLoader = pClassLoader;
		proxyClassAttribute = classAttribute;
	}

	protected Object basicGetRealized() {
		return realized;
	}

	public void configure(Element pElement) {
		this.element = pElement;
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public Element getElement() {
		return element;
	}

	public Class getProxyClass() {
		return proxyClass;
	}

	public String getProxyClassAttribute() {
		return proxyClassAttribute;
	}

	synchronized public Object getRealized() {
		if (realized == null) {
			try {
				realized = realize();
			} catch (ObjectCreationException e) {
				throw new TunnelingException(e);
			}
		}
		return realized;
	}

	protected Object realize() throws ObjectCreationException {
		Object object = ElementTools.createObject(getElement(),
				getProxyClassAttribute(), getProxyClass(), getClassLoader());
		return object;
	}

}
