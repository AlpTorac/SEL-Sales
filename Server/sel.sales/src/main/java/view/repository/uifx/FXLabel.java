package view.repository.uifx;

import javafx.scene.control.Label;
import view.repository.ILabel;

public class FXLabel extends Label implements FXAttachable, ILabel {
	@Override
	public void setCaption(String caption) {
		((Label) this).setText(caption);
	}
}
