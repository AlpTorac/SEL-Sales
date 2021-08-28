package view.repository.uiwrapper;

import view.repository.IInnerFrame;
import view.repository.IRootComponent;

public class UIRootComponent extends UIComponent implements IRootComponent {
	public UIRootComponent(IRootComponent component) {
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
