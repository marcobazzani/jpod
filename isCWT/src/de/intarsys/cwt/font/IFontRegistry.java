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
package de.intarsys.cwt.font;

/**
 * A registry of {@link IFont} instances.
 * 
 */
public interface IFontRegistry {

	/**
	 * The {@link IFontFamily} instances formed by the {@link IFont} instances
	 * in this {@link IFontRegistry}.
	 * 
	 * @return The {@link IFontFamily} instances formed by the {@link IFont}
	 *         instances in this {@link IFontRegistry}.
	 */
	public IFontFamily[] getFontFamilies();

	/**
	 * The {@link IFont} instances in this {@link IFontRegistry}.
	 * 
	 * @return The {@link IFont} instances in this {@link IFontRegistry}.
	 * 
	 */
	public IFont[] getFonts();

	/**
	 * The {@link IFont} selected by the {@link IFontQuery}.
	 * 
	 * @param query
	 *            A {@link IFontQuery} describing a single {@link IFont}.
	 * 
	 * @return The {@link IFont} selected by the {@link IFontQuery}.
	 * 
	 */
	public IFont lookupFont(IFontQuery query);

	/**
	 * The {@link IFontFamily} selected by the {@link IFontQuery}.
	 * 
	 * @param query
	 *            A {@link IFontQuery} describing a single {@link IFontFamily}.
	 * 
	 * @return The {@link IFontFamily} selected by the {@link IFontQuery}.
	 * 
	 */
	public IFontFamily lookupFontFamily(IFontQuery query);

	/**
	 * Add a new {@link IFont} to the {@link IFontRegistry}.
	 * 
	 * @param font
	 *            The new {@link IFont}
	 */
	public void registerFont(IFont font);
}
