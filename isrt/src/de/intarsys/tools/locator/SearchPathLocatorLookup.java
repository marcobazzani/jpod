package de.intarsys.tools.locator;

import java.io.File;
import java.util.Enumeration;
import java.util.StringTokenizer;

public class SearchPathLocatorLookup extends DelegatingLocatorLookup {

	public static final String PATH_SEPARATOR = ";"; //$NON-NLS-1$

	protected void addSearchPath(File parent, String path) {
		File child = new File(path);
		ILocator locator;
		if (child.isAbsolute() || parent == null) {
			locator = new FileLocator(child);
			((FileLocator) locator).setSynchSynchronous(true);
		} else {
			FileLocator parentLocator = new FileLocator(parent);
			parentLocator.setSynchSynchronous(true);
			locator = parentLocator.getChild(path);
		}
		addLocatorFactory(new LocatorBasedLookup(locator));
	}

	public void setSearchPath(File parent, String paths) {
		clear();
		// set up search paths
		for (Enumeration e = new StringTokenizer(paths, PATH_SEPARATOR); e
				.hasMoreElements();) {
			String path = (String) e.nextElement();
			if ((path != null) && (path.trim().length() > 0)) {
				addSearchPath(parent, path);
			}
		}
	}
}
