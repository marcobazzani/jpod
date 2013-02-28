/*
 * Copyright (c) 2012, intarsys consulting GmbH
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
package de.intarsys.tools.attachment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import de.intarsys.tools.attribute.Attribute;
import de.intarsys.tools.attribute.IAttributeSupport;

/**
 * A tool class for implementing a generic attachment feature.
 * <p>
 * Attachments can be made for an object or a thread context.
 */
public class AttachmentTools {

	static private final Attribute ATTR_ATTACHMENTS = new Attribute(
			"attachments"); //$NON-NLS-1$

	static private final Attachment[] EMPTY = new Attachment[0];

	private static final Map<Object, List<Attachment>> objectAttachments = new WeakHashMap<Object, List<Attachment>>();

	private static final ThreadLocal<List<Attachment>> threadAttachments = new ThreadLocal<List<Attachment>>();

	/**
	 * Add a {@link Attachment} to the thread context.
	 * 
	 * @param attachment
	 *            The new attachment
	 */
	static public void addAttachment(Attachment attachment) {
		if (attachment == null) {
			return;
		}
		List<Attachment> temp = basicGetAttachments();
		temp.add(attachment);
	}

	/**
	 * Add a {@link Attachment} to the target object.
	 * 
	 * @param target
	 *            The target object
	 * @param attachment
	 *            The new attachment
	 */
	static public void addAttachment(Object target, Attachment attachment) {
		if (attachment == null) {
			return;
		}
		List<Attachment> temp = basicGetAttachments(target);
		temp.add(attachment);
	}

	/**
	 * Add a {@link Attachment} to the target object.
	 * 
	 * @param target
	 *            The target object
	 * @param key
	 *            The key
	 * @param value
	 *            The attached object
	 */
	static public void addAttachment(Object target, String key, Object value) {
		addAttachment(target, new Attachment(key, value));
	}

	/**
	 * Add a {@link Attachment} to the thread context.
	 * 
	 * 
	 * @param key
	 *            The key
	 * @param value
	 *            The attached object
	 */
	static public void addAttachment(String key, Object value) {
		addAttachment(new Attachment(key, value));
	}

	/**
	 * Add an array of {@link Attachment} instances to the thread context.
	 * 
	 * @param pAttachments
	 */
	static public void addAttachments(Attachment[] pAttachments) {
		if (pAttachments == null) {
			return;
		}
		List<Attachment> temp = basicGetAttachments();
		temp.addAll(Arrays.asList(pAttachments));
	}

	/**
	 * Add a list of {@link Attachment} instances to the thread context.
	 * 
	 * @param pAttachments
	 */
	static public void addAttachments(List<Attachment> pAttachments) {
		if (pAttachments == null) {
			return;
		}
		for (Attachment attachment : pAttachments) {
			// type check - fail early
		}
		List<Attachment> temp = basicGetAttachments();
		temp.addAll(pAttachments);
	}

	/**
	 * Add an array of {@link Attachment} instances to the target object.
	 * 
	 * @param target
	 *            The target object
	 * @param pAttachments
	 */
	static public void addAttachments(Object target, Attachment[] pAttachments) {
		if (pAttachments == null) {
			return;
		}
		List<Attachment> temp = basicGetAttachments(target);
		temp.addAll(Arrays.asList(pAttachments));
	}

	/**
	 * Add a list of {@link Attachment} instances to the target object.
	 * 
	 * @param target
	 *            The target object
	 * @param pAttachments
	 */
	static public void addAttachments(Object target,
			List<Attachment> pAttachments) {
		if (pAttachments == null) {
			return;
		}
		for (Attachment attachment : pAttachments) {
			// type check - fail early
		}
		List<Attachment> temp = basicGetAttachments(target);
		temp.addAll(pAttachments);
	}

	protected static List<Attachment> basicGetAttachments() {
		List<Attachment> tempList = threadAttachments.get();
		if (tempList == null) {
			tempList = new ArrayList<Attachment>();
			threadAttachments.set(tempList);
		}
		return tempList;
	}

	protected static List<Attachment> basicGetAttachments(Object object) {
		if (object instanceof IAttachmentSupport) {
			return ((IAttachmentSupport) object).getAttachments();
		} else if (object instanceof IAttributeSupport) {
			List<Attachment> tempList = (List<Attachment>) ((IAttributeSupport) object)
					.getAttribute(ATTR_ATTACHMENTS);
			if (tempList == null) {
				tempList = new ArrayList<Attachment>();
				((IAttributeSupport) object).setAttribute(ATTR_ATTACHMENTS,
						tempList);
			}
			return tempList;
		} else {
			List<Attachment> tempList = objectAttachments.get(object);
			if (tempList == null) {
				tempList = new ArrayList<Attachment>();
				objectAttachments.put(object, tempList);
			}
			return tempList;
		}
	}

