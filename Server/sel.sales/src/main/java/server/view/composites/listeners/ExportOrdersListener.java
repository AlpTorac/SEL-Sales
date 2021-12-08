package server.view.composites.listeners;

import controller.IApplicationEvent;
import controller.IApplicationEventShooter;
import controller.IController;
import server.controller.IServerController;
import server.controller.ServerSpecificEvent;
import view.repository.uiwrapper.ClickEventListener;

public class ExportOrdersListener extends ClickEventListener implements IApplicationEventShooter {
	private IController controller;
	
	public ExportOrdersListener(IServerController controller) {
		this.controller = controller;
	}
	
	@Override
	public void clickAction() {
		this.fireApplicationEvent(controller);
	}
	
	@Override
	public Object[] getArgs() {
		return null;
	}

	@Override
	public IApplicationEvent getApplicationEvent() {
		return ServerSpecificEvent.EXPORT_ORDERS;
	}

}
