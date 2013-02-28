package de.intarsys.tools.tlv.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A template is a collection of {@link TlvElement} instances. A
 * {@link TlvTemplate} is for example contained in a composite
 * {@link TlvElement}.
 * 
 */
public class TlvTemplate implements Iterable<TlvElement> {

	private final List<TlvElement> elements;

	public TlvTemplate() {
		super();
		elements = new ArrayList<TlvElement>();
	}

	public TlvTemplate(TlvInputStream is) throws TlvFormatException {
		super();
		elements = new ArrayList<TlvElement>();
		try {
			TlvElement element = is.readElement();
			while (element != null) {
				elements.add(element);
				element = is.readElement();
			}
		} catch (IOException e) {
			throw new TlvFormatException(e.getMessage(), e);
		}
	}

	public void addElement(TlvElement element) {
		elements.add(element);
	}

	/**
	 * The first {@link TlvElement} identified by identifier or
	 * <code>null</code>.
	 * 
	 * @param identifier
	 * @return The first {@link TlvElement} identified by identifier or
	 *         <code>null</code>.
	 */
	public TlvElement getElement(int identifier) {
		for (TlvElement element : elements) {
			if (element.getIdentifier() == identifier) {
				return element;
			}
		}
		return null;
	}

	public byte[] getEncoded() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		for (TlvElement element : elements) {
			try {
				os.write(element.getEncoded());
			} catch (IOException e) {
				// won't happen
				e.printStackTrace();
			}
		}
		return os.toByteArray();
	}

	@Override
	public Iterator<TlvElement> iterator() {
		return elements.iterator();
	}

}
