package view.repository;

import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class FXHBox extends HBox implements IHBoxLayout, IFXLayout, FXComponent {
	@Override
	public void addUIComponent(IUIComponent c) {
		this.getChildren().add((Node) c.getComponent());
	}
}
