package view.repository.uifx;

import javafx.scene.Node;
import javafx.scene.Parent;
import view.repository.IUIComponent;

public interface FXComponent extends IUIComponent {
	default public FXComponent getComponent() {
		return this;
	}
	
	@Override
	default public void show() {
		((Node) this).setVisible(true);
	}
	default public Parent getParentProperty() {
		return ((Node) this).getParent();
	}
}
