package view.repository;

import java.util.Collection;

public interface IVBoxLayout extends ILayout {
	default public void setSpacing(double space) {
		((IVBoxLayout) this.getComponent()).setSpacing(space);
	}
	
	default public void addUIComponent(IUIComponent c) {
		((IVBoxLayout) this.getComponent()).addUIComponent(c);
	}
	
	default public void addUIComponents(IUIComponent[] cs) {
		for (IUIComponent c : cs) {
			this.addUIComponent(c);
		}
	}
}
