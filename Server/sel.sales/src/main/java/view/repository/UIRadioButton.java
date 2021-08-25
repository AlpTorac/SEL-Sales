package view.repository;

public class UIRadioButton extends UIComponent implements IRadioButton {

	UIRadioButton(IRadioButton component) {
		super(component);
	}

	public IRadioButton getComponent() {
		return (IRadioButton) super.getComponent();
	}
}
