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
package de.intarsys.cwt.swt.image;

import java.awt.Point;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

import sun.awt.image.ByteInterleavedRaster;
import sun.awt.image.BytePackedRaster;
import de.intarsys.cwt.awt.image.CwtAwtImageTools;
import de.intarsys.tools.system.SystemTools;
import de.intarsys.tools.valueholder.IValueHolder;
import de.intarsys.tools.valueholder.ObjectValueHolder;

public class ImageConverterAwt2Swt {

	static class OS_Other implements OS_Switch {
		public PaletteData createRgbPaletteData() {
			return new PaletteData(0xFF0000, 0xFF00, 0xFF);
		}

		public void initializeRgbBandOffsets(int[] bandOffsets) {
			for (int index = 0; index < 3; index++) {
				bandOffsets[index] = index;
			}
		}
	}

	static interface OS_Switch {
		PaletteData createRgbPaletteData();

		void initializeRgbBandOffsets(int[] bandOffsets);
	}

	static class OS_Windows implements OS_Switch {
		public PaletteData createRgbPaletteData() {
			return new PaletteData(0xFF, 0xFF00, 0xFF0000);
		}

		public void initializeRgbBandOffsets(int[] bandOffsets) {
			for (int index = 0; index < 3; index++) {
				bandOffsets[index] = 2 - index;
			}
		}
	}

	private static Map<ColorSpace, Map<ColorSpace, ColorConvertOp>> ConvertOps;

	private static OS_Switch OsSwitch;

	static {
		ConvertOps = new HashMap<ColorSpace, Map<ColorSpace, ColorConvertOp>>();
		if (SystemTools.isWindows()) {
			OsSwitch = new OS_Windows();
		} else {
			OsSwitch = new OS_Other();
		}
	}

	public static BufferedImage createSwtCompatibleAwtImage(int width,
			int height, boolean transparent) {
		ComponentColorModel colorModel;
		int bands;
		int[] bandOffsets;
		SampleModel sampleModel;
		DataBuffer dataBuffer;
		WritableRaster raster;

		if (transparent) {
			colorModel = CwtAwtImageTools.getRgbTransparentColorModel();
		} else {
			colorModel = CwtAwtImageTools.getRgbColorModel();
		}
		bands = colorModel.getNumComponents();
		bandOffsets = new int[bands];
		OsSwitch.initializeRgbBandOffsets(bandOffsets);
		if (bandOffsets.length > 3) {
			bandOffsets[3] = 3;
		}
		sampleModel = new PixelInterleavedSampleModel(DataBuffer.TYPE_BYTE,
				width, height, bands, width * bands, bandOffsets);
		dataBuffer = new DataBufferByte(width * height * bands);
		raster = new SwtCompatibleRaster(sampleModel, dataBuffer, new Point(0,
				0));
		return new BufferedImage(colorModel, raster, false, null);
	}

	protected static Map<ColorSpace, Map<ColorSpace, ColorConvertOp>> getConvertOps() {
		return ConvertOps;
	}

	private BufferedImage bufferedImage;
	private ImageData imageData;

	public ImageConverterAwt2Swt(BufferedImage paramBufferedImage) {
		bufferedImage = paramBufferedImage;
	}

	protected void computeValuesFromAwtImage(int depth, IValueHolder palette,
			IValueHolder scanlinePad, IValueHolder transparentPixel) {
		SampleModel sampleModel;
		ColorModel colorModel;

		sampleModel = getBufferedImage().getSampleModel();
		if (sampleModel.getDataType() != DataBuffer.TYPE_BYTE) {
			throw new IllegalStateException();
		}
		colorModel = getBufferedImage().getColorModel();
		if (!colorModel.getColorSpace().isCS_sRGB()) {
			throw new IllegalStateException();
		}

		if (colorModel instanceof ComponentColorModel) {
			if (!(sampleModel instanceof ComponentSampleModel)) {
				throw new IllegalStateException();
			}
			computeValuesPaletteDirect((ComponentSampleModel) sampleModel,
					(ComponentColorModel) colorModel, palette, scanlinePad,
					transparentPixel);
			return;
		} else if (colorModel instanceof IndexColorModel) {
			computeValuesPaletteIndexed((IndexColorModel) colorModel, depth,
					palette, scanlinePad, transparentPixel);
			return;
		}
		throw new IllegalStateException();
	}

	protected void computeValuesPaletteDirect(ComponentSampleModel sampleModel,
			ComponentColorModel colorModel, IValueHolder palette,
			IValueHolder scanlinePad, IValueHolder transparentPixel) {
		int[] componentSize;
		int[] bandOffsets;
		PaletteData paletteData;

		componentSize = colorModel.getComponentSize();
		for (int index = 0; index < componentSize.length; index++) {
			if (componentSize[index] != 8) {
				throw new IllegalStateException();
			}
		}
		bandOffsets = (sampleModel).getBandOffsets();
		if (bandOffsets.length == 4 && bandOffsets[3] != 3) {
			throw new IllegalStateException();
		}
		paletteData = new PaletteData(0xFF0000 >> (bandOffsets[0] << 3),
				0xFF0000 >> (bandOffsets[1] << 3),
				0xFF0000 >> (bandOffsets[2] << 3));
		palette.set(paletteData);
		scanlinePad.set(3);
	}

