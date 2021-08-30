package view.composites;

import model.order.IOrderData;
import view.repository.uiwrapper.ClickEventListener;

public class OrderInspectionListener extends ClickEventListener {
	private OrderInspectionArea oia;
	
	public OrderInspectionListener(OrderInspectionArea oia) {
		super();
		this.oia = oia;
	}
	
	protected void multiClickAction(int amountOfClicks, Object[] parameters) {
		super.multiClickAction(amountOfClicks, parameters);
		
		if (amountOfClicks > 1 && parameters != null && parameters[0] != null) {
			this.oia.displayOrder((IOrderData) parameters[0]);
		}
	}
}
