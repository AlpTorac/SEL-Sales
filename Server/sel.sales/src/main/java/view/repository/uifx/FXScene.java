package view.repository.uifx;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import view.repository.IInnerFrame;

public class FXScene extends Scene implements IInnerFrame, FXComponent {
	FXScene(Pane parent) {
		super(parent);
	}
}
