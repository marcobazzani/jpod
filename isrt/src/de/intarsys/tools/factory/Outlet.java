package de.intarsys.tools.factory;

public class Outlet {

	private static IOutlet ACTIVE = new StandardOutlet();

	static public IOutlet get() {
		return ACTIVE;
	}

	static public void set(IOutlet active) {
		ACTIVE = active;
	}
}
