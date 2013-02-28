/*
 * Copyright (c) 2008, intarsys consulting GmbH
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
package de.intarsys.cwt.awt.image;

import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;

/**
 * Tool methods for the AWT CWT image implementation
 */
public class CwtAwtImageTools {

	private static ComponentColorModel GrayColorModel;

	private static ComponentColorModel GrayTransparentColorModel;

	public static ComponentColorModel RgbColorModel;

	public static ComponentColorModel RgbTransparentColorModel;

	public static ComponentColorModel getGrayColorModel() {
		if (CwtAwtImageTools.GrayColorModel == null) {
			CwtAwtImageTools.GrayColorModel = new ComponentColorModel(
					ColorSpace.getInstance(ColorSpace.CS_GRAY), false, false,
					Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
		}
		return CwtAwtImageTools.GrayColorModel;
	}

	public static ComponentColorModel getGrayTransparentColorModel() {
		if (CwtAwtImageTools.GrayTransparentColorModel == null) {
			CwtAwtImageTools.GrayTransparentColorModel = new ComponentColorModel(
					ColorSpace.getInstance(ColorSpace.CS_GRAY), true, false,
					Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);
		}
		return CwtAwtImageTools.GrayTransparentColorModel;
	}

	public static ComponentColorModel getRgbColorModel() {
		if (CwtAwtImageTools.RgbColorModel == null) {
			CwtAwtImageTools.RgbColorModel = new ComponentColorModel(ColorSpace
					.getInstance(ColorSpace.CS_sRGB), false, false,
					Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
		}
		return CwtAwtImageTools.RgbColorModel;
	}

	public static ComponentColorModel getRgbTransparentColorModel() {
		if (CwtAwtImageTools.RgbTransparentColorModel == null) {
			CwtAwtImageTools.RgbTransparentColorModel = new ComponentColorModel(
					ColorSpace.getInstance(ColorSpace.CS_sRGB), true, false,
					Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);
		}
		return CwtAwtImageTools.RgbTransparentColorModel;
	}

}
