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
package de.intarsys.cwt.freetype;

import de.intarsys.cwt.freetype.nativec.FTOutline;
import de.intarsys.cwt.freetype.nativec.FTVector;
import de.intarsys.cwt.freetype.nativec._FTNI;
import de.intarsys.nativec.api.INativeHandle;
import de.intarsys.nativec.type.NativeNumber;

public class Outline {

	private _FTNI ftni;

	private FTOutline outline;

	private INativeHandle pointsHandle;

	private INativeHandle tagsHandle;

	protected Outline(_FTNI paramFtni, FTOutline paramOutline) {
		ftni = paramFtni;
		outline = paramOutline;
		pointsHandle = outline.getPoints().getNativeHandle();
		tagsHandle = outline.getTags().getNativeHandle();
	}

	public int getContour(int index) {
		return outline.getContours().getShort(index << 1);
	}

	public int getFlags() {
		return outline.getFlags();
	}

	public int getNumContours() {
		return outline.getNumContours();
	}

	public FTOutline getOutline() {
		return outline;
	}

	public int getPointX(int index) {
		return pointsHandle.getInt((index << FTVector.SHIFT_VECTOR));
	}

	public int getPointY(int index) {
		return pointsHandle.getInt((index << FTVector.SHIFT_VECTOR)
				+ NativeNumber.SIZE_LONG);
	}

	public byte getTag(int index) {
		return tagsHandle.getByte(index);
	}

	public boolean isEvenOddFill() {
		return (getFlags() & Freetype.OUTLINE_EVEN_ODD_FILL) != 0;
	}

	public boolean isOwner() {
		return (getFlags() & Freetype.OUTLINE_OWNER) != 0;
	}

	public boolean isReverseFill() {
		return (getFlags() & Freetype.OUTLINE_REVERSE_FILL) != 0;
	}
}
