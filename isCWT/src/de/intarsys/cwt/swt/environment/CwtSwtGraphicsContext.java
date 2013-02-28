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
package de.intarsys.cwt.swt.environment;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.RenderingHints.Key;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.LineAttributes;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.graphics.Transform;

import de.intarsys.cwt.common.BlendMode;
import de.intarsys.cwt.common.IGraphicsObject;
import de.intarsys.cwt.common.IPaint;
import de.intarsys.cwt.environment.IGraphicsContext;
import de.intarsys.cwt.environment.IGraphicsEnvironment;
import de.intarsys.cwt.image.IImage;
import de.intarsys.tools.geometry.ApplySpaceChangeShape;
import de.intarsys.tools.geometry.ShapeTools;
import de.intarsys.tools.geometry.TransformedShape;
import de.intarsys.tools.resourcetracker.ResourceTracker;

/**
 * An {@link IGraphicsContext} mapping to SWT.
 * <p>
 * It is currently not safe to use this {@link IGraphicsContext} concurrently.
 */
public class CwtSwtGraphicsContext implements IGraphicsContext {

	static final int BACKGROUND = 1 << 1;

	static final int FOREGROUND = 1 << 0;

	/**
	 * A tracker handling path resources used by shapes. The path will be
	 * disposed when the shape is no longer referenced.
	 */
	private static ResourceTracker tracker = new ResourceTracker() {
		@Override
		protected void basicDispose(Object resource) {
			((Path) resource).dispose();
		}
	};

	static public Path toPath(Device device, PathIterator pathIterator) {
		Path path;
		float[] coords;

		path = new Path(device);
		if (pathIterator.isDone()) {
			// empty path, might result from intersection
			path.addRectangle(0, 0, 0, 0);
			return path;
		}
		coords = new float[6];
		while (!pathIterator.isDone()) {
			int type;

			type = pathIterator.currentSegment(coords);
			switch (type) {
			case PathIterator.SEG_CLOSE:
				path.close();
				break;
			case PathIterator.SEG_CUBICTO:
				path.cubicTo(coords[0], coords[1], coords[2], coords[3],
						coords[4], coords[5]);
				break;
			case PathIterator.SEG_LINETO:
				path.lineTo(coords[0], coords[1]);
				break;
			case PathIterator.SEG_MOVETO:
				path.moveTo(coords[0], coords[1]);
				break;
			case PathIterator.SEG_QUADTO:
				path.quadTo(coords[0], coords[1], coords[2], coords[3]);
				break;
			default:
				throw new InternalError(
						"Program execution should not reach this point.");
			}
			pathIterator.next();
		}
		return path;
	}

	static public Rectangle2D toRectangle2D(Rectangle rect) {
		return new Rectangle2D.Float(rect.x, rect.y, rect.width, rect.height);
	}

	/**
	 * This methods intentionally duplicated to avoid compile time dependency..
	 * 
	 */
	static public Transform toTransform(Device device, AffineTransform at) {
		double[] tempElements = new double[6];
		at.getMatrix(tempElements);
		return new Transform(device, (float) tempElements[0],
				(float) tempElements[1], (float) tempElements[2],
				(float) tempElements[3], (float) tempElements[4],
				(float) tempElements[5]);
	}

	/**
	 * This methods intentionally duplicated to avoid compile time dependency..
	 * 
	 */
	static public Transform toTransform(Device device, Transform st,
			AffineTransform at) {
		if (st == null) {
			return new Transform(device, (float) at.getScaleX(), (float) at
					.getShearX(), (float) at.getShearY(), (float) at
					.getScaleY(), (float) at.getTranslateX(), (float) at
					.getTranslateY());
		}
		st.setElements((float) at.getScaleX(), (float) at.getShearX(),
				(float) at.getShearY(), (float) at.getScaleY(), (float) at
						.getTranslateX(), (float) at.getTranslateY());
		return st;
	}

	private boolean applyAlphaBackground;

