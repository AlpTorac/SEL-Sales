package view.repository;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class FXButton extends Button implements IButton, FXComponent {
	FXButton(double x, double y, double width, double height, String name, Pane parent) {
		super(name);
		this.attachTo((IUIComponent) parent);
		this.setLayoutX(x);
		this.setLayoutY(y);
		this.setPrefSize(width, height);
	}
}
