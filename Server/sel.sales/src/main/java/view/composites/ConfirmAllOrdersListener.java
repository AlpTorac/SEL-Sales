package view.composites;

import model.order.IOrderData;
import view.repository.uiwrapper.ClickEventListener;

public class ConfirmAllOrdersListener extends ClickEventListener {
	private OrderInspectionArea oia;
	private OrderTrackingArea ota;
	
	public ConfirmAllOrdersListener(OrderInspectionArea oia, OrderTrackingArea ota) {
		super();
		this.oia = oia;
		this.ota = ota;
	}
	
	public void clickAction() {
		while (this.ota.getUnconfirmedOrderList().getSize() > 0) {
			IOrderData data = this.ota.getUnconfirmedOrderList().getItem(0);
			this.oia.displayOrder(data);
			this.oia.getAddConfirmButton().performArtificialClick();
		}
	}
}
