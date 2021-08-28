package view.repository.uiwrapper;

import view.repository.IInnerFrame;

public class UIInnerFrame extends UIComponent implements IInnerFrame {
	public UIInnerFrame(IInnerFrame component) {
		super(component);
	}
	
	public IInnerFrame getComponent() {
		return (IInnerFrame) super.getComponent();
	}
}
