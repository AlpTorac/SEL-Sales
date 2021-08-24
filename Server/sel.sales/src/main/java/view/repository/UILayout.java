package view.repository;

public class UILayout extends UIComponent implements ILayout {

	UILayout(ILayout component) {
		super(component);
	}

	@Override
	public void addUIComponent(IUIComponent c) {
		((ILayout) this.getComponent()).addUIComponent(c);
	}
}
