package view.repository;

public class UITable<T> extends UIComponent implements ITable<T> {

	UITable(ITable<T> component) {
		super(component);
	}

	@Override
	public void addClickListener(ClickEventListener l) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeClickListener(ClickEventListener l) {
		// TODO Auto-generated method stub

	}
	
	@SuppressWarnings("unchecked")
	public ITable<T> getComponent() {
		return (ITable<T>) super.getComponent();
	}
}
