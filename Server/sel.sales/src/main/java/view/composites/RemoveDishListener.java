package view.composites;

import controller.BusinessEvent;
import controller.IBusinessEventShooter;
import controller.IController;
import view.repository.HasText;
import view.repository.uiwrapper.ClickEventListener;

public class RemoveDishListener extends ClickEventListener implements IBusinessEventShooter {
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
	public BusinessEvent getBusinessEvent() {
		return BusinessEvent.REMOVE_DISH;
	}

	@Override
	public void clickAction() {
		this.fireBusinessEvent(controller);
	}

	private HasText getDishID() {
		return this.dishID;
	}
	
	private void resetUserInput() {
		this.getDishID().clearText();
	}
	
}
