package de.intarsys.tools.functor;

/**
 * A tool class for handling "result" objects.
 * 
 */
public class ResultTools {

	public static final String ARG_RESULT = "result";

	public static final String ARG_RESULT_RETURN = "result.return";

	public static final String ARG_RESULT_PROPERTY = "result.property";

	public static final String PROP_STYLE = "style";

	public static final String PROP_RETURN = "return";

	public static final String PROP_DISPLAY = "display";

	public static IArgs getPropertyDescriptor(IArgs args, String name) {
		IArgs property = ArgTools.getArgs(args, ARG_RESULT_PROPERTY + "."
				+ name, Args.create());
		return property;
	}

	public static EnumStyle getPropertyStyle(IArgs propertyDescriptor,
			EnumStyle defaultValue) {
		return ArgTools.getEnumItem(propertyDescriptor, EnumStyle.META,
				PROP_STYLE, defaultValue);
	}

	public static EnumStyle getPropertyStyle(IArgs args, String name,
			EnumStyle defaultValue) {
		return ArgTools.getEnumItem(args, EnumStyle.META, ARG_RESULT_PROPERTY
				+ "." + name + ".style", defaultValue);
	}

	public static IArgs getResultDescriptor(IArgs args) {
		return ArgTools.getArgs(args, ARG_RESULT, Args.create());
	}

	public static EnumReturnMode getResultReturnMode(IArgs args) {
		return (EnumReturnMode) ArgTools.getEnumItem(args, EnumReturnMode.META,
				ARG_RESULT_RETURN);
	}

	public static boolean isPropertyDisplay(IArgs propertyDescriptor,
			boolean defaultValue) {
		return ArgTools.getBoolean(propertyDescriptor, PROP_DISPLAY,
				defaultValue);
	}

	public static boolean isPropertyDisplay(IArgs args, String name,
			boolean defaultValue) {
		return ArgTools.getBoolean(args, ARG_RESULT_PROPERTY + "." + name
				+ ".display", defaultValue);
	}

	public static boolean isPropertyReturn(IArgs propertyDescriptor,
			boolean defaultValue) {
		return ArgTools.getBoolean(propertyDescriptor, PROP_RETURN,
				defaultValue);
	}

	public static boolean isPropertyReturn(IArgs args, String name,
			boolean defaultValue) {
		return ArgTools.getBoolean(args, ARG_RESULT_PROPERTY + "." + name
				+ ".return", defaultValue);
	}

}
