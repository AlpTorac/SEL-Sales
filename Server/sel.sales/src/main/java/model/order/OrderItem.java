package model.order;

import java.math.BigDecimal;

import model.dish.IDishMenuItemData;

public class OrderItem implements IOrderItem {
	private IDishMenuItemData menuItem;
	private BigDecimal amount;
	
	public OrderItem(IDishMenuItemData menuItem, BigDecimal amount) {
		this.menuItem = menuItem;
		this.amount = amount;
	}
	
	OrderItem(IOrderItemData data) {
		this(data.getItemData(), data.getAmount());
//		this.menuItem = data.getItemData();
//		this.amount = data.getAmount();
	}
	
	@Override
	public IDishMenuItemData getMenuItemData() {
		return this.menuItem;
	}

	@Override
	public BigDecimal getAmount() {
		return this.amount;
	}

	@Override
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
