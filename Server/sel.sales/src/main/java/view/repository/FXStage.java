package view.repository;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXStage extends Stage implements IStage, FXComponent {
	FXStage(double width, double height, String name) {
		super();
		this.setTitle(name);
		this.setWidth(width);
		this.setHeight(height);
	}
	
	public void setScene(IScene scene) {
		this.setScene((Scene) scene);
	}
}
