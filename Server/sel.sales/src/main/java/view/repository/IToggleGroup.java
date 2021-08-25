package view.repository;

import java.util.Collection;

public interface IToggleGroup {
	default public void addToToggleGroup(Toggleable t) {
		((IToggleGroup) this.getToggleGroup()).addToToggleGroup(t);
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
