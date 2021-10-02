package view.repository;

public interface IAdvancedUIComponentFactory {
	<T> IConnectionDetailsTable<T> createConnectionDetailsTable(String isAllowedColumnTitle, String isAllowedColumnFieldName, String connStatusColumnTitle, String connStatusColumnFieldName);
}
