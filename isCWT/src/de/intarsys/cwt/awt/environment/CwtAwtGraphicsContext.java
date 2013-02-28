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
package de.intarsys.cwt.awt.environment;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.RenderingHints.Key;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.Map;

import de.intarsys.cwt.common.BlendMode;
import de.intarsys.cwt.common.IPaint;
import de.intarsys.cwt.environment.IGraphicsContext;
import de.intarsys.cwt.environment.IGraphicsEnvironment;
import de.intarsys.cwt.image.IImage;

/**
 * The AWT implementation of {@link IGraphicsContext}
 * 
 */
public class CwtAwtGraphicsContext implements IGraphicsContext {

	private BlendMode blendMode = BlendMode.META.getDefault();

	private Graphics2D graphics;

	private Paint paintActive;

	private Paint paintBackground;

	private Paint paintForeground;

	public CwtAwtGraphicsContext(Graphics2D paramGraphics) {
		graphics = paramGraphics;
	}

	public void clip(Shape shape) {
		if (shape == null) {
			return;
		}
		if (shape.getPathIterator(null).isDone()) {
			return;
		}
		graphics.clip(shape);
	}

	public void dispose() {
		graphics.dispose();
	}

	public void disposeClip(Object clip) {
		// nothing to do for awt
	}

	public void draw(Shape s) {
		if (paintActive != paintForeground) {
			graphics.setPaint(paintForeground);
			paintActive = paintForeground;
		}
		graphics.draw(s);
	}

	public void drawImage(IImage image, float x, float y) {
		image.drawFromGraphicsContext(this, x, y);
	}

	public void drawString(String s, float x, float y) {
		if (paintActive != paintForeground) {
			graphics.setPaint(paintForeground);
			paintActive = paintForeground;
		}
		graphics.drawString(s, x, y);
	}

	public void fill(Shape s) {
		if (paintActive != paintBackground) {
			graphics.setPaint(paintBackground);
			paintActive = paintBackground;
		}
		graphics.fill(s);
	}

	public Color getBackgroundColor() {
		if (paintBackground instanceof Color) {
			return (Color) paintBackground;
		}
		return graphics.getBackground();
	}

	public BlendMode getBlendMode() {
		return blendMode;
	}

	public Shape getClip() {
		return graphics.getClip();
	}

	public Font getFont() {
		return graphics.getFont();
	}

	public FontRenderContext getFontRenderContext() {
		return graphics.getFontRenderContext();
	}

	public Color getForegroundColor() {
		if (paintForeground instanceof Color) {
			return (Color) paintForeground;
		}
		return graphics.getColor();
	}

	public Graphics2D getGraphics() {
		return graphics;
	}

	public IGraphicsEnvironment getGraphicsEnvironment() {
		return CwtAwtGraphicsEnvironment.get();
	}

	public RenderingHints getRenderingHints() {
		return graphics.getRenderingHints();
	}

	public AffineTransform getTransform() {
		return graphics.getTransform();
	}

	public void resetAdvanced() {
		// 
	}

	public void rotate(float theta) {
		graphics.rotate(theta);
	}

	public void scale(float x, float y) {
		graphics.scale(x, y);
	}

	public void setBackgroundColor(Color color) {
		graphics.setBackground(color);
		paintBackground = color;
	}

	public void setBackgroundPaint(IPaint paint) {
		paint.setBackgroundPaintFromGraphicsContext(this);
	}

	public void setBackgroundPaint(Paint paint) {
		paintBackground = paint;
	}

	public void setBlendMode(BlendMode blendMode) {
		this.blendMode = blendMode;
	}

	public void setClip(Shape shape) {
		if (shape == null) {
			graphics.setClip(null);
			return;
		}
		if (shape.getPathIterator(null).isDone()) {
			// empty clipping path, might result from intersection; will result
			// in no clipping at all
			graphics.setClip(0, 0, 0, 0);
			return;
		}
		graphics.setClip(shape);
	}

	public void setFont(Font font) {
		graphics.setFont(font);
	}

	public void setForegroundColor(Color c) {
		paintForeground = c;
	}

	public void setForegroundPaint(IPaint paint) {
		paint.setForegroundPaintFromGraphicsContext(this);
	}

	public void setForegroundPaint(Paint paint) {
		paintForeground = paint;
	}

	public void setRenderingHint(Key hintKey, Object hintValue) {
		graphics.setRenderingHint(hintKey, hintValue);
	}

	public void setRenderingHints(Map hints) {
		graphics.setRenderingHints(hints);
	}

	public void setStroke(Stroke s) {
		graphics.setStroke(s);
	}

	public void setTransform(AffineTransform Tx) {
		graphics.setTransform(Tx);
	}

	public void transform(AffineTransform Tx) {
		graphics.transform(Tx);
	}

	public void translate(float x, float y) {
		graphics.translate(x, y);
	}

}
