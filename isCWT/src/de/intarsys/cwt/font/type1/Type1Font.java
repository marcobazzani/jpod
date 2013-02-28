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
package de.intarsys.cwt.font.type1;

import java.io.IOException;

import de.intarsys.cwt.font.FontStyle;
import de.intarsys.cwt.font.FontTools;
import de.intarsys.cwt.font.afm.AFM;
import de.intarsys.tools.locator.ILocator;

/**
 * This is the skeleton for a type 1 font implementation. Up to now, we only
 * need the font metrics (.afm) part.
 */
public class Type1Font {

	public static Type1Font createFromLocator(ILocator locator)
			throws IOException {
		Type1Font result = new Type1Font();
		result.setLocator(locator);
		result.initializeFromLocator();
		return result;
	}

	private AFM afm;

	private ILocator locator;

	public Type1Font() {
	}

	public Type1Font(AFM afm) {
		this.afm = afm;
	}

	public String getFontFamilyName() {
		return FontTools.getFontFamilyName(getFontName());
	}

	synchronized public AFM getFontMetrics() {
		return afm;
	}

	public String getFontName() {
		return afm.getFontName();
	}

	public FontStyle getFontStyle() {
		return FontTools.getFontStyle(getFontName());
	}

	public ILocator getLocator() {
		return locator;
	}

	protected void initializeAfm() throws IOException {
		ILocator parentLocator = getLocator().getParent();
		if (parentLocator == null) {
			// can't lookup font metrics...
			return;
		}
		String localName = getLocator().getLocalName();
		ILocator afmLocator = parentLocator.getChild(localName + ".afm");
		afm = AFM.createFromLocator(afmLocator);
	}

	protected void initializeFromLocator() throws IOException {
		initializePfb();
		initializeAfm();
	}

	protected void initializePfb() {
		// currently we do not parse any information from the pfb..
	}

	public boolean isSymbolFont() {
		return false;
	}

	protected void setLocator(ILocator locator) {
		this.locator = locator;
	}

}
