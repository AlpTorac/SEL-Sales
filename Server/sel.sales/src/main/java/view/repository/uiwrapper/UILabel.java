package view.repository.uiwrapper;

import view.repository.ILabel;

public class UILabel extends UIComponent implements ILabel {

	public UILabel(ILabel component) {
		super(component);
	}
	
	public ILabel getComponent() {
		return (ILabel) super.getComponent();
	}
}
