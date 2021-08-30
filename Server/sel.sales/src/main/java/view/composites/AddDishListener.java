package view.composites;

import java.math.BigDecimal;

import controller.BusinessEvent;
import controller.IBusinessEventShooter;
import controller.IController;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemIDFactory;
import view.repository.HasText;
import view.repository.uiwrapper.ClickEventListener;

public class AddDishListener extends ClickEventListener implements IBusinessEventShooter {
	private IController controller;
	private HasText dishName;
	private HasText dishID;
	private HasText portion;
	private HasText productionCost;
	private HasText price;
	private HasText discount;
	
	public AddDishListener(IController controller, HasText dishName, HasText dishID, HasText portion, HasText productionCost, HasText price, HasText discount) {
		super();
		this.controller = controller;
		
		this.dishName = dishName;
		this.dishID = dishID;
		this.portion = portion;
		this.productionCost = productionCost;
		this.price = price;
		this.discount = discount;
	}

	@Override
	protected void clickAction() {
		this.fireBusinessEvent(controller);
	}

	@Override
	public Object[] getArgs() {
		IDishMenuItemDataFactory fac = this.controller.getItemDataCommunicationProtocoll();
		IDishMenuItemIDFactory idFac = this.controller.getItemIDCommunicationProtocoll();
		
		String discountAsText = this.getDiscount().getText();
		BigDecimal discount = BigDecimal.ZERO;
		
		if (discountAsText != null) {
			discount = BigDecimal.valueOf(Double.valueOf(discountAsText).doubleValue());
		}
		
		IDishMenuItemData data = fac.constructData(
				this.getDishName().getText(),
				BigDecimal.valueOf(Double.valueOf(this.getPortion().getText()).doubleValue()), 
				BigDecimal.valueOf(Double.valueOf(this.getPrice().getText()).doubleValue()),
				BigDecimal.valueOf(Double.valueOf(this.getProductionCost().getText()).doubleValue()),
				discount,
				this.getDishID().getText(),
				idFac
		);
		
		this.resetUserInput();
		
		return new Object[] {data};
	}

	@Override
	public BusinessEvent getBusinessEvent() {
		return BusinessEvent.ADD_DISH;
	}

	private void resetUserInput() {
		this.getDishID().clearText();
		this.getDishName().clearText();
		this.getPortion().clearText();
		this.getPrice().clearText();
		this.getProductionCost().clearText();
		this.getDiscount().clearText();
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

	public HasText getDiscount() {
		return discount;
	}

}
