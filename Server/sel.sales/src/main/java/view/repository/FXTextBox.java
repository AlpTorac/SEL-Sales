package view.repository;

import javafx.scene.Parent;
import javafx.scene.control.TextField;

public class FXTextBox extends TextField implements ITextBox, FXComponent {
	FXTextBox(double x, double y, double width, double height, String name, Parent parent) {
		super(name);
		this.attachTo((IUIComponent) parent);
		this.setLayoutX(x);
		this.setLayoutY(y);
		this.setPrefSize(width, height);
	}
}
