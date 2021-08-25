package view.repository;

public class UIVBoxLayout extends UILayout implements IVBoxLayout {
	protected UIVBoxLayout(IVBoxLayout component) {
		super(component);
	}
	
	public IVBoxLayout getComponent() {
		return (IVBoxLayout) super.getComponent();
	}
}
