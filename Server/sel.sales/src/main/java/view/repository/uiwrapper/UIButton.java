package view.repository.uiwrapper;

import view.repository.IButton;

public class UIButton extends EventShooterUIComponent implements IButton {
	public UIButton(IButton component) {
		super(component);
	}
	
	public IButton getComponent() {
		return (IButton) super.getComponent();
	}
}
