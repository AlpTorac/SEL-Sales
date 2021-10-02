package view.repository.uiwrapper;

import view.repository.ITabPane;

public class UITabPane extends UIComponent implements ITabPane {

	public UITabPane(ITabPane component) {
		super(component);
	}
	
	public ITabPane getComponent() {
		return (ITabPane) super.getComponent();
	}
}
