package view.repository;

import javafx.scene.control.Label;

public class FXLabel extends Label implements FXAttachable, ILabel {
	@Override
	public void setCaption(String caption) {
		((Label) this).setText(caption);
	}
}
