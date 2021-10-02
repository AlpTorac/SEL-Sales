package view.repository.uiwrapper;

import view.repository.IConnectionDetailsTable;

public class UIConnectionDetailsTable<T> extends UITable<T> implements IConnectionDetailsTable<T> {
	public UIConnectionDetailsTable(IConnectionDetailsTable<T> component) {
		super(component);
	}
	
	public IConnectionDetailsTable<T> getComponent() {
		return (IConnectionDetailsTable<T>) super.getComponent();
	}
}