	protected static List<Attachment> basicLookupAttachments() {
		List<Attachment> tempList = threadAttachments.get();
		return tempList;
	}

	protected static List<Attachment> basicLookupAttachments(Object object) {
		if (object instanceof IAttachmentSupport) {
			return ((IAttachmentSupport) object).getAttachments();
		} else if (object instanceof IAttributeSupport) {
			List<Attachment> tempList = (List<Attachment>) ((IAttributeSupport) object)
					.getAttribute(ATTR_ATTACHMENTS);
			return tempList;
		} else {
			List<Attachment> tempList = objectAttachments.get(object);
			return tempList;
		}
	}

	/**
	 * Clear all attachments for the thread context.
	 * 
	 */
	static public void clearAttachments() {
		List<Attachment> tempList = basicLookupAttachments();
		if (tempList == null) {
			return;
		}
		tempList.clear();
	}

	/**
	 * Clear all attachments for the target object.
	 * 
	 * @param target
	 *            The target object
	 */
	static public void clearAttachments(Object target) {
		List<Attachment> tempList = basicLookupAttachments(target);
		if (tempList == null) {
			return;
		}
		tempList.clear();
	}

	/**
	 * Get the first {@link Attachment} matching "key" within the target object.
	 * 
	 * @param target
	 *            The target object
	 * @param key
	 *            The key value that is searched
	 * @return The first matching {@link Attachment} or <code>null</code>
	 */
	static public Attachment getAttachment(Object target, String key) {
		List<Attachment> tempList = basicLookupAttachments(target);
		if (tempList == null) {
			return null;
		}
		for (Iterator it = tempList.iterator(); it.hasNext();) {
			Attachment attachment = (Attachment) it.next();
			if (key.equals(attachment.getKey())) {
				return attachment;
			}
		}
		return null;
	}

	/**
	 * Get the first {@link Attachment} matching "key" in the thread context.
	 * 
	 * @param key
	 *            The key value that is searched
	 * @return The first matching {@link Attachment} or <code>null</code>
	 */
	static public Attachment getAttachment(String key) {
		List<Attachment> tempList = basicLookupAttachments();
		if (tempList == null) {
			return null;
		}
		for (Iterator it = tempList.iterator(); it.hasNext();) {
			Attachment attachment = (Attachment) it.next();
			if (key.equals(attachment.getKey())) {
				return attachment;
			}
		}
		return null;
	}

	/**
	 * Get all {@link Attachment} instances for the thread context.
	 * 
	 * @return Get all attachments for the thread context.
	 */
	static public Attachment[] getAttachments() {
		List<Attachment> attachments = basicLookupAttachments();
		if (attachments == null) {
			return EMPTY;
		}
		return attachments.toArray(new Attachment[attachments.size()]);
	}

	/**
	 * Get all {@link Attachment} instances for the target object.
	 * 
	 * @param target
	 *            The target object
	 * @return Get all attachments for object.
	 */
	static public Attachment[] getAttachments(Object target) {
		List<Attachment> attachments = basicLookupAttachments(target);
		if (attachments == null) {
			return EMPTY;
		}
		return attachments.toArray(new Attachment[attachments.size()]);
	}

	/**
	 * Get all attachments matching "key" for the target object.
	 * 
	 * @param target
	 *            The target object
	 * @param key
	 *            The key value that is searched
	 * @return All matching attachments
	 */
	static public Attachment[] getAttachments(Object target, String key) {
		List<Attachment> tempList = basicLookupAttachments(target);
		if (tempList == null) {
			return EMPTY;
		}
		List<Attachment> result = new ArrayList<Attachment>();
		for (Iterator it = tempList.iterator(); it.hasNext();) {
			Attachment attachment = (Attachment) it.next();
			if (key.equals(attachment.getKey())) {
				result.add(attachment);
			}
		}
		return result.toArray(new Attachment[result.size()]);
	}

	/**
	 * Get all attachments matching "key" in the thread context.
	 * 
	 * @param key
	 *            The key value that is searched
	 * @return All matching attachments
	 */
	static public Attachment[] getAttachments(String key) {
		List<Attachment> tempList = basicLookupAttachments();
		if (tempList == null) {
			return EMPTY;
		}
		List<Attachment> result = new ArrayList<Attachment>();
		for (Iterator it = tempList.iterator(); it.hasNext();) {
			Attachment attachment = (Attachment) it.next();
			if (key.equals(attachment.getKey())) {
				result.add(attachment);
			}
		}
		return result.toArray(new Attachment[result.size()]);
	}

