package view.repository;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
//import javafx.stage.Stage;

public class FXScene extends Scene implements IInnerFrame, FXComponent {
	FXScene(double width, double height, String name, Pane parent) {
		super(parent, width, height);
	}
	
	@Override
	public void attachTo(IUIComponent parent) {

	}
}
