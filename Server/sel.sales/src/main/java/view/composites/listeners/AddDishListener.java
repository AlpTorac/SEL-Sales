package view.composites.listeners;

import java.math.BigDecimal;

import controller.BusinessEvent;
import controller.IApplicationEvent;
import controller.IApplicationEventShooter;
import controller.IController;
import view.composites.MenuDesignArea;
import view.repository.HasText;
import view.repository.uiwrapper.ClickEventListener;

public class AddDishListener extends ClickEventListener implements IApplicationEventShooter {
	private IController controller;
	private HasText dishName;
	private HasText dishID;
	private HasText portion;
	private HasText productionCost;
	private HasText price;
	
	public AddDishListener(IController controller, MenuDesignArea mda) {
		super();
		this.controller = controller;
		
		this.dishName = mda.getDishNameBox();
		this.dishID = mda.getMenuItemIDBox();
		this.portion = mda.getPortionBox();
		this.productionCost = mda.getProductionCostBox();
		this.price = mda.getPriceBox();
	}

	@Override
	public void clickAction() {
		this.fireApplicationEvent(controller);
	}

	@Override
	public Object[] getArgs() {
		String data = this.controller.getModel().getDishMenuItemSerialiser().serialise(
				this.getDishName().getText(),
				this.getDishID().getText(),
				BigDecimal.valueOf(Double.valueOf(this.getPortion().getText()).doubleValue()),
				BigDecimal.valueOf(Double.valueOf(this.getProductionCost().getText()).doubleValue()),
				BigDecimal.valueOf(Double.valueOf(this.getPrice().getText()).doubleValue()));
		
		this.resetUserInput();
		
		return new Object[] {data};
	}

	@Override
	public IApplicationEvent getApplicationEvent() {
		return BusinessEvent.ADD_DISH;
	}

	private void resetUserInput() {
		this.getDishID().clearText();
		this.getDishName().clearText();
		this.getPortion().clearText();
		this.getPrice().clearText();
		this.getProductionCost().clearText();
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
