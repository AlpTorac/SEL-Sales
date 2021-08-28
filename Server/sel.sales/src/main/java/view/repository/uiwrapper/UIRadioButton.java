package view.repository.uiwrapper;

import view.repository.IRadioButton;

public class UIRadioButton extends UIComponent implements IRadioButton {

	public UIRadioButton(IRadioButton component) {
		super(component);
	}

	public IRadioButton getComponent() {
		return (IRadioButton) super.getComponent();
	}
}
