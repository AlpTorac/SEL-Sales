package view.repository.uifx;

import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import view.repository.IToggleGroup;
import view.repository.Toggleable;

public interface FXToggleable extends FXAttachable, Toggleable {
	default public void setSelected(boolean selected) {
		((Toggle) this).setSelected(selected);
	}
	
	default public void setToggleGroup(IToggleGroup tg) {
		((Toggle) this).setToggleGroup((ToggleGroup) tg.getToggleGroup());
	}
}
