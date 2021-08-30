package model.order;

import java.math.BigDecimal;

import model.dish.IDishMenuItemData;

public interface IOrderItemData {
	IDishMenuItemData getItemData();
	BigDecimal getAmount();
	
	default BigDecimal getTotalDiscount() {
		return this.getItemData().getDiscount().multiply(this.getAmount());
	}
	
	default BigDecimal getPortionsInOrder() {
		return this.getItemData().getPortionSize().multiply(this.getAmount());
	}

	default BigDecimal getGrossPricePerMenuItem() {
		return this.getItemData().getGrossPrice();
	}

	default BigDecimal getNetPricePerMenuItem() {
		return this.getItemData().getNetPrice();
	}
	
	default BigDecimal getGrossPrice() {
		return this.getGrossPricePerMenuItem().multiply(this.getAmount());
	}
	
	default BigDecimal getNetPrice() {
		return this.getNetPricePerMenuItem().multiply(this.getAmount());
	}
}
