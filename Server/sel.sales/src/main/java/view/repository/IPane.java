package view.repository;

import java.util.Collection;

public interface IPane extends IUIComponent {
	public void addUIComponent(IUIComponent c);
	default public void addUIComponents(Collection<IUIComponent> cs) {
		for (IUIComponent c : cs) {
			this.addUIComponent(c);
		}
	}
}
