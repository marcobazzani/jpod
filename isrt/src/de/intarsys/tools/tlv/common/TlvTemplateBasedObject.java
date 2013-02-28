package de.intarsys.tools.tlv.common;

/**
 * An object that is based on a {@link TlvTemplate}, a sequence of
 * {@link TlvElement} instances.
 * 
 */
public abstract class TlvTemplateBasedObject {

	final private TlvTemplate template;

	public TlvTemplateBasedObject(TlvTemplate template) {
		this.template = template;
	}

	/**
	 * The {@link TlvElement} identified by identifier or <code>null</code>.
	 * 
	 * @param identifier
	 * @return The {@link TlvElement} identified by identifier or
	 *         <code>null</code>.
	 */
	public TlvElement getElement(int identifier) {
		return template.getElement(identifier);
	}

	public TlvTemplate getTemplate() {
		return template;
	}

}
