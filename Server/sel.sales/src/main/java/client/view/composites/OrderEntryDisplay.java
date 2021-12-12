package client.view.composites;

import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIHBoxLayout;

public class OrderEntryDisplay extends UIHBoxLayout {
	
	private OrderEntry currentEntry;
	
	public OrderEntryDisplay(UIComponentFactory fac) {
		super(fac.createHBoxLayout().getComponent());
	}
	
	public void displayOrderEntry(OrderEntry orderEntry) {
		if (this.currentEntry != null) {
			this.currentEntry.dettach();
		}
		
		this.currentEntry = orderEntry;
		this.addUIComponent(this.currentEntry);
	}
}
