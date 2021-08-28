package view.repository.uiwrapper;

import view.repository.ILayout;

public abstract class UILayout extends UIComponent implements ILayout {

	UILayout(ILayout component) {
		super(component);
	}
	
	public ILayout getComponent() {
		return (ILayout) super.getComponent();
	}
}
