package server.view.composites.listeners;

import model.order.OrderData;
import server.view.composites.OrderInspectionArea;
import view.repository.uiwrapper.ClickEventListener;

public class OrderInspectionListener extends ClickEventListener {
	private OrderInspectionArea oia;
	
	public OrderInspectionListener(OrderInspectionArea oia) {
		super();
		this.oia = oia;
	}
	
//	public void multiClickAction(int amountOfClicks, Object[] parameters) {
//		super.multiClickAction(amountOfClicks, parameters);
//		
//		if (amountOfClicks > 1 && parameters != null && parameters.length > 0 && parameters[0] != null) {
//			OrderData orderData = (OrderData) parameters[0];
//			this.oia.displayOrder(orderData);
//		}
//	}
	
	@Override
	public void clickAction(Object[] parameters) {
		if (parameters != null && parameters.length > 0 && parameters[0] != null) {
			OrderData orderData = (OrderData) parameters[0];
			this.oia.displayOrder(orderData);
		}
	}
}
