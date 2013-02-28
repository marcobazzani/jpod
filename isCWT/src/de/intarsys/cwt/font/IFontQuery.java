package de.intarsys.cwt.font;

/**
 * An object that can select {@link IFont} instances within a collection.
 * 
 */
public interface IFontQuery {

	/**
	 * The desired font family for the {@link IFont}.
	 * 
	 * @return The desired font family for the {@link IFont}.
	 */
	public String getFontFamilyName();

	/**
	 * The desired font name for the {@link IFont}.
	 * 
	 * @return The desired font name for the {@link IFont}.
	 */
	public String getFontName();

	/**
	 * The desired font name for the {@link IFont}.
	 * 
	 * @return The desired font name for the {@link IFont}.
	 */
	public Object getFontNameCanonical();

	/**
	 * The desired font style for the {@link IFont}.
	 * 
	 * @return The desired font style for the {@link IFont}.
	 */
	public FontStyle getFontStyle();

	/**
	 * The desired font type for the {@link IFont} (such as "Type1" or
	 * "TrueType"). <code>null</code>indicates a font of any type.
	 * 
	 * @return The desired font type for the {@link IFont}.
	 */
	public String getFontType();

}
