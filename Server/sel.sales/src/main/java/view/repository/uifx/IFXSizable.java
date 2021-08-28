package view.repository.uifx;

import javafx.scene.Node;
import javafx.scene.layout.Region;
import view.repository.ISizable;

public interface IFXSizable extends FXComponent, ISizable {
	@Override
	default public void setLayoutX(double x) {
		((Node) this).setLayoutX(x);
	}
	@Override
	default public void setLayoutY(double y) {
		((Node) this).setLayoutY(y);
	}
	@Override
	default public void setPrefWidth(double width) {
		((Region) this).setPrefWidth(width);
	}
	@Override
	default public void setPrefHeight(double height) {
		((Region) this).setPrefHeight(height);
	}
}