	private boolean applyAlphaForeground;

	private boolean applyTransform = false;

	private Color backgroundColor;

	private Pattern backgroundPattern;

	private BlendMode blendMode = BlendMode.META.getDefault();

	private AffineTransform cachedAWTShapeTransform;

	private Transform cachedSWTShapeTransform;

	private TransformedShape currentClip;

	private AffineTransform currentTransform;

	private Device device;

	private float[] elements = new float[6];

	private double[] flatmatrix = new double[6];

	private org.eclipse.swt.graphics.Font font;

	private Transform fontTransform;

	private Color foregroundColor;

	private Pattern foregroundPattern;

	private GC gc;

	private int swtBackgroundAlpha;

	private org.eclipse.swt.graphics.Color swtBackgroundColor;

	private int swtForegroundAlpha;

	private org.eclipse.swt.graphics.Color swtForegroundColor;

	private Transform swtTransform;

	public CwtSwtGraphicsContext(GC paramGC, Device paramDevice) {
		swtBackgroundAlpha = 255;
		swtBackgroundColor = null;
		backgroundColor = null;
		device = paramDevice;
		font = null;
		swtForegroundAlpha = 255;
		swtForegroundColor = null;
		foregroundColor = null;
		gc = paramGC;
		applyAlphaForeground = false;
		applyAlphaBackground = false;

		swtTransform = new Transform(paramDevice);
		paramGC.getTransform(swtTransform);
		swtTransform.getElements(elements);
		currentTransform = new AffineTransform(elements);

		// this is the "default" clipping
		Rectangle2D rect = toRectangle2D(paramGC.getClipping());
		currentClip = ApplySpaceChangeShape.create(new Area(rect),
				currentTransform);

		// when setting to SWT.DEFAULT here curved shapes (like letters) will
		// look awful on gtk
		paramGC.setAntialias(SWT.OFF);
		paramGC.setInterpolation(SWT.NONE);
	}

	protected void applyAlphaBackground() {
		if (applyAlphaBackground) {
			applyAlphaBackground = false;
			if (gc.getAlpha() != swtBackgroundAlpha) {
				gc.setAlpha(swtBackgroundAlpha);
				if (swtForegroundAlpha != swtBackgroundAlpha) {
					applyAlphaForeground = true;
				}
			}
		}
	}

	protected void applyAlphaForeground() {
		if (applyAlphaForeground) {
			applyAlphaForeground = false;
			if (gc.getAlpha() != swtForegroundAlpha) {
				gc.setAlpha(swtForegroundAlpha);
				if (swtBackgroundAlpha != swtForegroundAlpha) {
					applyAlphaBackground = true;
				}
			}
		}
	}

	protected void applyClip() {
		if (currentClip == null) {
			gc.setClipping((Region) null);
		} else {
			// transform gc before clip is set
			applyTransform();
			PathIterator pathIterator = currentClip.getPathIterator(null);
			if (pathIterator.getWindingRule() == PathIterator.WIND_EVEN_ODD) {
				gc.setFillRule(SWT.FILL_EVEN_ODD);
			} else {
				gc.setFillRule(SWT.FILL_WINDING);
			}
			Path clipPath = toPath(device, pathIterator);
			gc.setClipping(clipPath);
			clipPath.dispose();
		}
	}

	protected void applyTransform() {
		if (applyTransform) {
			applyTransform = false;
			currentTransform.getMatrix(flatmatrix);
			swtTransform.setElements((float) flatmatrix[0],
					(float) flatmatrix[1], (float) flatmatrix[2],
					(float) flatmatrix[3], (float) flatmatrix[4],
					(float) flatmatrix[5]);
			gc.setTransform(swtTransform);
		}
	}

	public void clip(Shape shape) {
		Area tempShape = ShapeTools.createArea(shape, false);
		if (currentClip == null) {
			currentClip = ApplySpaceChangeShape.create(tempShape,
					currentTransform);
		} else {
			Area tempClip = ShapeTools.createArea(currentClip, true);
			tempClip.intersect(tempShape);
			currentClip = ApplySpaceChangeShape.create(tempClip,
					currentTransform);
		}
		applyClip();
	}

