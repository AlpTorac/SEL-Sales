package server.view.composites.listeners;

import controller.IApplicationEvent;
import controller.IApplicationEventShooter;
import controller.IController;
import server.controller.ServerSpecificEvent;
import server.view.composites.MenuDesignArea;
import view.repository.HasText;
import view.repository.uiwrapper.ClickEventListener;

public class RemoveDishListener extends ClickEventListener implements IApplicationEventShooter {
	private IController controller;
	private HasText dishID;
	
	public RemoveDishListener(IController controller, MenuDesignArea mda) {
		this.controller = controller;
		this.dishID = mda.getMenuItemIDBox();
	}
	
	@Override
	public Object[] getArgs() {
		String id = this.getDishID().getText();
		this.resetUserInput();
		return new Object[] {id};
	}

	@Override
	public IApplicationEvent getApplicationEvent() {
		return ServerSpecificEvent.REMOVE_DISH;
	}

	@Override
	public void clickAction() {
		this.fireApplicationEvent(controller);
	}

	private HasText getDishID() {
		return this.dishID;
	}
	
	private void resetUserInput() {
		this.getDishID().clearText();
	}
	
}
