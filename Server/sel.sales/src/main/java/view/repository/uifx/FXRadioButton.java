package view.repository.uifx;

import javafx.scene.control.RadioButton;
import view.repository.IRadioButton;

public class FXRadioButton extends RadioButton implements FXToggleable, IRadioButton {
	@Override
	public void setCaption(String caption) {
		((RadioButton) this).setText(caption);
	}
}
