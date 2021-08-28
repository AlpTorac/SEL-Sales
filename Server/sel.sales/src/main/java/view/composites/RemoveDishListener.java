package view.composites;

import controller.BusinessEvent;
import controller.IBusinessEventShooter;
import controller.IController;
import model.dish.IDishMenuItemID;
import model.dish.IDishMenuItemIDFactory;
import view.repository.HasText;
import view.repository.uiwrapper.ClickEventListener;

public class RemoveDishListener extends ClickEventListener implements IBusinessEventShooter {
	private IController controller;
	private HasText dishID;
	
	public RemoveDishListener(IController controller, HasText dishID) {
		this.controller = controller;
		this.dishID = dishID;
	}
	
	@Override
	public Object[] getArgs() {
		IDishMenuItemIDFactory idFac = this.controller.getItemIDCommunicationProtocoll();
		IDishMenuItemID id = idFac.createDishMenuItemID(this.getDishID().getText());
		this.resetUserInput();
		return new Object[] {id};
	}

	@Override
	public BusinessEvent getBusinessEvent() {
		return BusinessEvent.REMOVE_DISH;
	}

	@Override
	protected void clickAction() {
		this.fireBusinessEvent(controller);
	}

	private HasText getDishID() {
		return this.dishID;
	}
	
	private void resetUserInput() {
		this.getDishID().clearText();
	}
	
}
