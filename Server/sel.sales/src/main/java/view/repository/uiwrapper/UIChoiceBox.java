package view.repository.uiwrapper;

import view.repository.IChoiceBox;

public class UIChoiceBox<T> extends UIComponent implements IChoiceBox<T> {
	public UIChoiceBox(IChoiceBox<T> component) {
		super(component);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IChoiceBox<T> getComponent() {
		return (IChoiceBox<T>) super.getComponent();
	}
}
