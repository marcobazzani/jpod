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

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.printing.Printer;

import de.intarsys.cwt.environment.IGraphicsContext;
import de.intarsys.cwt.swt.environment.CwtSwtGraphicsContext;

public class CwtSwtImage implements ISwtImage {

	public static final boolean IS_GTK = "gtk".equals(SWT.getPlatform()); //$NON-NLS-1$
	private int height;
	private Image swtImage;
	private ImageData swtImageData;

	private int width;

	public CwtSwtImage(ImageData imageData) {
		swtImageData = imageData;
		setWidth(imageData.width);
		setHeight(imageData.height);
	}

	protected Image createSwtImage(Device device) {
		return new Image(device, getImageData());
	}

	public void drawFromGraphicsContext(IGraphicsContext graphicsContext,
			float x, float y) {
		Transform transform;
		GC gc = ((CwtSwtGraphicsContext) graphicsContext).getGc();
		ImageData imageData = getImageData();
		/*
		 * swt seems to have a size limit for images; above a certain size
		 * images are simply not displayed anymore; don't know the exact value
		 */
		transform = null;
		if (imageData.data.length > (Integer.MAX_VALUE >> 7)) { // arbitrary
			float[] elements;
			Transform tempTransform;
			double scaleX;
			double scaleY;

			transform = new Transform(gc.getDevice());
			gc.getTransform(transform);
			elements = new float[6];
			transform.getElements(elements);
			double tx = elements[0];
			double ty = elements[3];
			double sx = elements[1];
			double sy = elements[2];
			scaleX = Math.pow((tx * tx) + (sy * sy), 0.5);
			scaleY = Math.pow((ty * ty) + (sx * sx), 0.5);
			imageData = imageData.scaledTo((int) Math.round(imageData.width
					* scaleX), (int) Math.round(imageData.height * scaleY));
			tempTransform = new Transform(gc.getDevice(), elements);
			tempTransform.scale((float) (1 / scaleX), (float) (1 / scaleY));
			gc.setTransform(tempTransform);
			tempTransform.dispose();
		}

		try {
			// workaround for eclipse bug #174027
			byte[] bytes = null;
			if (IS_GTK && gc.getDevice() instanceof Printer) {
				if (imageData.transparentPixel == -1
						&& imageData.alphaData == null
						&& imageData.maskData == null) {
					bytes = new byte[imageData.width * imageData.height];
					Arrays.fill(bytes, (byte) -1);
					imageData.alphaData = bytes;
				}
			}
			try {
				Image deviceImage = new Image(gc.getDevice(), imageData);
				try {
					gc.drawImage(deviceImage, (int) x, (int) y);
				} finally {
					deviceImage.dispose();
				}
			} finally {
				if (bytes != null) {
					imageData.alphaData = null;
				}
			}
		} finally {
			if (transform != null) {
				gc.setTransform(transform);
				transform.dispose();
			}
		}
	}

	public int getHeight() {
		return height;
	}

	public Image getImage(Device device) {
		if (swtImage == null) {
			swtImage = createSwtImage(device);
		}
		return swtImage;
	}

	public ImageData getImageData() {
		return swtImageData;
	}

	public int getWidth() {
		return width;
	}

	protected void setHeight(int paramHeight) {
		height = paramHeight;
	}

	protected void setWidth(int paramWidth) {
		width = paramWidth;
	}
}
