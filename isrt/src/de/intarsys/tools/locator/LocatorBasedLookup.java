package de.intarsys.tools.locator;

import java.io.FileNotFoundException;
import java.io.IOException;

public class LocatorBasedLookup extends AbstractLocatorLookup {

	final private ILocator locator;

	public LocatorBasedLookup(ILocator locator) {
		super();
		this.locator = locator;
	}

	public ILocator createLocator(String location) throws IOException {
		ILocator lookup = getLocator().getChild(location);
		if (lookup.exists()) {
			return lookup;
		}
		throw new FileNotFoundException("locator '" + location + "' not found");
	}

	public ILocator getLocator() {
		return locator;
	}

}
