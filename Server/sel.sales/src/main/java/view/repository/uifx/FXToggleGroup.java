package view.repository.uifx;

import javafx.scene.control.ToggleGroup;
import view.repository.IToggleGroup;
import view.repository.Toggleable;

public class FXToggleGroup extends ToggleGroup implements IToggleGroup {

	@Override
	public IToggleGroup getToggleGroup() {
		return this;
	}
	
	@Override
	public void addToToggleGroup(Toggleable t) {
		t.setToggleGroup(this);
	}
	
	@Override
	public void clearSelections() {
		super.getToggles().forEach(t -> t.setSelected(false));
	}
}
