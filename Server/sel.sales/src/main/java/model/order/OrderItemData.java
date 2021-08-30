package model.order;

import java.math.BigDecimal;

import model.dish.IDishMenuItemData;

public class OrderItemData implements IOrderItemData {
	private IDishMenuItemData menuItemData;
	private BigDecimal amount;
	
	OrderItemData(IDishMenuItemData menuItemData, BigDecimal amount) {
		this.menuItemData = menuItemData;
		this.amount = amount;
	}

	@Override
	public BigDecimal getAmount() {
		return this.amount;
	}

	@Override
	public IDishMenuItemData getItemData() {
		return this.menuItemData;
	}
}
