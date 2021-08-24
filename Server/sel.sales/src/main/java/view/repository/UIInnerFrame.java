package view.repository;

public class UIInnerFrame extends UIComponent implements IInnerFrame {
	UIInnerFrame(IInnerFrame component) {
		super(component);
	}
	
	public IInnerFrame getComponent() {
		return (IInnerFrame) super.getComponent();
	}
}
