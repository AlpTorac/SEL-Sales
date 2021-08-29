package view.repository.uiwrapper;

import view.repository.ITable;

public class UITable<T> extends UIComponent implements ITable<T> {

	public UITable(ITable<T> component) {
		super(component);
	}
	
	@SuppressWarnings("unchecked")
	public ITable<T> getComponent() {
		return (ITable<T>) super.getComponent();
	}
}
