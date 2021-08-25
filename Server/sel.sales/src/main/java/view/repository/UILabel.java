package view.repository;

public class UILabel extends UIComponent implements ILabel {

	UILabel(ILabel component) {
		super(component);
	}
	
	public ILabel getComponent() {
		return (ILabel) super.getComponent();
	}
}
