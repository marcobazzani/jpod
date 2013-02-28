package de.intarsys.tools.tlv.common;

/**
 * Tool methods for handling the TLV implementation.
 * 
 */
public class TlvTools {

	public static int asInt(byte[] src, int offset, int length) {
		length = length < 4 ? length : 4;
		length = length <= (src.length - offset) ? length : src.length - offset;
		int result = src[offset];
		for (int i = 1; i < length; i++) {
			result <<= 8;
			result += src[offset + i] & 0xFF;
		}
		return result;
	}

	public static int asInt(TlvElement element) {
		return asInt(element.buffer, element.offset, element.length);
	}

	public static int asUnsignedInt(byte[] src, int offset, int length) {
		length = length < 4 ? length : 4;
		length = length <= (src.length - offset) ? length : src.length - offset;
		int result = src[offset] & 0xFF;
		for (int i = 1; i < length; i++) {
			result <<= 8;
			result += src[offset + i] & 0xFF;
		}
		return result;
	}

	public static int asUnsignedInt(TlvElement element) {
		return asUnsignedInt(element.buffer, element.offset, element.length);
	}

	public static void checkSize(byte[] src, int minSize) {
		if (src == null) {
			throw new IllegalArgumentException("byte[] src cannot be null"); //$NON-NLS-1$
		}
		if (src.length < minSize) {
			throw new IllegalArgumentException("byte[] src is to small"); //$NON-NLS-1$
		}
	}

	private TlvTools() {
		//
	}

}
