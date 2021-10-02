package view.composites;

import controller.BusinessEvent;
import controller.IApplicationEvent;
import controller.IApplicationEventShooter;
import controller.IController;
import view.repository.uiwrapper.ClickEventListener;

public class RediscoverClientsListener extends ClickEventListener implements IApplicationEventShooter {

	private IController controller;
	
	RediscoverClientsListener(IController controller) {
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
		return BusinessEvent.DISCOVER_CLIENTS;
	}

}
