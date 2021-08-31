package view.repository;

import java.util.Collection;

public interface IToggleGroup {
	public void addToToggleGroup(Toggleable t);
	
	default public Toggleable[] getAllToggleables() {
		return null;
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
	
	public IToggleGroup getToggleGroup();
}
