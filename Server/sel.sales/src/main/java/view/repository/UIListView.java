package view.repository;

public class UIListView extends UIComponent implements IListView {
	UIListView(IListView component) {
		super(component);
	}
	
	public IListView getComponent() {
		return (IListView) super.getComponent();
	}
}
