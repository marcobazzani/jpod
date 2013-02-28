package de.intarsys.tools.tlv.common;

/**
 * Composite TLV based object. This class eases the mapping of
 * {@link TlvElement} based objects that are composite (the {@link TlvElement}
 * is not primitive, but contains a {@link TlvTemplate} itself.
 * <p>
 * 
 */
public abstract class TlvElementBasedComposite extends TlvElementBasedObject {

	private final TlvTemplate template;

	public TlvElementBasedComposite(TlvElement element) {
		super(element);
		this.template = element.getTemplate();
	}

	/**
	 * The {@link TlvElement} identified by identifier or <code>null</code>.
	 * 
	 * @param identifier
	 * @return
	 */
	protected TlvElement getElement(int identifier) {
		return template.getElement(identifier);
	}

	/**
	 * The {@link TlvTemplate} containing the properties.
	 * 
	 * @return The {@link TlvTemplate} containing the properties.
	 */
	public TlvTemplate getTemplate() {
		return template;
	}

}
