package view.composites;

import java.util.Collection;

import view.repository.uiwrapper.ItemChangeListener;

public class UnconfirmedOrderListener extends ItemChangeListener {
	private OrderTrackingArea ota;
	private OrderInspectionArea oia;
	
	public UnconfirmedOrderListener(OrderTrackingArea ota, OrderInspectionArea oia) {
		super();
		this.ota = ota;
		this.oia = oia;
	}
	
	@Override
	public void itemRemovedAction(Collection<?> items) {
		
	}
	
	@Override
	public void itemAddedAction(Collection<?> items) {
		if (this.ota.getAuto().isToggled()) {
			this.oia.getConfirmAllButton().performArtificialClick();
		}
	}
	
	@Override
	public void itemEditedAction(Collection<?> items) {
		
	}
}
