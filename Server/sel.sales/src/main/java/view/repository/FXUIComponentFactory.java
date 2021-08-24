package view.repository;

import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public class FXUIComponentFactory extends UIComponentFactory {
	
	public UIRootComponent createRootComponent(double width, double height, String name) {
		FXStage wrapee = new FXStage(width, height, name);
		UIRootComponent rootComponent = new UIRootComponent(wrapee);
		return rootComponent;
	}

	public UIButton createButton(double x, double y, double width, double height, String name, IUIComponent parent) {
		FXButton wrapee = new FXButton(x, y, width, height, name, (Pane) parent.getComponent());
		UIButton button = new UIButton(wrapee);
		return button;
	}

	public UITextBox createTextBox(double x, double y, double width, double height, String name, IUIComponent parent) {
		FXTextBox wrapee = new FXTextBox(x, y, width, height, name, (Parent) parent.getComponent());
		UITextBox textBox = new UITextBox(wrapee);
		return textBox;
	}

	public UIInnerFrame createInnerFrame(double width, double height, String name, IUIComponent parent) {
		FXScene wrapee = new FXScene(width, height, name, (Pane) parent.getComponent());
		UIInnerFrame innerFrame = new UIInnerFrame(wrapee);
		return innerFrame;
	}

	public UILayout createLayout(double x, double y, double width, double height) {
		FXPane wrapee = new FXPane(x, y, width, height);
		UILayout layout = new UILayout(wrapee);
		return layout;
	}

}
