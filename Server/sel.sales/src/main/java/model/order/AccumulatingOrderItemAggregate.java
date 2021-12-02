package model.order;

import model.datamapper.DishMenuItemAttribute;
import model.dish.DishMenuItem;
import model.entity.AccumulatingAggregate;

public class AccumulatingOrderItemAggregate extends AccumulatingAggregate<DishMenuItemAttribute, DishMenuItem>{

	@Override
	public AccumulatingAggregate<DishMenuItemAttribute, DishMenuItem> getEmptyClone() {
		return new AccumulatingOrderItemAggregate();
	}
//	public BigDecimal getTotalPortions() {
//		return this.getMenuItem().getPortionSize().multiply(this.getAmount());
//	}
//	
//	public BigDecimal getPortionsInOrder() {
//		return this.getMenuItem().getPortionSize().multiply(this.getAmount());
//	}
//
//	public BigDecimal getGrossPricePerMenuItem() {
//		return this.getMenuItem().getGrossPrice();
//	}
//	
//	public BigDecimal getGrossPricePerPortion() {
//		return this.getMenuItem().getGrossPricePerPortion();
//	}
//	
//	public BigDecimal getGrossPrice() {
//		return this.getGrossPricePerMenuItem().multiply(this.getAmount());
//	}
}
