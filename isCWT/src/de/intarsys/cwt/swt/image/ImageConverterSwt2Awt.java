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

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import org.eclipse.swt.graphics.ImageData;

import de.intarsys.cwt.awt.image.CwtAwtImageTools;

public class ImageConverterSwt2Awt {

	private BufferedImage bufferedImage;

	private ImageData imageData;

	public ImageConverterSwt2Awt(ImageData imageData) {
		this.imageData = imageData;
	}

	protected BufferedImage createBufferedImage() {
		ColorModel colorModel;
		WritableRaster raster;
		// will only work if device independent image is standard rgb
		// TODO we also have others
		colorModel = CwtAwtImageTools.getRgbColorModel();
		raster = colorModel.createCompatibleWritableRaster(
				getImageData().width, getImageData().height);
		raster.setDataElements(0, 0, getImageData().data);
		return new BufferedImage(colorModel, raster, false, null);
	}

	public BufferedImage getBufferedImage() {
		if (bufferedImage == null) {
			bufferedImage = createBufferedImage();
		}
		return bufferedImage;
	}

	public ImageData getImageData() {
		return imageData;
	}
}
