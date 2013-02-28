package de.intarsys.tools.locator;

import java.io.IOException;

public class DelegatingLocatorFactory extends CommonLocatorFactory {

	final private ILocatorFactory factory;

	public DelegatingLocatorFactory(ILocatorFactory factory) {
		super();
		this.factory = factory;
	}

	@Override
	public ILocator createLocator(String location) throws IOException {
		return factory.createLocator(location);
	}

}
