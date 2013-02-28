package de.intarsys.tools.locator;

import java.io.IOException;
import java.util.Map;

public class StandardLocatorOutlet extends CommonLocatorFactory implements
		ILocatorOutlet {

	private ILocatorFactory defaultLocatorFactory = new FileLocatorFactory();

	public StandardLocatorOutlet() {
		registerLocatorFactory("file", new FileLocatorFactory());
	}

	public final ILocator createLocator(String location) throws IOException {
		String[] parts = location.split("\\:\\:", 2);
		if (parts.length == 1) {
			return getDefaultLocatorFactory().createLocator(location);
		}
		ILocatorFactory factory = LocatorOutlet.get().lookupLocatorFactory(
				parts[0]);
		if (factory != null) {
			return factory.createLocator(parts[1]);
		}
		throw new IOException("locator format " + parts[0] + " not supported");
	}

	public ILocatorFactory getDefaultLocatorFactory() {
		return defaultLocatorFactory;
	}

	public Map<String, ILocatorFactory> getLocatorFactories() {
		return null;
	}

	public ILocatorFactory lookupLocatorFactory(String format) {
		return null;
	}

	public void registerLocatorFactory(String format, ILocatorFactory factory) {
	}

	public void setDefaultLocatorFactory(ILocatorFactory defaultLocatorFactory) {
		defaultLocatorFactory = defaultLocatorFactory;
	}

	public void unregisterLocatorFactory(String format) {
	}
}
