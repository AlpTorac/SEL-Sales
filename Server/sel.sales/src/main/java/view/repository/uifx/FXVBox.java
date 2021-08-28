package view.repository.uifx;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import view.repository.IUIComponent;
import view.repository.IVBoxLayout;

public class FXVBox extends VBox implements IVBoxLayout, IFXLayout, FXComponent {
	@Override
	public void addUIComponent(IUIComponent c) {
		this.getChildren().add((Node) c.getComponent());
	}
}
