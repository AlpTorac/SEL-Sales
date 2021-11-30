package view.repository.uiwrapper;

import view.repository.ISingleRowTextBox;

public class UISingleRowTextBox extends EventShooterUIComponent implements ISingleRowTextBox {
	public UISingleRowTextBox(ISingleRowTextBox component) {
		super(component);
	}
	
	public ISingleRowTextBox getComponent() {
		return (ISingleRowTextBox) super.getComponent();
	}
}
