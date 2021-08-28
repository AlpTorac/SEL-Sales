package view.repository.uiwrapper;

import view.repository.ITextBox;

public class UITextBox extends EventShooterUIComponent implements ITextBox {
	public UITextBox(ITextBox component) {
		super(component);
	}
	
	public ITextBox getComponent() {
		return (ITextBox) super.getComponent();
	}
}
