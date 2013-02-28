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
package de.intarsys.cwt.environment;

import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.RenderingHints.Key;
import java.awt.geom.AffineTransform;
import java.util.Map;

import de.intarsys.cwt.common.IPaint;
import de.intarsys.cwt.image.IImage;

/**
 * A platform independent (SWT, AWT) description of a graphics context.
 * <p>
 * The primary goal of this one is derived from the requirements enforced by
 * processing a PDF context stream.
 */
public interface IGraphicsContext {

	/**
	 * Clip the subsequent drawing operations to the shape. This adds the new
	 * clip shape to the existing one.
	 * 
	 * @param shape
	 */
	public void clip(Shape shape);

	/**
	 * Dispose the current context and all its resources.
	 */
	public void dispose();

	/**
	 * Draw <code>s</code> using the current graphics context state.
	 * 
	 * @param s
	 */
	public void draw(Shape s);

	/**
	 * Draw image <code>image</code> at coordinates <code>x</code>,
	 * <code>y</code>.
	 * 
	 * @param image
	 * @param x
	 * @param y
	 */
	public void drawImage(IImage image, float x, float y);

	/**
	 * Draw a plain text string in the graphics.
	 * 
	 * @param text
	 * @param x
	 * @param y
	 */
	public void drawString(String text, float x, float y);

	/**
	 * Fill <code>s</code> using the current graphics context state.
	 * 
	 * @param s
	 */
	public void fill(Shape s);

	/**
	 * The current background color.
	 * 
	 * @return
	 */
	public Color getBackgroundColor();

	/**
	 * The current clip.
	 * 
	 * @return
	 */
	public Shape getClip();

	/**
	 * The current foregroumd color.
	 * 
	 * @return
	 */
	public Color getForegroundColor();

	/**
	 * The associated {@link IGraphicsEnvironment}
	 * 
	 * @return
	 */
	public IGraphicsEnvironment getGraphicsEnvironment();

	/**
	 * The current {@link RenderingHints}
	 * 
	 * @return
	 */
	public RenderingHints getRenderingHints();

	/**
	 * The transformatiom currentlz active.
	 * 
	 * @return
	 */
	public AffineTransform getTransform();

	/**
	 * Rotate the currently active transformation by <code>theta</code>.
	 * 
	 * @param theta
	 */
	public void rotate(float theta);

	/**
	 * Scale the currently active transformation by <code>x</code>,
	 * <code>y</code>
	 * 
	 * @param theta
	 */
	public void scale(float x, float y);

	/**
	 * Assign a new background color.
	 * 
	 * @param color
	 */
	public void setBackgroundColor(Color color);

	/**
	 * Assign a new background {@link IPaint}.
	 * 
	 * @param paint
	 */
	public void setBackgroundPaint(IPaint paint);

	/**
	 * Assign the new <code>shape</code> as the clipping shape.
	 * 
	 * @param shape
	 */
	public void setClip(Shape shape);

	/**
	 * Set the current font for writing plain text in the graphics.
	 * 
	 * @param font
	 */
	public void setFont(Font font);

	/**
	 * Assign a new foreground color.
	 * 
	 * @param c
	 */
	public void setForegroundColor(Color c);

	/**
	 * Assign a new foreground {@link IPaint}
	 * 
	 * @param paint
	 */
	public void setForegroundPaint(IPaint paint);

	/**
	 * Set a specific rendering hint.
	 * 
	 * @param hintKey
	 * @param hintValue
	 */
	public void setRenderingHint(Key hintKey, Object hintValue);

	/**
	 * Assign the currently active rendering hints.
	 * 
	 * @param hints
	 */
	public void setRenderingHints(Map hints);

	/**
	 * Assign the currently active {@link Stroke}
	 * 
	 * @param s
	 */
	public void setStroke(Stroke s);

	/**
	 * Set the currently active {@link AffineTransform}
	 * 
	 * @param Tx
	 */
	public void setTransform(AffineTransform Tx);

	/**
	 * Transform the {@link IGraphicsEnvironment} by <code>transform</code>
	 * 
	 * @param transform
	 */
	public void transform(AffineTransform transform);

	/**
	 * translate the currently active transformation by <code>x</code>,
	 * <code>y</code>
	 * 
	 * @param theta
	 */
	public void translate(float x, float y);
}
