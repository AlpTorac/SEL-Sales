package view.repository.uiwrapper;

import view.repository.IListView;

public class UIListView<T> extends UIComponent implements IListView<T> {
	public UIListView(IListView<T> component) {
		super(component);
	}
	
	@SuppressWarnings("unchecked")
	public IListView<T> getComponent() {
		return (IListView<T>) super.getComponent();
	}
}
