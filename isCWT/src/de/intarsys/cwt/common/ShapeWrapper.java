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
package de.intarsys.cwt.common;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import de.intarsys.tools.geometry.IShapeWrapper;

public class ShapeWrapper implements IShape, IShapeWrapper {

	private Object data;

	private Shape shape;

	public ShapeWrapper(Shape shape) {
		super();
		this.shape = shape;
	}

	public boolean contains(double x, double y) {
		return shape.contains(x, y);
	}

	public boolean contains(double x, double y, double w, double h) {
		return shape.contains(x, y, w, h);
	}

	public boolean contains(Point2D p) {
		return shape.contains(p);
	}

	public boolean contains(Rectangle2D r) {
		return shape.contains(r);
	}

	public Shape getBaseShape() {
		return shape;
	}

	public Rectangle getBounds() {
		return shape.getBounds();
	}

	public Rectangle2D getBounds2D() {
		return shape.getBounds2D();
	}

	public Object getData() {
		return data;
	}

	public PathIterator getPathIterator(AffineTransform at) {
		return shape.getPathIterator(at);
	}

	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return shape.getPathIterator(at, flatness);
	}

	public Shape getShape() {
		return shape;
	}

	public boolean intersects(double x, double y, double w, double h) {
		return shape.intersects(x, y, w, h);
	}

	public boolean intersects(Rectangle2D r) {
		return shape.intersects(r);
	}

	public void setData(Object pData) {
		data = pData;
	}

}
