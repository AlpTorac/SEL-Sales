package view.repository;

public class ToggleGroupWrapper implements IToggleGroup {
	private IToggleGroup wrapee;
	
	ToggleGroupWrapper(IToggleGroup wrapee) {
		this.wrapee = wrapee;
	}
	
	@Override
	public IToggleGroup getToggleGroup() {
		return this.wrapee;
	}
}