	/**
	 * <code>true</code> if the target object has an attachment for "key"
	 * 
	 * @param target
	 *            The target object
	 * @param key
	 *            The key value that is searched
	 * @return <code>true</code> if object has an attachment for "key"
	 */
	static public boolean hasAttachment(Object target, String key) {
		List<Attachment> tempList = basicLookupAttachments(target);
		if (tempList == null) {
			return false;
		}
		for (Iterator it = tempList.iterator(); it.hasNext();) {
			Attachment attachment = (Attachment) it.next();
			if (key.equals(attachment.getKey())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <code>true</code> if the thread context has an attachment for "key"
	 * 
	 * @param key
	 *            The key value that is searched
	 * @return <code>true</code> if object has an attachment for "key"
	 */
	static public boolean hasAttachment(String key) {
		List<Attachment> tempList = basicLookupAttachments();
		if (tempList == null) {
			return false;
		}
		for (Iterator it = tempList.iterator(); it.hasNext();) {
			Attachment attachment = (Attachment) it.next();
			if (key.equals(attachment.getKey())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Remove attachment with attached object in the thread context.
	 * 
	 * @param attached
	 * @return true if removed
	 */
	static public boolean removeAttached(Object attached) {
		List<Attachment> tempList = basicLookupAttachments();
		if (tempList == null) {
			return false;
		}
		for (Iterator it = tempList.iterator(); it.hasNext();) {
			Attachment attachment = (Attachment) it.next();
			if (attachment.getAttached() == attached) {
				it.remove();
				return true;
			}
		}
		return false;
	}

	/**
	 * Remove attachment with attached object in the target object.
	 * 
	 * @param target
	 * @param attached
	 * @return true if removed
	 */
	static public boolean removeAttached(Object target, Object attached) {
		List<Attachment> tempList = basicLookupAttachments(target);
		if (tempList == null) {
			return false;
		}
		for (Iterator it = tempList.iterator(); it.hasNext();) {
			Attachment attachment = (Attachment) it.next();
			if (attachment.getAttached() == attached) {
				it.remove();
				return true;
			}
		}
		return false;
	}

	/**
	 * Remove attachment in the thread context.
	 * 
	 * @param pAttachment
	 * @return true if removed
	 */
	static public boolean removeAttachment(Attachment pAttachment) {
		List<Attachment> tempList = basicLookupAttachments();
		if (tempList == null) {
			return false;
		}
		for (Iterator it = tempList.iterator(); it.hasNext();) {
			Attachment attachment = (Attachment) it.next();
			if (attachment == pAttachment) {
				it.remove();
				return true;
			}
		}
		return false;
	}

	/**
	 * Remove attachment in the target object.
	 * 
	 * @param target
	 * @param pAttachment
	 * @return true if removed
	 */
	static public boolean removeAttachment(Object target, Attachment pAttachment) {
		List<Attachment> tempList = basicLookupAttachments(target);
		if (tempList == null) {
			return false;
		}
		for (Iterator it = tempList.iterator(); it.hasNext();) {
			Attachment attachment = (Attachment) it.next();
			if (attachment == pAttachment) {
				it.remove();
				return true;
			}
		}
		return false;
	}

	/**
	 * Remove all attachments for key in the target object.
	 * 
	 * @param target
	 *            The target object
	 * @param key
	 *            The key value for the attachments to be removed
	 */
	static public void removeAttachments(Object target, String key) {
		List<Attachment> tempList = basicLookupAttachments(target);
		if (tempList == null) {
			return;
		}
		for (Iterator it = tempList.iterator(); it.hasNext();) {
			Attachment attachment = (Attachment) it.next();
			if (key.equals(attachment.getKey())) {
				it.remove();
			}
		}
	}

	/**
	 * Remove all attachments for key in the thread context.
	 * 
	 * @param key
	 *            The key value for the attachments to be removed
	 */
	static public void removeAttachments(String key) {
		List<Attachment> tempList = basicLookupAttachments();
		if (tempList == null) {
			return;
		}
		for (Iterator it = tempList.iterator(); it.hasNext();) {
			Attachment attachment = (Attachment) it.next();
			if (key.equals(attachment.getKey())) {
				it.remove();
			}
		}
	}

}
