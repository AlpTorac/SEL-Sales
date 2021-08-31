package view.repository.uiwrapper;

import java.util.ArrayList;
import java.util.Collection;

import view.repository.IToggleGroup;
import view.repository.Toggleable;

public class ToggleGroupWrapper implements IToggleGroup {
	private Collection<Toggleable> toggleables;
	private IToggleGroup wrapee;
	
	public ToggleGroupWrapper(IToggleGroup wrapee) {
		this.wrapee = wrapee;
		this.toggleables = new ArrayList<Toggleable>();
	}
	
	@Override
	public IToggleGroup getToggleGroup() {
		return this.wrapee;
	}
	
	public void addToToggleGroup(Toggleable t) {
		this.toggleables.add(t);
		((IToggleGroup) this.getToggleGroup()).addToToggleGroup(t);
	}

	@Override
	public Toggleable[] getAllToggleables() {
		return this.toggleables.toArray(Toggleable[]::new);
	}
}
