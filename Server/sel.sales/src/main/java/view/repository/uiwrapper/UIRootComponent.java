package view.repository.uiwrapper;

import view.repository.IRootComponent;

public class UIRootComponent extends UIComponent implements IRootComponent {
	public UIRootComponent(IRootComponent component) {
		super(component);
	}
	
	public IRootComponent getComponent() {
		return (IRootComponent) super.getComponent();
	}
}
