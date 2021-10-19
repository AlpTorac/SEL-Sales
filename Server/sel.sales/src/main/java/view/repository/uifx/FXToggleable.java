package view.repository.uifx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import view.repository.IToggleGroup;
import view.repository.Toggleable;
import view.repository.uiwrapper.ChangeEventListener;

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
	
	@Override
	default public void addChangeEventListener(ChangeEventListener cel) {
		((Toggle) this).selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				cel.changeAction(oldValue, newValue);
			}
		});
	}
}
