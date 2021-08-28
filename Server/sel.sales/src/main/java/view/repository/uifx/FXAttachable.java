package view.repository.uifx;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import view.repository.Attachable;
import view.repository.ILayout;
import view.repository.IUIComponent;

public interface FXAttachable extends FXComponent, Attachable {
	default public void attachTo(IUIComponent parent) {
		((Pane) parent).getChildren().add((Node) this);
	}
	
	default public void addToLayout(ILayout parent) {
		this.attachTo(parent);
	}
	
	default public void dettach() {
		if (this.getParentProperty() != null) {
			((Pane) this.getParentProperty()).getChildren().remove((Node) this);
		}
	}
}
