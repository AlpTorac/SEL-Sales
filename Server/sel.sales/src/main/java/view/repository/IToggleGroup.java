package view.repository;

import java.util.Collection;

import view.repository.uiwrapper.ChangeEventListener;

public interface IToggleGroup {
	public void addToToggleGroup(Toggleable t);
	
	default public Toggleable[] getAllToggleables() {
		return ((IToggleGroup) this.getToggleGroup()).getAllToggleables();
	}
	
	default public void clearSelections() {
		((IToggleGroup) this.getToggleGroup()).clearSelections();
	}
	
	default public void addAllToToggleGroup(Toggleable[] ts) {
		for (Toggleable t : ts) {
			this.addToToggleGroup(t);
		}
	}
	
	default public void addAllToToggleGroup(Collection<Toggleable> ts) {
		for (Toggleable t : ts) {
			this.addToToggleGroup(t);
		}
	}
	
	IToggleGroup getToggleGroup();
	
	default public void addChangeEventListener(ChangeEventListener cel) {
		((IToggleGroup) this.getToggleGroup()).addChangeEventListener(cel);
	}
}
