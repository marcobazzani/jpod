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
package de.intarsys.cwt.hybrid.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

import de.intarsys.cwt.awt.image.IAwtImage;
import de.intarsys.cwt.environment.IGraphicsContext;
import de.intarsys.cwt.image.IImage;
import de.intarsys.cwt.image.IImageContainer;
import de.intarsys.cwt.image.ImageEntry;
import de.intarsys.cwt.swt.image.ISwtImage;

public class ImageIOImageEntry extends ImageEntry implements IAwtImage,
		ISwtImage {

	private static final int width = 400;

	private static final int height = 400;

	private int index;

	public ImageIOImageEntry(IImageContainer container, int index) {
		super(container);
		this.index = index;
	}

	public void drawFromGraphicsContext(IGraphicsContext graphicsContext,
			float x, float y) {
		getDelegate().drawFromGraphicsContext(graphicsContext, x, y);
	}

	public BufferedImage getBufferedImage() {
		return ((IAwtImage) getDelegate()).getBufferedImage();
	}

	@Override
	synchronized protected IImage getErrorImage(Exception e) {
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D gc = image.createGraphics();
		gc.setBackground(Color.WHITE);
		gc.fillRect(0, 0, width, height);
		gc.setColor(Color.RED);
		gc.drawLine(0, 0, width, height);
		gc.drawLine(0, height, width, 0);
		gc.setColor(Color.BLACK);
		gc.drawString("error loading image (" + e.getMessage() + ")", 10, 20);
		return new CwtHybridImage(image);
	}

	public Image getImage(Device device) {
		return ((ISwtImage) getDelegate()).getImage(device);
	}

	public ImageData getImageData() {
		return ((ISwtImage) getDelegate()).getImageData();
	}

	public int getIndex() {
		return index;
	}

	@Override
	protected IImage loadContent() throws IOException {
		return myContainer().loadImage(getIndex());
	}

	private ImageIOImageContainer myContainer() {
		return (ImageIOImageContainer) getContainer();
	}
}
