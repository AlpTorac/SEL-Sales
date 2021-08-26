package view.composites;

import controller.BusinessEvent;
import controller.IBusinessEventShooter;
import controller.IController;
import model.IDishMenuItemData;
import model.IDishMenuItemDataFactory;
import model.IDishMenuItemIDFactory;
import view.repository.ClickEventListener;
import view.repository.HasText;

public class AddDishListener extends ClickEventListener implements IBusinessEventShooter {
	private IController controller;
	private HasText dishName;
	private HasText dishID;
	private HasText portion;
	private HasText productionCost;
	private HasText price;
	
	public AddDishListener(IController controller, HasText dishName, HasText dishID, HasText portion, HasText productionCost, HasText price) {
		super();
		this.controller = controller;
		
		this.dishName = dishName;
		this.dishID = dishID;
		this.portion = portion;
		this.productionCost = productionCost;
		this.price = price;
	}

	@Override
	protected void clickAction() {
		this.fireBusinessEvent(controller);
	}

	@Override
	public Object[] getArgs() {
		IDishMenuItemDataFactory fac = this.controller.getItemDataCommunicationProtocoll();
		IDishMenuItemIDFactory idFac = this.controller.getItemIDCommunicationProtocoll();
		
		IDishMenuItemData data = fac.constructData(
				this.getDishName().getText(),
				Double.valueOf(this.getPortion().getText()).doubleValue(), 
				Double.valueOf(this.getProductionCost().getText()).doubleValue(),
				Double.valueOf(this.getPrice().getText()).doubleValue(),
				idFac.createDishMenuItemID(this.getDishID().getText())
		);
		return new Object[] {data};
	}

	@Override
	public BusinessEvent getBusinessEvent() {
		return BusinessEvent.ADD_DISH;
	}

	private HasText getDishName() {
		return dishName;
	}

	private HasText getDishID() {
		return dishID;
	}

	private HasText getPortion() {
		return portion;
	}

	private HasText getProductionCost() {
		return productionCost;
	}

	private HasText getPrice() {
		return price;
	}

}
