package view.repository;

import javafx.scene.control.ToggleGroup;

public class FXToggleGroup extends ToggleGroup implements IToggleGroup {

	@Override
	public IToggleGroup getToggleGroup() {
		return this;
	}
	
	@Override
	public void addToToggleGroup(Toggleable t) {
		t.setToggleGroup(this);
	}
}
