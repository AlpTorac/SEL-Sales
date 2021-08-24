package view.repository;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXStage extends Stage implements IRootComponent, FXComponent {
	FXStage(double width, double height, String name) {
		super();
		this.setTitle(name);
		this.setWidth(width);
		this.setHeight(height);
	}
	
	public void setInnerFrame(IInnerFrame scene) {
		this.setScene((Scene) scene);
	}
}
