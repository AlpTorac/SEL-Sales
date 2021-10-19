package model.order;

import java.math.BigDecimal;

import model.dish.IDishMenuItemData;

public interface IOrderItemData extends Comparable<IOrderItemData> {
	IDishMenuItemData getItemData();
	BigDecimal getAmount();
	
	default BigDecimal getPortionsInOrder() {
		return this.getItemData().getPortionSize().multiply(this.getAmount());
	}

	default BigDecimal getGrossPricePerMenuItem() {
		return this.getItemData().getGrossPrice();
	}
	
	default BigDecimal getGrossPricePerPortion() {
		return this.getItemData().getGrossPricePerPortion();
	}
	
	default BigDecimal getGrossPrice() {
		return this.getGrossPricePerMenuItem().multiply(this.getAmount());
	}
	
	default public int compareTo(IOrderItemData data) {
		return this.getItemData().getId().compareTo(data.getItemData().getId());
	}
	
	IOrderItemData combine(IOrderItemData data);
}
