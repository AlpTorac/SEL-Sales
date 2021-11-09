package view.repository;

public interface IHBoxLayout extends ILayout {
	default public void setSpacing(double space) {
		((IHBoxLayout) this.getComponent()).setSpacing(space);
	}
	
	default public void addUIComponent(IUIComponent c) {
		((IHBoxLayout) this.getComponent()).addUIComponent(c);
	}
	
	default public void addUIComponents(IUIComponent[] cs) {
		for (IUIComponent c : cs) {
			this.addUIComponent(c);
		}
	}
}
