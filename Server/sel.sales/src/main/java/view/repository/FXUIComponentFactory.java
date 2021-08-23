package view.repository;

import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public class FXUIComponentFactory implements UIComponentFactory {
	
	public FXStage createStage(double width, double height, String name) {
		return new FXStage(width, height, name);
	}

	public FXButton createButton(double x, double y, double width, double height, String name, IUIComponent parent) {
		return new FXButton(x, y, width, height, name, (Pane) parent);
	}

	public FXTextBox createTextBox(double x, double y, double width, double height, String name, IUIComponent parent) {
		return new FXTextBox(x, y, width, height, name, (Parent) parent);
	}

	public FXScene createScene(double width, double height, String name, IUIComponent parent) {
		return new FXScene(width, height, name, (Pane) parent);
	}

	public IPane createPane(double x, double y, double width, double height) {
		return new FXPane(x, y, width, height);
	}

}
