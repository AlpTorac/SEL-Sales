package model.dish;

import java.math.BigDecimal;

public interface IDishMenuItemData {
	String getDishName();

	BigDecimal getPortionSize();

	BigDecimal getGrossPrice();

	BigDecimal getProductionCost();

	BigDecimal getDiscount();
	
	default BigDecimal getNetPrice() {
		return this.getGrossPrice().subtract(this.getDiscount());
	}
	
	IDishMenuItemID getId();
}
