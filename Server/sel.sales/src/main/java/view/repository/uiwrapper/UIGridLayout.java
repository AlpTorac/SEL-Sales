package view.repository.uiwrapper;

import view.repository.IGridLayout;

public class UIGridLayout extends UILayout implements IGridLayout {
	public UIGridLayout(IGridLayout component) {
		super(component);
	}
	
	public IGridLayout getComponent() {
		return (IGridLayout) super.getComponent();
	}
}
