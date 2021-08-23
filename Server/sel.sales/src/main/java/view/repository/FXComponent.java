package view.repository;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public interface FXComponent extends IUIComponent {
	default public void attachTo(IUIComponent parent) {
		((Pane) parent).getChildren().add((Node) this);
	}

	default public void dettach() {
		if (this.getParentProperty() != null) {
			((Pane) this.getParentProperty()).getChildren().remove((Node) this);
		}
	}
	
	@Override
	default public void show() {
		((Node) this).setVisible(true);
	}
	
	default public Parent getParentProperty() {
		return ((Node) this).getParent();
	}
}
