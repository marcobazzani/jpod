package de.intarsys.tools.locator;

import java.util.Map;

public interface ILocatorOutlet extends ILocatorFactory {

	public Map<String, ILocatorFactory> getLocatorFactories();

	public ILocatorFactory lookupLocatorFactory(String format);

	public void registerLocatorFactory(String format, ILocatorFactory factory);

	public void unregisterLocatorFactory(String format);
}
