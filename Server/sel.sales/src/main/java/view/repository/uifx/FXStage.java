package view.repository.uifx;

import javafx.scene.Scene;
import javafx.stage.Stage;
import view.repository.IInnerFrame;
import view.repository.IRootComponent;

public class FXStage extends Stage implements IRootComponent, IFXSizable {
	public void setInnerFrame(IInnerFrame scene) {
		this.setScene((Scene) scene.getComponent());
	}
	
	@Override
	public void setPrefWidth(double width) {
		((Stage) this).setWidth(width);
	}
	
	@Override
	public void setPrefHeight(double height) {
		((Stage) this).setHeight(height);
	}
}
