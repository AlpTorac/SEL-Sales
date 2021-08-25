package view.repository;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class FXVBox extends VBox implements IVBoxLayout, IFXLayout, FXComponent {
	@Override
	public void addUIComponent(IUIComponent c) {
		this.getChildren().add((Node) c.getComponent());
	}
}
