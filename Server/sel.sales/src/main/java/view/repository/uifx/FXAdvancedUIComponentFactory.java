package view.repository.uifx;

import view.repository.uiwrapper.AdvancedUIComponentFactory;
import view.repository.uiwrapper.UIConnectionDetailsTable;

public class FXAdvancedUIComponentFactory extends AdvancedUIComponentFactory {

	@Override
	public <T> UIConnectionDetailsTable<T> createConnectionDetailsTable(String isAllowedColumnTitle, String isAllowedColumnFieldName, String connStatusColumnTitle, String connStatusColumnFieldName) {
		FXConnectionDetailsTable<T> wrapee = new FXConnectionDetailsTable<T>(isAllowedColumnTitle, isAllowedColumnFieldName, connStatusColumnTitle, connStatusColumnFieldName);
		UIConnectionDetailsTable<T> cdt = new UIConnectionDetailsTable<T>(wrapee);
		return cdt;
	}

}
