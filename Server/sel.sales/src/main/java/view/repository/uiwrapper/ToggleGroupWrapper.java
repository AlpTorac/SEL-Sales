package view.repository.uiwrapper;

import view.repository.IToggleGroup;

public class ToggleGroupWrapper implements IToggleGroup {
	private IToggleGroup wrapee;
	
	public ToggleGroupWrapper(IToggleGroup wrapee) {
		this.wrapee = wrapee;
	}
	
	@Override
	public IToggleGroup getToggleGroup() {
		return this.wrapee;
	}
}
