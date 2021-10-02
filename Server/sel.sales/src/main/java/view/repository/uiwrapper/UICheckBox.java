package view.repository.uiwrapper;

import view.repository.ICheckBox;

public class UICheckBox extends UIComponent implements ICheckBox {
	public UICheckBox(ICheckBox component) {
		super(component);
	}

	public ICheckBox getComponent() {
		return (ICheckBox) super.getComponent();
	}
}
