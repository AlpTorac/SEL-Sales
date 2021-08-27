package view.composites;

import controller.BusinessEvent;
import controller.IBusinessEventShooter;
import controller.IController;
import model.IDishMenuItemData;
import model.IDishMenuItemDataFactory;
import model.IDishMenuItemID;
import model.IDishMenuItemIDFactory;
import view.repository.ClickEventListener;
import view.repository.HasText;

public class EditDishListener extends ClickEventListener implements IBusinessEventShooter {	private IController controller;
	private HasText dishName;
	private HasText dishID;
	private HasText portion;
	private HasText productionCost;
	private HasText price;
	
	public EditDishListener(IController controller, HasText dishName, HasText dishID, HasText portion, HasText productionCost, HasText price) {
		super();
		this.controller = controller;
		
		this.dishName = dishName;
		this.dishID = dishID;
		this.portion = portion;
		this.productionCost = productionCost;
		this.price = price;
	}
	
	@Override
	public Object[] getArgs() {
		IDishMenuItemDataFactory fac = this.controller.getItemDataCommunicationProtocoll();
		IDishMenuItemIDFactory idFac = this.controller.getItemIDCommunicationProtocoll();
		
		IDishMenuItemData data = fac.constructData(
				this.getDishName().getText(),
				Double.valueOf(this.getPortion().getText()).doubleValue(), 
				Double.valueOf(this.getPrice().getText()).doubleValue(),
				Double.valueOf(this.getProductionCost().getText()).doubleValue(),
				this.getDishID().getText(),
				idFac
		);
		
		IDishMenuItemID id = idFac.createDishMenuItemID(this.getDishID().getText());
		
		this.resetUserInput();
		
		return new Object[] {id, data};
	}
	
	@Override
	public BusinessEvent getBusinessEvent() {
		return BusinessEvent.EDIT_DISH;
	}
	
	@Override
	protected void clickAction() {
		this.fireBusinessEvent(controller);
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
	
	private void resetUserInput() {
		this.getDishID().clearText();
		this.getDishName().clearText();
		this.getPortion().clearText();
		this.getPrice().clearText();
		this.getProductionCost().clearText();
	}

}
