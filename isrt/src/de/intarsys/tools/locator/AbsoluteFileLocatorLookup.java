package de.intarsys.tools.locator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AbsoluteFileLocatorLookup extends AbstractLocatorLookup {

	public ILocator createLocator(String location) throws IOException {
		File file = new File(location);
		if (file.exists()) {
			return new FileLocator(file);
		}
		throw new FileNotFoundException("locator '" + location + "' not found");
	}
}
