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
package de.intarsys.cwt.freetype.nativec;

import de.intarsys.nativec.api.INativeHandle;
import de.intarsys.nativec.type.INativeObject;
import de.intarsys.nativec.type.INativeType;
import de.intarsys.nativec.type.NativeArray;
import de.intarsys.nativec.type.NativeByte;
import de.intarsys.nativec.type.NativeInt;
import de.intarsys.nativec.type.NativeShort;
import de.intarsys.nativec.type.NativeStaticStruct;
import de.intarsys.nativec.type.NativeStructType;
import de.intarsys.nativec.type.StructMember;

/**
 * 
 */
public class FTOutline extends NativeStaticStruct {
	/**
	 * The meta class implementation
	 */
	public static class MetaClass extends NativeStructType {
		protected MetaClass(Class instanceClass) {
			super(instanceClass);
		}

		@Override
		public INativeObject createNative(INativeHandle handle) {
			return new FTOutline(handle);
		}

	}

	private static final StructMember contours;

	private static final StructMember flags;

	/** The meta class instance */
	public static final MetaClass META = new MetaClass(MetaClass.class
			.getDeclaringClass());

	private static final StructMember nContours;

	private static final StructMember nPoints;

	private static final StructMember points;

	private static final StructMember tags;

	static {
		nContours = META.declare("nContours", NativeShort.META); //$NON-NLS-1$
		nPoints = META.declare("nPoints", NativeShort.META); //$NON-NLS-1$
		points = META.declare("points", FTVector.META.Array(0).Ref()); //$NON-NLS-1$
		tags = META.declare("tags", NativeByte.META.Array(0).Ref()); //$NON-NLS-1$
		contours = META.declare("contours", NativeShort.META.Array(0).Ref()); //$NON-NLS-1$
		flags = META.declare("flags", NativeInt.META); //$NON-NLS-1$
	}

	public FTOutline() {
		super();
	}

	protected FTOutline(INativeHandle handle) {
		super(handle);
	}

	public NativeArray getContours() {
		NativeArray array = (NativeArray) contours.getValue(this);
		array.setSize(getNumContours());
		return array;
	}

	public int getFlags() {
		return flags.getInt(this, 0);
	}

	@Override
	public INativeType getNativeType() {
		return META;
	}

	public int getNumContours() {
		return nContours.getShort(this, 0);
	}

	public int getNumPoints() {
		return nPoints.getShort(this, 0);
	}

	public NativeArray getPoints() {
		NativeArray array = (NativeArray) points.getValue(this);
		array.setSize(getNumPoints());
		return array;
	}

	public NativeArray getTags() {
		NativeArray array = (NativeArray) tags.getValue(this);
		array.setSize(getNumPoints());
		return array;
	}

}
