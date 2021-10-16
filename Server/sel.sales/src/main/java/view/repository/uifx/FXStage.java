package view.repository.uifx;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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
	
	@Override
	public void terminateOnClose() {
		super.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);
		});
	}
	
	@Override
	public void closeArtificially() {
		super.fireEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSE_REQUEST));
	}
}
