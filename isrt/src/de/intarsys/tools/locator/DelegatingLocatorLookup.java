package de.intarsys.tools.locator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.intarsys.tools.exception.ExceptionTools;

public class DelegatingLocatorLookup extends AbstractLocatorLookup {

	private List<ILocatorFactory> factories = new ArrayList<ILocatorFactory>();

	public boolean addLocatorFactory(ILocatorFactory factory) {
		if (factory == this) {
			// common mistake
			throw new IllegalArgumentException("can not delegate to myself");
		}
		return factories.add(factory);
	}

	public ILocator createLocator(String location) throws IOException {
		for (Iterator it = factories.iterator(); it.hasNext();) {
			ILocatorFactory factory = (ILocatorFactory) it.next();
			try {
				return factory.createLocator(location);
			} catch (FileNotFoundException e) {
				// search on
			} catch (IOException e) {
				// we tried to load but failed
				throw e;
			} catch (Exception e) {
				// we tried to load but failed for unknown reason
				throw ExceptionTools.createIOException("", e);
			}
		}
		// we tried all but failed
		throw new FileNotFoundException(location);
	}

	public void clear() {
		factories.clear();
	}
	
	public List<ILocatorFactory> getLocatorFactories() {
		return new ArrayList(factories);
	}

	public boolean removeLocatorFactory(ILocatorFactory factory) {
		return factories.remove(factory);
	}
}
