package de.intarsys.cwt.hybrid.image;

import java.awt.image.BufferedImage;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

import de.intarsys.cwt.awt.environment.CwtAwtGraphicsContext;
import de.intarsys.cwt.awt.image.CwtAwtImage;
import de.intarsys.cwt.awt.image.IAwtImage;
import de.intarsys.cwt.environment.IGraphicsContext;
import de.intarsys.cwt.swt.environment.CwtSwtGraphicsContext;
import de.intarsys.cwt.swt.image.CwtSwtImage;
import de.intarsys.cwt.swt.image.ISwtImage;
import de.intarsys.cwt.swt.image.ImageConverterAwt2Swt;
import de.intarsys.cwt.swt.image.ImageConverterSwt2Awt;

public class CwtHybridImage implements IAwtImage, ISwtImage {

	private CwtAwtImage awtImage;

	private CwtSwtImage swtImage;

	public CwtHybridImage(BufferedImage bufferedImage) {
		super();
		this.awtImage = new CwtAwtImage(bufferedImage);
	}

	public CwtHybridImage(CwtAwtImage awtImage) {
		super();
		this.awtImage = awtImage;
	}

	public CwtHybridImage(CwtSwtImage swtImage) {
		super();
		this.swtImage = swtImage;
	}

	public CwtHybridImage(ImageData imageData) {
		super();
		this.swtImage = new CwtSwtImage(imageData);
	}

	protected CwtAwtImage createAwtImage() {
		ImageConverterSwt2Awt converter = new ImageConverterSwt2Awt(swtImage
				.getImageData());
		BufferedImage bufferedImage = converter.getBufferedImage();
		return new CwtAwtImage(bufferedImage);
	}

	protected CwtSwtImage createSwtImage() {
		ImageConverterAwt2Swt converter = new ImageConverterAwt2Swt(awtImage
				.getBufferedImage());
		ImageData imageData = converter.getImageData();
		return new CwtSwtImage(imageData);
	}

	protected void drawFromAwt(CwtAwtGraphicsContext graphicsContext, float x,
			float y) {
		if (awtImage == null) {
			awtImage = createAwtImage();
		}
		graphicsContext.drawImage(awtImage, x, y);
	}

	public void drawFromGraphicsContext(IGraphicsContext graphicsContext,
			float x, float y) {
		if (graphicsContext instanceof CwtSwtGraphicsContext) {
			drawFromSwt((CwtSwtGraphicsContext) graphicsContext, x, y);
		} else if (graphicsContext instanceof CwtAwtGraphicsContext) {
			drawFromAwt((CwtAwtGraphicsContext) graphicsContext, x, y);
		} else {
			throw new IllegalArgumentException("graphicsContext not supported");
		}
	}

	protected void drawFromSwt(CwtSwtGraphicsContext graphicsContext, float x,
			float y) {
		if (swtImage == null) {
			try {
				swtImage = createSwtImage();
			} catch (RuntimeException e) {
				// todo 0 dummy / error image
				return;
			}
		}
		CwtSwtGraphicsContext gc = graphicsContext;
		graphicsContext.drawImage(swtImage, x, y);
	}

	public BufferedImage getBufferedImage() {
		if (awtImage == null) {
			awtImage = createAwtImage();
		}
		return awtImage.getBufferedImage();
	}

	public int getHeight() {
		if (swtImage != null) {
			return swtImage.getHeight();
		}
		if (awtImage != null) {
			return awtImage.getHeight();
		}
		return 0;
	}

	public Image getImage(Device device) {
		if (swtImage == null) {
			swtImage = createSwtImage();
		}
		return swtImage.getImage(device);
	}

	public ImageData getImageData() {
		if (swtImage == null) {
			swtImage = createSwtImage();
		}
		return swtImage.getImageData();
	}

	public int getWidth() {
		if (swtImage != null) {
			return swtImage.getWidth();
		}
		if (awtImage != null) {
			return awtImage.getWidth();
		}
		return 0;
	}

}
