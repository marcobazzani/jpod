package de.intarsys.cwt.font;

public class FontQuery implements IFontQuery {

	/**
	 * The font type requested
	 */
	private String fontType = null;

	/**
	 * The font family requested
	 */
	private String fontFamilyName;

	/**
	 * The font name requested
	 */
	private String fontName;

	/**
	 * The font style required
	 */
	private FontStyle fontStyle;

	public FontQuery() {
		super();
	}

	public FontQuery(String fontName) {
		super();
		setFontName(fontName);
	}

	public FontQuery(String family, FontStyle style) {
		super();
		setFontFamilyName(family);
		setFontStyle(style);
	}

	public FontQuery(String family, String style) {
		super();
		setFontFamilyName(family);
		setFontStyle(FontStyle.getFontStyle(style));
	}

	public String getFontFamilyName() {
		if (fontFamilyName == null) {
			return FontTools.getFontFamilyName(fontName);
		} else {
			return fontFamilyName;
		}
	}

	public String getFontName() {
		return fontName;
	}

	public Object getFontNameCanonical() {
		String tempFamilyName = getFontFamilyName();
		FontStyle tempStyle = null;
		if (getFontStyle() == null) {
			tempStyle = FontTools.getFontStyle(fontName);
		} else {
			tempStyle = getFontStyle();
		}
		return FontTools.createCanonicalName(tempFamilyName, tempStyle.getId());
	}

	public FontStyle getFontStyle() {
		return fontStyle;
	}

	public String getFontType() {
		return fontType;
	}

	public void setFontFamilyName(String fontFamilyName) {
		this.fontFamilyName = fontFamilyName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public void setFontStyle(FontStyle fontStyle) {
		this.fontStyle = fontStyle;
	}

	public void setFontType(String fontType) {
		this.fontType = fontType;
	}
}
