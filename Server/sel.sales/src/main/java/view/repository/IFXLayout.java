package view.repository;

import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public interface IFXLayout extends ILayout, ISizable {
	@Override
	default public void setMarigins(double top, double right, double bottom, double left) {
		((Pane) this).setPadding(new Insets(top, right, bottom, left));
	}
}