	protected Path createPath(Shape s) {
		Path path;
		if (s instanceof IGraphicsObject) {
			path = (Path) ((IGraphicsObject) s).getData();
			if (path != null && !path.isDisposed()
					&& !path.getDevice().isDisposed()) {
				return path;
			}
		}
		if (s instanceof Rectangle2D) {
			Rectangle2D rect = (Rectangle2D) s;
			path = new Path(device);
			path.addRectangle((float) rect.getX(), (float) rect.getY(),
					(float) rect.getWidth(), (float) rect.getHeight());
		} else {
			path = toPath(device, s.getPathIterator(null));
		}
		tracker.trackPhantom(s, path);
		if (s instanceof IGraphicsObject) {
			((IGraphicsObject) s).setData(path);
		}
		return path;
	}

	public void dispose() {
		// The underlying GC might be used beyond the lifetime of this
		// IGraphicsContext implementation. Thus we have to assert that eventual
		// transformation resettings are propagated properly to the GC.
		applyTransform();
		if (swtBackgroundColor != null) {
			swtBackgroundColor.dispose();
		}
		if (backgroundPattern != null) {
			backgroundPattern.dispose();
		}
		if (font != null) {
			font.dispose();
		}
		if (fontTransform != null) {
			fontTransform.dispose();
		}
		if (swtForegroundColor != null) {
			swtForegroundColor.dispose();
		}
		if (foregroundPattern != null) {
			foregroundPattern.dispose();
		}
		if (swtTransform != null) {
			swtTransform.dispose();
		}
		if (cachedSWTShapeTransform != null) {
			cachedSWTShapeTransform.dispose();
		}
	}

	public void draw(Shape s) {
		applyAlphaForeground();
		if (s instanceof TransformedShape && gc.getForegroundPattern() == null) {
			AffineTransform awtShapeTransform = ((TransformedShape) s)
					.getTransform();
			if (awtShapeTransform != null) {
				// do NOT applyTransform here
				if (awtShapeTransform != cachedAWTShapeTransform) {
					cachedAWTShapeTransform = awtShapeTransform;
					if (cachedSWTShapeTransform != null) {
						cachedSWTShapeTransform.dispose();
					}
					cachedSWTShapeTransform = toTransform(device,
							awtShapeTransform);
				}
				Transform tempTransform = new Transform(device);
				currentTransform.getMatrix(flatmatrix);
				tempTransform.setElements((float) flatmatrix[0],
						(float) flatmatrix[1], (float) flatmatrix[2],
						(float) flatmatrix[3], (float) flatmatrix[4],
						(float) flatmatrix[5]);
				tempTransform.multiply(cachedSWTShapeTransform);
				gc.setTransform(tempTransform);
			} else {
				applyTransform();
			}
			Shape baseShape = ((TransformedShape) s).getBaseShape();
			gc.drawPath(createPath(baseShape));
			gc.setTransform(swtTransform);
		} else {
			applyTransform();
			gc.drawPath(createPath(s));
		}
	}

	public void drawImage(IImage image, float x, float y) {
		applyAlphaBackground();
		applyTransform();
		image.drawFromGraphicsContext(this, x, y);
	}

	public void drawString(String s, float x, float y) {
		applyTransform();
		boolean reset;
		applyAlphaForeground();
		reset = false;
		if (fontTransform != null) {
			float[] point;

			if (swtTransform == null) {
				swtTransform = new Transform(device);
			}
			gc.getTransform(swtTransform);
			swtTransform.getElements(elements);
			swtTransform.translate(x, y);
			point = new float[] { 0, 0 - gc.textExtent(s).y };
			fontTransform.transform(point);
			swtTransform.translate(point[0], point[1]);
			swtTransform.multiply(fontTransform);
			gc.setTransform(swtTransform);
			reset = true;
		}
		try {
			gc.drawString(s, 0, 0);
		} finally {
			if (reset) {
				swtTransform.setElements(elements[0], elements[1], elements[2],
						elements[3], elements[4], elements[5]);
				gc.setTransform(swtTransform);
			}
		}
	}

