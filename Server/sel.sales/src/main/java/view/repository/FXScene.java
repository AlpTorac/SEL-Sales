package view.repository;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class FXScene extends Scene implements IInnerFrame, FXComponent {
	FXScene(double width, double height, String name, Pane parent) {
		super(parent, width, height);
	}
}
