package view.repository.uifx;

import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import view.repository.IToggleGroup;
import view.repository.Toggleable;

public interface FXToggleable extends FXAttachable, Toggleable {
	default public void setToggled(boolean isToggled) {
		((Toggle) this).setSelected(isToggled);
	}
	
	default public boolean isToggled() {
		return ((Toggle) this).isSelected();
	}
	
	default public void setToggleGroup(IToggleGroup tg) {
		((Toggle) this).setToggleGroup((ToggleGroup) tg.getToggleGroup());
	}
}
