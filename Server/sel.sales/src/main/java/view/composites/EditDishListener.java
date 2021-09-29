package view.composites;

import java.math.BigDecimal;

import controller.BusinessEvent;
import controller.IApplicationEvent;
import controller.IApplicationEventShooter;
import controller.IController;
import view.repository.HasText;
import view.repository.uiwrapper.ClickEventListener;

public class EditDishListener extends ClickEventListener implements IApplicationEventShooter {	private IController controller;
	private HasText dishName;
	private HasText dishID;
	private HasText portion;
	private HasText productionCost;
	private HasText price;
	private HasText discount;
	
	public EditDishListener(IController controller, MenuDesignArea mda) {
		super();
		this.controller = controller;
		
		this.dishName = mda.getDishNameBox();
		this.dishID = mda.getMenuItemIDBox();
		this.portion = mda.getPortionBox();
		this.productionCost = mda.getProductionCostBox();
		this.price = mda.getPriceBox();
		this.discount = mda.getDiscountBox();
	}
	
	@Override
	public Object[] getArgs() {
		String discountAsText = this.getDiscount().getText();
		BigDecimal discount = BigDecimal.ZERO;
		
		if (discountAsText != null) {
			discount = BigDecimal.valueOf(Double.valueOf(discountAsText).doubleValue());
		}
		
		String data = this.controller.getDishMenuItemSerialiser().serialise(
				this.getDishName().getText(),
				this.getDishID().getText(),
				BigDecimal.valueOf(Double.valueOf(this.getPortion().getText()).doubleValue()),
				BigDecimal.valueOf(Double.valueOf(this.getProductionCost().getText()).doubleValue()),
				BigDecimal.valueOf(Double.valueOf(this.getPrice().getText()).doubleValue()),
				discount
		);
		
		this.resetUserInput();
		
		return new Object[] {data};
	}
	
	@Override
	public IApplicationEvent getApplicationEvent() {
		return BusinessEvent.EDIT_DISH;
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
		this.getDiscount().clearText();
	}

	public HasText getDiscount() {
		return discount;
	}

}