	public void fill(Shape s) {
		applyAlphaBackground();
		if (s instanceof TransformedShape && gc.getBackgroundPattern() == null) {
			AffineTransform awtShapeTransform = ((TransformedShape) s)
					.getTransform();
			if (awtShapeTransform != null) {
				// do NOT applyTransform here
				if (awtShapeTransform != cachedAWTShapeTransform) {
					cachedAWTShapeTransform = awtShapeTransform;
					if (cachedSWTShapeTransform != null) {
						cachedSWTShapeTransform.dispose();
					}
					cachedSWTShapeTransform = toTransform(device,
							awtShapeTransform);
				}
				Transform tempTransform = new Transform(device);
				currentTransform.getMatrix(flatmatrix);
				tempTransform.setElements((float) flatmatrix[0],
						(float) flatmatrix[1], (float) flatmatrix[2],
						(float) flatmatrix[3], (float) flatmatrix[4],
						(float) flatmatrix[5]);
				tempTransform.multiply(cachedSWTShapeTransform);
				gc.setTransform(tempTransform);
			} else {
				applyTransform();
			}
			Shape baseShape = ((TransformedShape) s).getBaseShape();
			if (ShapeTools.getWindingRule(baseShape) == PathIterator.WIND_EVEN_ODD) {
				gc.setFillRule(SWT.FILL_EVEN_ODD);
			} else {
				gc.setFillRule(SWT.FILL_WINDING);
			}
			Path path = createPath(baseShape);
			gc.fillPath(path);
			gc.setTransform(swtTransform);
		} else {
			applyTransform();
			PathIterator pathIterator = s.getPathIterator(null);
			if (pathIterator.getWindingRule() == PathIterator.WIND_EVEN_ODD) {
				gc.setFillRule(SWT.FILL_EVEN_ODD);
			} else {
				gc.setFillRule(SWT.FILL_WINDING);
			}
			Path path = createPath(s);
			gc.fillPath(path);
		}
	}

	public Color getBackgroundColor() {
		if (backgroundColor == null) {
			RGB rgb = swtBackgroundColor.getRGB();
			backgroundColor = new Color(rgb.red, rgb.green, rgb.blue);
		}
		return backgroundColor;
	}

	public BlendMode getBlendMode() {
		return blendMode;
	}

	public Shape getClip() {
		if (currentClip == null) {
			return null;
		}
		return (Shape) currentClip.clone();
	}

	public Device getDevice() {
		return device;
	}

	public Font getFont() {
		// TODO;
		return null;
	}

	public FontRenderContext getFontRenderContext() {
		// TODO
		return null;
	}

	public Color getForegroundColor() {
		if (foregroundColor == null) {
			RGB rgb = swtForegroundColor.getRGB();
			foregroundColor = new Color(rgb.red, rgb.green, rgb.blue);
		}
		return foregroundColor;
	}

	public GC getGc() {
		return gc;
	}

	public IGraphicsEnvironment getGraphicsEnvironment() {
		return CwtSwtGraphicsEnvironment.get();
	}

