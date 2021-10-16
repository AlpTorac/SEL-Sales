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
		super.setWidth(width);
	}
	
	@Override
	public void setPrefHeight(double height) {
		super.setHeight(height);
	}
	@Override
	public void maximise() {
		super.setMaximized(true);
	}
	@Override
	public void setFullScreen() {
		super.setFullScreen(true);
	}
	
	@Override
	public void close() {
		super.close();
	}
}
