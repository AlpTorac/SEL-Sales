package client.view.composites;

import view.repository.uiwrapper.ClickEventListener;

public class OrderEntryDisplayListener extends ClickEventListener {
	private OrderEntryDisplay oed;
	
	public OrderEntryDisplayListener(OrderEntryDisplay oed) {
		this.oed = oed;
	}
	
	@Override
	public void clickAction(Object[] parameters) {
		if (parameters != null && parameters.length > 0 && parameters[0] != null) {
			OrderEntry orderEntry = (OrderEntry) parameters[0];
			this.oed.displayOrderEntry(orderEntry);
		}
	}
}
