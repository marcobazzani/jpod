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
package de.intarsys.cwt.font.truetype;

/**
 * 
 */
public interface ITTNamingIDs {
	// Code Meaning
	// 0 Copyright notice.
	public static final int Copyright = 0;

	// 1 Font Family name. Up to four fonts can share the Font Family name,
	// forming a font style linking group (regular, italic, bold, bold italic -
	// as defined by OS/2.fsSelection bit settings).
	public static final int FontFamilyName = 1;

	// 2 Font Subfamily name. The Font Subfamily name distiguishes the font in a
	// group with the same Font Family name (name ID 1). This is assumed to
	// address style (italic, oblique) and weight (light, bold, black, etc.). A
	// font with no particular differences in weight or style (e.g. medium
	// weight, not italic and fsSelection bit 6 set) should have the string
	// "Regular" stored in this position.
	public static final int FontSubfamilyName = 2;

	// 3 Unique font identifier
	public static final int FontId = 3;

	// 4 Full font name; this should be a combination of strings 1 and 2.
	// Exception: if the font is "Regular" as indicated in string 2, then use
	// only the family name contained in string 1.
	public static final int FullFontName = 4;

	// 5 Version string. Should begin with the syntax 'Version
	// <number>.<number>' (upper case, lower case, or mixed, with a space
	// between "Version" and the number).
	public static final int Version = 5;

	// 6 Postscript name for the font; Name ID 6 specifies a string which is
	// used to invoke a PostScript language font that corresponds to this
	// OpenType font. If no name ID 6 is present, then there is no defined
	// method for invoking this font on a PostScript interpreter.
	public static final int PSName = 6;

	// 7 Trademark; this is used to save any trademark notice/information for
	// this font. Such information should be based on legal advice. This is
	// distinctly separate from the copyright.
	public static final int Trademark = 7;

	// 8 Manufacturer Name.
	public static final int ManufacturerName = 8;

	// 9 Designer; name of the designer of the typeface.
	public static final int DesignerName = 9;

	// 10 Description; description of the typeface. Can contain revision
	// information, usage recommendations, history, features, etc.
	public static final int Description = 10;

	// 11 URL Vendor; URL of font vendor (with protocol, e.g., http://, ftp://).
	// If a unique serial number is embedded in the URL, it can be used to
	// register the font.
	public static final int URLVendor = 11;

	// 12 URL Designer; URL of typeface designer (with protocol, e.g., http://,
	// ftp://).
	public static final int URLDesigner = 12;

	// 13 License Description; description of how the font may be legally used,
	// or different example scenarios for licensed use. This field should be
	// written in plain language, not legalese.
	public static final int LicenseDescription = 13;

	// 14 License Info URL; URL where additional licensing information can be
	// found.
	public static final int LicenseInfoURL = 14;

	// 15 Reserved; Set to zero.

	// 16 Preferred Family; For historical reasons, font families have contained
	// a maximum of four styles, but font designers may group more than four
	// fonts to a single family. The Preferred Family allows font designers to
	// include the preferred family grouping which contains more than four
	// fonts. This ID is only present if it is different from ID 1.
	public static final int PreferredFamily = 16;

	// 17 Preferred Subfamily; Allows font designers to include the preferred
	// subfamily grouping that is more descriptive than ID 2. This ID is only
	// present if it is different from ID 2 and must be unique for the the
	// Preferred Family.
	public static final int PreferredSubFamily = 17;

	// 18 Compatible Full (Macintosh only); On the Macintosh, the menu name is
	// constructed using the FOND resource. This usually matches the Full Name.
	// If you want the name of the font to appear differently than the Full
	// Name, you can insert the Compatible Full Name in ID 18.
	public static final int CompatibleFull = 18;

	// 19 Sample text; This can be the font name, or any other text that the
	// designer thinks is the best sample to display the font in.
	public static final int SampleText = 19;

	// 20 PostScript CID findfont name; Its presence in a font means that the
	// nameID 6 holds a PostScript font name that is meant to be used with the
	// "composefont" invocation in order to invoke the font in a PostScript
	// interpreter. See the definition of name ID 6.
	public static final int PS_CID = 20;
}
