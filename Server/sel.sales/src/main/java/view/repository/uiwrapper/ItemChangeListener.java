package view.repository.uiwrapper;

import java.util.Collection;

public abstract class ItemChangeListener extends EditPart {
	protected ItemChangeListener() {
		super();
	}
	
	public void itemRemovedAction(Collection<?> items) {
		
	}
	
	public void itemAddedAction(Collection<?> items) {
		
	}
	
	public void itemEditedAction(Collection<?> items) {
		
	}
	
	public void selectedItemChanged(Object item) {
		
	}
}
