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
 * An abstract font description object.
 * 
 */
public interface IFont {
	/**
	 * The font family name
	 * 
	 * @return The font family name
	 */
	public String getFontFamilyName();

	/**
	 * The font name. This may deviate from the postscript font name for
	 * TrueType fonts.
	 * 
	 * @return The font name.
	 */
	public String getFontName();

	/**
	 * The canonical font name.
	 * 
	 * @return The canonical font name.
	 */
	public String getFontNameCanonical();

	/**
	 * The postscript font name.
	 * 
	 * @return The postscript font name.
	 */
	public String getFontNamePostScript();

	/**
	 * The referenced {@link IFontProgram}.
	 * 
	 * @return The referenced {@link IFontProgram}.
	 */
	public IFontProgram getFontProgram();

	/**
	 * The font style.
	 * 
	 * @return The font style.
	 */
	public FontStyle getFontStyle();

	/**
	 * The font type. This is for example "TrueType" or "Type1".
	 * 
	 * @return The font type.
	 */
	public String getFontType();
}
