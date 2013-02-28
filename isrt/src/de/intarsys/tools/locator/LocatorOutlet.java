package de.intarsys.tools.locator;

public class LocatorOutlet {

	private static ILocatorOutlet ACTIVE = new StandardLocatorOutlet();

	public static ILocatorOutlet get() {
		return ACTIVE;
	}

	public static void set(ILocatorOutlet active) {
		ACTIVE = active;
	}
}