	public RenderingHints getRenderingHints() {
		RenderingHints renderingHints;
		int value;

		renderingHints = new RenderingHints(null);

		value = gc.getAntialias();
		if (value == SWT.DEFAULT) {
			renderingHints.put(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_DEFAULT);
		} else if (value == SWT.OFF) {
			renderingHints.put(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_OFF);
		} else {
			renderingHints.put(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
		}

		value = gc.getInterpolation();
		if (value == SWT.HIGH) {
			renderingHints.put(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		} else {
			renderingHints.put(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		}

		value = gc.getTextAntialias();
		if (value == SWT.DEFAULT) {
			renderingHints.put(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
		} else if (value == SWT.OFF) {
			renderingHints.put(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		} else {
			renderingHints.put(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		}

		// TODO transfer other values
		return renderingHints;
	}

	public AffineTransform getTransform() {
		return (AffineTransform) currentTransform.clone();
	}

	protected float[] getTransformationValues(AffineTransform tx) {
		tx.getMatrix(flatmatrix);
		elements[0] = (float) flatmatrix[0];
		elements[1] = (float) flatmatrix[1];
		elements[2] = (float) flatmatrix[2];
		elements[3] = (float) flatmatrix[3];
		elements[4] = (float) flatmatrix[4];
		elements[5] = (float) flatmatrix[5];
		return elements;
	}

	public void resetAdvanced() {
		gc.setAdvanced(false);
	}

	public void rotate(float theta) {
		currentTransform.rotate(theta);
		applyTransform = true;
		if (currentClip != null) {
			currentClip.invalidate();
		}
	}

	public void scale(float x, float y) {
		currentTransform.scale(x, y);
		applyTransform = true;
		if (currentClip != null) {
			currentClip.invalidate();
		}
	}

	public void setBackgroundColor(Color color) {
		backgroundColor = color;
		swtBackgroundAlpha = color.getAlpha();
		applyAlphaBackground = true;
		org.eclipse.swt.graphics.Color newColor = new org.eclipse.swt.graphics.Color(
				device, color.getRed(), color.getGreen(), color.getBlue());
		setBackgroundColor(newColor);
	}

	public void setBackgroundColor(org.eclipse.swt.graphics.Color resource) {
		if (swtBackgroundColor != null) {
			swtBackgroundColor.dispose();
		}
		swtBackgroundColor = resource;
		backgroundColor = null;
		gc.setBackground(swtBackgroundColor);
	}

	public void setBackgroundPaint(IPaint paint) {
		swtBackgroundAlpha = (int) (paint.getAlphaValue() * 255);
		applyAlphaBackground = true;
		paint.setBackgroundPaintFromGraphicsContext(this);
	}

	public void setBackgroundPattern(Pattern resource) {
		if (backgroundPattern != null) {
			backgroundPattern.dispose();
		}
		backgroundPattern = resource;
		gc.setBackgroundPattern(backgroundPattern);
	}

	public void setBlendMode(BlendMode blendMode) {
		this.blendMode = blendMode;
	}

	public void setClip(Shape shape) {
		if (shape == null) {
			currentClip = null;
		} else {
			currentClip = ApplySpaceChangeShape.create(ShapeTools
					.createArea(shape, false), currentTransform);
		}
		applyClip();
	}

	public void setFont(Font awtFont) {
		int style;
		AffineTransform tx;

		if (font != null) {
			font.dispose();
		}
		style = SWT.NORMAL;
		if ((awtFont.getStyle() & Font.BOLD) > 0) {
			style |= SWT.BOLD;
		}
		if ((awtFont.getStyle() & Font.ITALIC) > 0) {
			style |= SWT.ITALIC;
		}
		if (fontTransform != null) {
			fontTransform.dispose();
		}
		if ((tx = awtFont.getTransform()).isIdentity()) {
			fontTransform = null;
		} else {
			fontTransform = new Transform(device, getTransformationValues(tx));
		}
		font = new org.eclipse.swt.graphics.Font(device, awtFont.getFontName(),
				awtFont.getSize(), style);
		gc.setFont(font);
	}

	public void setForegroundColor(Color color) {
		foregroundColor = color;
		swtForegroundAlpha = color.getAlpha();
		applyAlphaForeground = true;
		org.eclipse.swt.graphics.Color newColor = new org.eclipse.swt.graphics.Color(
				device, color.getRed(), color.getGreen(), color.getBlue());
		setForegroundColor(newColor);
	}

	public void setForegroundColor(org.eclipse.swt.graphics.Color resource) {
		if (swtForegroundColor != null) {
			swtForegroundColor.dispose();
		}
		swtForegroundColor = resource;
		foregroundColor = null;
		gc.setForeground(swtForegroundColor);
	}

	public void setForegroundPaint(IPaint paint) {
		swtForegroundAlpha = (int) (paint.getAlphaValue() * 255);
		applyAlphaForeground = true;
		paint.setForegroundPaintFromGraphicsContext(this);
	}

	public void setForegroundPattern(Pattern resource) {
		if (foregroundPattern != null) {
			foregroundPattern.dispose();
		}
		foregroundPattern = resource;
		gc.setForegroundPattern(foregroundPattern);
	}

	public void setRenderingHint(Key hintKey, Object hintValue) {
		if (hintKey == RenderingHints.KEY_ALPHA_INTERPOLATION) {
			// TODO might not have an equivalent
			return;
		}
		if (hintKey == RenderingHints.KEY_ANTIALIASING) {
			if (hintValue == RenderingHints.VALUE_ANTIALIAS_DEFAULT) {
				gc.setAntialias(SWT.DEFAULT);
			} else if (hintValue == RenderingHints.VALUE_ANTIALIAS_OFF) {
				gc.setAntialias(SWT.OFF);
			} else {
				gc.setAntialias(SWT.ON);
			}
			return;
		}
		if (hintKey == RenderingHints.KEY_COLOR_RENDERING) {
			// TODO might not have an equivalent
			return;
		}
		if (hintKey == RenderingHints.KEY_DITHERING) {
			// TODO might not have an equivalent
			return;
		}
		if (hintKey == RenderingHints.KEY_FRACTIONALMETRICS) {
			// TODO might not have an equivalent
			return;
		}
		if (hintKey == RenderingHints.KEY_INTERPOLATION) {
			if (hintValue == RenderingHints.VALUE_INTERPOLATION_BICUBIC) {
				gc.setInterpolation(SWT.HIGH);
			} else if (hintValue == RenderingHints.VALUE_INTERPOLATION_BILINEAR) {
				gc.setInterpolation(SWT.HIGH);
			} else {
				gc.setInterpolation(SWT.LOW);
			}
			return;
		}
		if (hintKey == RenderingHints.KEY_RENDERING) {
			// TODO might not have an equivalent
			return;
		}
		if (hintKey == RenderingHints.KEY_STROKE_CONTROL) {
			// TODO might not have an equivalent
			return;
		}
		if (hintKey == RenderingHints.KEY_TEXT_ANTIALIASING) {
			if (hintValue == RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT) {
				gc.setTextAntialias(SWT.DEFAULT);
			} else if (hintValue == RenderingHints.VALUE_TEXT_ANTIALIAS_OFF) {
				gc.setTextAntialias(SWT.OFF);
			} else {
				gc.setTextAntialias(SWT.ON);
			}
		}
	}

	public void setRenderingHints(Map hints) {
		if (hints.containsKey(RenderingHints.KEY_ALPHA_INTERPOLATION)) {
			// TODO
		}
		if (hints.containsKey(RenderingHints.KEY_ANTIALIASING)) {
			if (hints.get(RenderingHints.KEY_ANTIALIASING) == RenderingHints.VALUE_ANTIALIAS_DEFAULT) {
				gc.setAntialias(SWT.DEFAULT);
			} else if (hints.get(RenderingHints.KEY_ANTIALIASING) == RenderingHints.VALUE_ANTIALIAS_OFF) {
				gc.setAntialias(SWT.OFF);
			} else {
				gc.setAntialias(SWT.ON);
			}
		} else {
			gc.setAntialias(SWT.DEFAULT);
		}
		if (hints.containsKey(RenderingHints.KEY_COLOR_RENDERING)) {
			// TODO
		}
		if (hints.containsKey(RenderingHints.KEY_DITHERING)) {
			// TODO
		}
		if (hints.containsKey(RenderingHints.KEY_FRACTIONALMETRICS)) {
			// TODO
		}
		if (hints.containsKey(RenderingHints.KEY_INTERPOLATION)) {
			// TODO assign values
			if (hints.get(RenderingHints.KEY_INTERPOLATION) == RenderingHints.VALUE_INTERPOLATION_BICUBIC) {
				gc.setInterpolation(SWT.HIGH);
			} else if (hints.get(RenderingHints.KEY_INTERPOLATION) == RenderingHints.VALUE_INTERPOLATION_BILINEAR) {
				gc.setInterpolation(SWT.HIGH);
			} else {
				gc.setInterpolation(SWT.LOW);
			}
		} else {
			gc.setInterpolation(SWT.NONE);
		}
		if (hints.containsKey(RenderingHints.KEY_RENDERING)) {
			// TODO
		}
		if (hints.containsKey(RenderingHints.KEY_STROKE_CONTROL)) {
			// TODO
		}
		if (hints.containsKey(RenderingHints.KEY_TEXT_ANTIALIASING)) {
			if (hints.get(RenderingHints.KEY_TEXT_ANTIALIASING) == RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT) {
				gc.setTextAntialias(SWT.DEFAULT);
			} else if (hints.get(RenderingHints.KEY_TEXT_ANTIALIASING) == RenderingHints.VALUE_TEXT_ANTIALIAS_OFF) {
				gc.setTextAntialias(SWT.OFF);
			} else {
				gc.setTextAntialias(SWT.ON);
			}
		} else {
			gc.setTextAntialias(SWT.DEFAULT);
		}
	}

	public void setStroke(Stroke s) {
		if (s instanceof BasicStroke) {
			BasicStroke stroke;
			LineAttributes attributes;
			float[] dash;
			int value;

			stroke = (BasicStroke) s;
			attributes = new LineAttributes(stroke.getLineWidth());

			dash = stroke.getDashArray();
			if (dash == null) {
				attributes.style = SWT.LINE_SOLID;
			} else {
				for (int index = 0; index < dash.length; index++) {
					if (dash[index] <= 0) {
						// workaround for strange dash definitions,this will NOT
						// create a semantically equal dash!
						dash[index] = 1f;
					}
				}
				attributes.style = SWT.LINE_CUSTOM;
				attributes.dash = dash;
			}

			value = stroke.getEndCap();
			if (value == BasicStroke.CAP_BUTT) {
				attributes.cap = SWT.CAP_FLAT;
			} else if (value == BasicStroke.CAP_ROUND) {
				attributes.cap = SWT.CAP_ROUND;
			} else {
				attributes.cap = SWT.CAP_SQUARE;
			}

			value = stroke.getLineJoin();
			if (value == BasicStroke.JOIN_BEVEL) {
				attributes.join = SWT.JOIN_BEVEL;
			} else if (value == BasicStroke.JOIN_MITER) {
				attributes.join = SWT.JOIN_MITER;
			} else {
				attributes.join = SWT.JOIN_ROUND;
			}

			gc.setLineAttributes(attributes);
			// workaround for swt bug #210412 - set explicitly
			gc.setLineCap(attributes.cap);
			return;
		}
		// TODO do we use others?
	}

	public void setTransform(AffineTransform pTransform) {
		currentTransform = (AffineTransform) pTransform.clone();
		applyTransform = true;
		if (currentClip != null) {
			currentClip = ApplySpaceChangeShape.setTransform(currentClip,
					currentTransform);
		}
	}

	public java.awt.Point textExtent(String s) {
		Point extent = gc.textExtent(s);
		return new java.awt.Point(extent.x, extent.y);
	}

	public void transform(AffineTransform deltaTransform) {
		currentTransform.concatenate(deltaTransform);
		applyTransform = true;
		if (currentClip != null) {
			currentClip.invalidate();
		}
	}

	public void translate(float x, float y) {
		currentTransform.translate(x, y);
		applyTransform = true;
		if (currentClip != null) {
			currentClip.invalidate();
		}
	}

}
