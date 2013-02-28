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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

import de.intarsys.cwt.image.IImage;
import de.intarsys.cwt.image.IImageEntry;
import de.intarsys.cwt.image.ImageContainer;
import de.intarsys.cwt.image.ImageMetadata;
import de.intarsys.tools.locator.ILocator;
import de.intarsys.tools.stream.StreamTools;

/*
 * DOCME
 */
public class ImageIOImageContainer extends ImageContainer {

	static public ImageIOImageContainer createFromLocator(ILocator locator)
			throws IOException {
		ImageReaderSpi provider = lookupProviderBySuffix(locator.getType());
		if (provider == null) {
			throw new IllegalArgumentException("unknown suffix");
		}
		return createFromLocator(locator, provider);
	}

	static public ImageIOImageContainer createFromLocator(ILocator locator,
			ImageReaderSpi provider) throws IOException {
		ImageIOImageContainer container = new ImageIOImageContainer(locator,
				provider);
		container.open();
		return container;
	}

	static public ImageIOImageContainer createFromLocator(ILocator locator,
			String formatName) throws IOException {
		ImageReaderSpi provider = lookupProviderByFormatName(formatName);
		if (provider == null) {
			throw new IllegalArgumentException("unknown format name");
		}
		return createFromLocator(locator, provider);
	}

	static protected ImageReaderSpi lookupProviderByFormatName(String formatName) {
		if (formatName == null) {
			return null;
		}
		for (Iterator iter = IIORegistry.getDefaultInstance()
				.getServiceProviders(ImageReaderSpi.class, true); iter
				.hasNext();) {
			ImageReaderSpi provider = (ImageReaderSpi) iter.next();
			String[] names = provider.getFormatNames();
			for (int index = 0; index < names.length; index++) {
				String currentName = names[index];
				if (formatName.equalsIgnoreCase(currentName)) {
					return provider;
				}
			}
		}
		return null;
	}

	static protected ImageReaderSpi lookupProviderBySuffix(String fileSuffix) {
		if (fileSuffix == null) {
			return null;
		}
		for (Iterator iter = IIORegistry.getDefaultInstance()
				.getServiceProviders(ImageReaderSpi.class, true); iter
				.hasNext();) {
			ImageReaderSpi provider = (ImageReaderSpi) iter.next();
			String[] suffixes = provider.getFileSuffixes();
			for (int index = 0; index < suffixes.length; index++) {
				String currentSuffix = suffixes[index];
				if (fileSuffix.equalsIgnoreCase(currentSuffix)) {
					return provider;
				}
			}
		}
		return null;
	}

	private ILocator locator;

	private ImageReader imageReader;

	private ImageInputStream imageStream;

	private ImageReaderSpi provider;

	private int count;

	private ImageMetadata metadata;

	private List imageEntries = new ArrayList();

	private InputStream inputStream;

	protected ImageIOImageContainer(ILocator locator, ImageReaderSpi provider) {
		super();
		this.locator = locator;
		this.provider = provider;
	}

	@Override
	protected void basicClose() throws IOException {
		if (imageReader != null) {
			try {
				imageReader.dispose();
			} catch (Exception e) {
				//
			}
			imageReader = null;
		}
		if (imageStream != null) {
			try {
				imageStream.close();
			} catch (Exception e) {
				// 
			}
			imageStream = null;
		}
		// must close the input stream ourself! imageStream is not required
		// (and FileCacheImapgeInputStream really doesn't) to close!
		StreamTools.close(inputStream);
	}

	public IImageEntry getImageAt(int index) {
		if (index >= getImageCount()) {
			throw new IllegalArgumentException("invalid index");
		}
		return (IImageEntry) imageEntries.get(index);
	}

	public int getImageCount() {
		return count;
	}

	public ILocator getLocator() {
		return locator;
	}

	synchronized public ImageMetadata getMetadata() throws IOException {
		if (metadata == null) {
			metadata = ImageMetadata.createFromImageReader(imageReader);
		}
		return metadata;
	}

	protected IImage loadImage(int index) throws IOException {
		if (index >= getImageCount()) {
			throw new IllegalArgumentException("invalid index");
		}
		BufferedImage bufferedImage = imageReader.read(index++);
		if (bufferedImage == null) {
			return null;
		}
		return new CwtHybridImage(bufferedImage);
	}

	protected void open() throws IOException {
		try {
			inputStream = getLocator().getInputStream();
			imageStream = ImageIO.createImageInputStream(inputStream);
			imageReader = provider.createReaderInstance();
			imageReader.setInput(imageStream);
			count = imageReader.getNumImages(true);
			for (int i = 0; i < count; i++) {
				imageEntries.add(new ImageIOImageEntry(this, i));
			}
		} catch (IOException e) {
			basicClose();
			throw e;
		} catch (Exception e) {
			basicClose();
			throw new IOException("unexpected exception opening file");
		}
	}

}
