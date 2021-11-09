package server.view.composites.listeners;

import controller.GeneralEvent;
import controller.IApplicationEvent;
import controller.IApplicationEventShooter;
import controller.IController;
import view.repository.uiwrapper.ClickEventListener;

public class RediscoverDevicesListener extends ClickEventListener implements IApplicationEventShooter {

	private IController controller;
	
	public RediscoverDevicesListener(IController controller) {
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
		return GeneralEvent.DISCOVER_DEVICES;
	}

}
