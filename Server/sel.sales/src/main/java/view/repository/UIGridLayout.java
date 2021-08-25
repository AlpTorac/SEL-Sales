package view.repository;

public class UIGridLayout extends UILayout implements IGridLayout {
	protected UIGridLayout(IGridLayout component) {
		super(component);
	}
	
	public IGridLayout getComponent() {
		return (IGridLayout) super.getComponent();
	}
}
