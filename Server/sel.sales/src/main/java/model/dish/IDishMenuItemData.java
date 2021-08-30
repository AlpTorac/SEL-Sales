package model.dish;

import java.math.BigDecimal;

public interface IDishMenuItemData {
	String getDishName();

	default BigDecimal getGrossPricePerPortion() {
		return this.getGrossPrice().divide(this.getPortionSize());
	}
	
	default BigDecimal getNetPricePerPortion() {
		return this.getNetPrice().divide(this.getPortionSize());
	}
	
	default BigDecimal getDiscountPerPortion() {
		return this.getDiscount().divide(this.getPortionSize());
	}
	
	BigDecimal getPortionSize();

	BigDecimal getGrossPrice();

	BigDecimal getProductionCost();

	BigDecimal getDiscount();
	
	default BigDecimal getNetPrice() {
		return this.getGrossPrice().subtract(this.getDiscount());
	}
	
	IDishMenuItemID getId();
}
