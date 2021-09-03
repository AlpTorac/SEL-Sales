package view.composites;

import model.order.IOrderData;
import view.repository.uiwrapper.ClickEventListener;

public class OrderInspectionListener extends ClickEventListener {
	private OrderInspectionArea oia;
	private OrderTrackingArea ota;
	
	public OrderInspectionListener(OrderTrackingArea ota, OrderInspectionArea oia) {
		super();
		this.oia = oia;
		this.ota = ota;
	}
	
	public void multiClickAction(int amountOfClicks, Object[] parameters) {
		super.multiClickAction(amountOfClicks, parameters);
		
		if (amountOfClicks > 1 && parameters != null && parameters.length > 0 && parameters[0] != null) {
			IOrderData orderData = (IOrderData) parameters[0];
			this.oia.displayOrder(orderData);
//			if (!this.ota.getPastOrderList().contains(orderData)) {
//				this.oia.getAddConfirmButton().setEnabled(true);
//			} else {
//				this.oia.getAddConfirmButton().setEnabled(false);
//			}
//			this.oia.getEditButton().setEnabled(true);
//			this.oia.getRemoveButton().setEnabled(true);
		}
	}
}
