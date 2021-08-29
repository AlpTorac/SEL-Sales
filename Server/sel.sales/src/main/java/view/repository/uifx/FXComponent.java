package view.repository.uifx;

import javafx.scene.Node;
import javafx.scene.Parent;
import view.repository.IUIComponent;

public interface FXComponent extends IUIComponent {
	@Override
	default FXComponent getComponent() {
		return this;
	}
	
	@Override
	default void setEnabled(boolean isEnabled) {
		((Node) this).setDisable(!isEnabled);
	}
	@Override
	default void show() {
		((Node) this).setVisible(true);
	}
	default Parent getParentProperty() {
		return ((Node) this).getParent();
	}
}
