package view.repository;

public class UIHBoxLayout extends UILayout implements IHBoxLayout {
	protected UIHBoxLayout(IHBoxLayout component) {
		super(component);
	}
	
	public IHBoxLayout getComponent() {
		return (IHBoxLayout) super.getComponent();
	}
}
