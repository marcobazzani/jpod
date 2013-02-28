package de.intarsys.tools.factory;

public interface IOutlet {

	public IFactory<?>[] getFactories();

	public <T> IFactory<T>[] lookupFactories(Class<T> type);

	public IFactory<?> lookupFactory(String id);

	public void registerFactory(IFactory<?> factory);

	public void unregisterFactory(IFactory<?> factory);

}
