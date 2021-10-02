package view.repository.uiwrapper;

import view.repository.IAdvancedUIComponentFactory;

public abstract class AdvancedUIComponentFactory implements IAdvancedUIComponentFactory {
	public abstract <T> UIConnectionDetailsTable<T> createConnectionDetailsTable(String isAllowedColumnTitle, String isAllowedColumnFieldName, String connStatusColumnTitle, String connStatusColumnFieldName);
}
