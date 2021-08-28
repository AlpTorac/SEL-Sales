package view.repository.uifx;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import view.repository.IHBoxLayout;
import view.repository.IUIComponent;

public class FXHBox extends HBox implements IHBoxLayout, IFXLayout, FXComponent {
	@Override
	public void addUIComponent(IUIComponent c) {
		this.getChildren().add((Node) c.getComponent());
	}
}
