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
 * The family a font belongs to. This is a collection of similar fonts that are
 * different with respect to their style.
 * 
 */
public interface IFontFamily {

	/**
	 * for example
	 * 
	 * @return for example
	 */
	public String getFamilyName();

	/**
	 * The {@link IFont} within the family with a specific {@link FontStyle} or
	 * null.
	 * 
	 * @param style
	 *            The requested style.
	 * @return The {@link IFont} within the family with a specific
	 *         {@link FontStyle} or null.
	 */
	public IFont getFont(FontStyle style);

	/**
	 * All {@link IFont} instances within this family.
	 * 
	 * @return All {@link IFont} instances within this family.
	 */
	public IFont[] getFonts();

	/**
	 * The supported {@link FontStyle} instances in this family.
	 * 
	 * @return The supported {@link FontStyle} instances in this family.
	 * 
	 */
	public FontStyle[] getFontStyles();

	/**
	 * The font type. This is for example "TrueType" or "Type1".
	 * 
	 * @return The font type.
	 */
	public String getFontType();

	/**
	 * Add a new {@link IFont} to this family.
	 * 
	 * @param font
	 *            The new {@link IFont}
	 */
	public void registerFont(IFont font);

}
