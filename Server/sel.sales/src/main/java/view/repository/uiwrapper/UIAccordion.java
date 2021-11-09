package view.repository.uiwrapper;

import view.repository.IAccordion;

public class UIAccordion extends UIComponent implements IAccordion {

	public UIAccordion(IAccordion component) {
		super(component);
	}

	@Override
	public IAccordion getComponent() {
		return (IAccordion) super.getComponent();
	}
}
