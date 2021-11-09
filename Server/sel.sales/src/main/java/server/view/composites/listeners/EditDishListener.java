package server.view.composites.listeners;

import java.math.BigDecimal;

import controller.IApplicationEvent;
import controller.IApplicationEventShooter;
import server.controller.IServerController;
import server.controller.ServerSpecificEvent;
import server.view.composites.MenuDesignArea;
import view.repository.HasText;
import view.repository.uiwrapper.ClickEventListener;

public class EditDishListener extends ClickEventListener implements IApplicationEventShooter {	private IServerController controller;
	private HasText dishName;
	private HasText dishID;
	private HasText portion;
	private HasText productionCost;
	private HasText price;
	
	public EditDishListener(IServerController controller, MenuDesignArea mda) {
		super();
		this.controller = controller;
		
		this.dishName = mda.getDishNameBox();
		this.dishID = mda.getMenuItemIDBox();
		this.portion = mda.getPortionBox();
		this.productionCost = mda.getProductionCostBox();
		this.price = mda.getPriceBox();
	}
	
	@Override
	public Object[] getArgs() {
		String data = this.controller.getModel().getDishMenuHelper().serialiseMenuItemForApp(
				this.getDishName().getText(),
				this.getDishID().getText(),
				BigDecimal.valueOf(Double.valueOf(this.getPortion().getText()).doubleValue()),
				BigDecimal.valueOf(Double.valueOf(this.getProductionCost().getText()).doubleValue()),
				BigDecimal.valueOf(Double.valueOf(this.getPrice().getText()).doubleValue())
		);
		
		this.resetUserInput();
		
		return new Object[] {data};
	}
	
	@Override
	public IApplicationEvent getApplicationEvent() {
		return ServerSpecificEvent.EDIT_DISH;
	}
	
	@Override
	public void clickAction() {
		this.fireApplicationEvent(controller);
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
