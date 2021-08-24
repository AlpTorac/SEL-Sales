package view.repository;

public class UIRootComponent extends UIComponent implements IRootComponent {
	UIRootComponent(IRootComponent component) {
		super(component);
	}

	@Override
	public void setInnerFrame(IInnerFrame scene) {
		((IRootComponent) this.getComponent()).setInnerFrame(scene);
	}
	
	public IRootComponent getComponent() {
		return (IRootComponent) super.getComponent();
	}
}
