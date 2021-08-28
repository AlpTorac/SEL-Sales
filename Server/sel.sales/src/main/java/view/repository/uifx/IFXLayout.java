package view.repository.uifx;

import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import view.repository.ILayout;
import view.repository.ISizable;

public interface IFXLayout extends ILayout, ISizable {
	@Override
	default public void setMarigins(double top, double right, double bottom, double left) {
		((Pane) this).setPadding(new Insets(top, right, bottom, left));
	}
}
