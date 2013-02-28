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
package de.intarsys.cwt.image;

import java.io.IOException;

import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;

import org.w3c.dom.Node;

public class ImageMetadata {
	public static ImageMetadata createFromImageReader(ImageReader imageReader)
			throws IOException {
		IIOMetadata[] imageMetadata;

		imageMetadata = new IIOMetadata[imageReader.getNumImages(true)];
		for (int index = 0; index < imageMetadata.length; index++) {
			imageMetadata[index] = imageReader.getImageMetadata(index);
		}

		return new ImageMetadata(imageReader.getStreamMetadata(), imageMetadata);

	}

	private IIOMetadata[] imageMetadata;

	private IIOMetadata streamMetadata;

	protected ImageMetadata() {
		super();
	}

	public ImageMetadata(IIOMetadata paramStreamMetadata,
			IIOMetadata[] paramImageMetadata) {
		streamMetadata = paramStreamMetadata;
		imageMetadata = paramImageMetadata;
	}

	public Node getAsTree(String formatName) {
		Node root;

		root = new IIOMetadataNode("root");
		if (streamMetadata != null) {
			root.appendChild(streamMetadata.getAsTree(formatName));
		}
		for (int index = 0; index < imageMetadata.length; index++) {
			Node node;

			node = new IIOMetadataNode(String.valueOf(index));
			root.appendChild(node);
			node.appendChild(imageMetadata[index].getAsTree(formatName));
		}
		return root;
	}

	public IIOMetadata[] getImageMetadata() {
		return imageMetadata;
	}

	public String getNativeMetadataFormatName() {
		// expect at least 1 image metadata entry
		return imageMetadata[0].getNativeMetadataFormatName();
	}

	public IIOMetadata getStreamMetadata() {
		return streamMetadata;
	}

	protected void setImageMetadata(IIOMetadata[] paramMetadata) {
		imageMetadata = paramMetadata;
	}

	protected void setStreamMetadata(IIOMetadata paramMetadata) {
		streamMetadata = paramMetadata;
	}
}
