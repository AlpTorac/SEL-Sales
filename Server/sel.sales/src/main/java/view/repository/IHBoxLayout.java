package view.repository;

import java.util.Collection;

public interface IHBoxLayout extends ILayout {
	default public void setSpacing(double space) {
		((IHBoxLayout) this.getComponent()).setSpacing(space);
	}
	
	default public void addUIComponent(IUIComponent c) {
		((IHBoxLayout) this.getComponent()).addUIComponent(c);
	}
	
	default public void addUIComponents(Collection<IUIComponent> cs) {
		for (IUIComponent c : cs) {
			this.addUIComponent(c);
		}
	}
	
	default public void addUIComponents(IUIComponent[] cs) {
		for (IUIComponent c : cs) {
			this.addUIComponent(c);
		}
	}
}