	protected void computeValuesPaletteIndexed(IndexColorModel colorModel,
			int depth, IValueHolder paletteHolder,
			IValueHolder scanlinePadHolder, IValueHolder transparentPixelHolder) {
		int[] awtRgb;
		RGB[] swtRgb;

		awtRgb = new int[colorModel.getMapSize()];
		colorModel.getRGBs(awtRgb);

		swtRgb = new RGB[(int) Math.pow(2, depth)];
		for (int index = 0; index < awtRgb.length; index++) {
			RGB rgb;

			rgb = new RGB((awtRgb[index] >> 16) & 0xFF,
					(awtRgb[index] >> 8) & 0xFF, awtRgb[index] & 0xFF);
			swtRgb[index] = rgb;
		}
		for (int index = awtRgb.length; index < swtRgb.length; index++) {
			swtRgb[index] = new RGB(0, 0, 0);
		}
		paletteHolder.set(new PaletteData(swtRgb));
		scanlinePadHolder.set(1);
		transparentPixelHolder.set(colorModel.getTransparentPixel());
	}

	protected void convert(BufferedImage srcImage, BufferedImage destImage) {
		ColorSpace srcColorSpace;
		ColorSpace destColorSpace;
		Map<ColorSpace, ColorConvertOp> srcMatchingConvertOps;
		ColorConvertOp matchingConvertOp;

		srcColorSpace = srcImage.getColorModel().getColorSpace();
		destColorSpace = destImage.getColorModel().getColorSpace();
		srcMatchingConvertOps = getConvertOps().get(srcColorSpace);
		if (srcMatchingConvertOps == null) {
			srcMatchingConvertOps = new HashMap<ColorSpace, ColorConvertOp>();
			getConvertOps().put(srcColorSpace, srcMatchingConvertOps);
			matchingConvertOp = null;
		} else {
			matchingConvertOp = srcMatchingConvertOps.get(destColorSpace);
		}
		if (matchingConvertOp == null) {
			matchingConvertOp = new ColorConvertOp(null);
			srcMatchingConvertOps.put(destColorSpace, matchingConvertOp);
		}
		matchingConvertOp.filter(srcImage, destImage);
	}

	protected ImageData createImageData() {
		ColorModel colorModel;
		int depth;
		IValueHolder palette;
		IValueHolder scanlinePad;
		IValueHolder transparentPixel;
		WritableRaster raster;
		byte[] bytes;
		byte[] rgbBytes;
		byte[] alphaBytes;

		colorModel = getBufferedImage().getColorModel();
		depth = colorModel.getPixelSize();
		if (colorModel.getTransparency() == Transparency.TRANSLUCENT) {
			depth = depth / colorModel.getNumComponents()
					* colorModel.getNumColorComponents();
		}
		palette = new ObjectValueHolder(null);
		scanlinePad = new ObjectValueHolder(0);
		transparentPixel = new ObjectValueHolder(-1);
		try {
			computeValuesFromAwtImage(depth, palette, scanlinePad,
					transparentPixel);
			raster = getBufferedImage().getRaster();
		} catch (IllegalStateException ex) {
			BufferedImage newBufferedImage;

			newBufferedImage = createSwtCompatibleAwtImage(getBufferedImage()
					.getWidth(), getBufferedImage().getHeight(), colorModel
					.hasAlpha());
			/*
			 * this yields inexact colors but is much faster than drawing on the
			 * new image
			 */
			// TODO is there a way to improve this?
			convert(getBufferedImage(), newBufferedImage);
			depth = 24;
			palette.set(OsSwitch.createRgbPaletteData());
			scanlinePad.set(3);
			raster = newBufferedImage.getRaster();
		}

		if (raster instanceof SwtCompatibleRaster) {
			bytes = ((SwtCompatibleRaster) raster).getDataStorage();
		} else if (raster instanceof ByteInterleavedRaster) {
			// TODO this is a sun... class
			bytes = ((ByteInterleavedRaster) raster).getDataStorage();
		} else if (raster instanceof BytePackedRaster) {
			// TODO this is a sun... class
			bytes = ((BytePackedRaster) raster).getDataStorage();
		} else {
			bytes = (byte[]) raster.getDataElements(raster.getMinX(), raster
					.getMinY(), raster.getWidth(), raster.getHeight(), null);
		}
		if (((PaletteData) palette.get()).isDirect && colorModel.hasAlpha()) {
			alphaBytes = new byte[bytes.length / 4];
			rgbBytes = new byte[bytes.length / 4 * 3];
			for (int index = 0; index < alphaBytes.length; index++) {
				rgbBytes[index * 3] = bytes[index * 4];
				rgbBytes[(index * 3) + 1] = bytes[(index * 4) + 1];
				rgbBytes[(index * 3) + 2] = bytes[(index * 4) + 2];
				alphaBytes[index] = bytes[(index * 4) + 3];
			}
		} else {
			alphaBytes = null;
			rgbBytes = bytes;
		}
		imageData = new ImageData(getBufferedImage().getWidth(),
				getBufferedImage().getHeight(), depth, (PaletteData) palette
						.get(), (Integer) scanlinePad.get(), rgbBytes);
		imageData.transparentPixel = (Integer) transparentPixel.get();
		if (alphaBytes != null) {
			imageData.alphaData = alphaBytes;
		}
		return imageData;
	}

	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	public ImageData getImageData() {
		if (imageData == null) {
			imageData = createImageData();
		}
		return imageData;
	}
}
