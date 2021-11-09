package server.view.composites.listeners;

import controller.IApplicationEvent;
import controller.IApplicationEventShooter;
import controller.IController;
import server.controller.ServerSpecificEvent;
import view.repository.uiwrapper.ClickEventListener;

public class WriteDishMenuListener extends ClickEventListener implements IApplicationEventShooter {
	private IController controller;
	
	public WriteDishMenuListener(IController controller) {
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
		return ServerSpecificEvent.WRITE_DISH_MENU;
	}

}
