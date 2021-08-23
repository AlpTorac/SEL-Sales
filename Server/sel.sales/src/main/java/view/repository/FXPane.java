package view.repository;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public class FXPane extends Pane implements IPane, FXComponent {
	FXPane(double x, double y, double width, double height) {
		super();
		this.setLayoutX(x);
		this.setLayoutY(y);
		this.setPrefSize(width, height);
	}

	@Override
	public void addUIComponent(IUIComponent c) {
		this.getChildren().add((Region) c);
	}
}
