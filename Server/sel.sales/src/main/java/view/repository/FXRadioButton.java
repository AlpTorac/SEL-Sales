package view.repository;

import javafx.scene.control.RadioButton;

public class FXRadioButton extends RadioButton implements FXToggleable, IRadioButton {
	@Override
	public void setCaption(String caption) {
		((RadioButton) this).setText(caption);
	}
}
