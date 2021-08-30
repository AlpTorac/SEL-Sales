package model.order;

import java.math.BigDecimal;

import model.IDishMenuItemFinder;
import model.dish.IDishMenuItem;

public class OrderItem implements IOrderItem {
	private IDishMenuItem menuItem;
	private BigDecimal amount;
	
	OrderItem(IDishMenuItem menuItem, BigDecimal amount) {
		this.menuItem = menuItem;
		this.amount = amount;
	}
	
	OrderItem(IDishMenuItemFinder finder, IOrderItemData data) {
		this.menuItem = finder.getDish(data.getItemData().getId());
		this.amount = data.getAmount();
	}
	
	@Override
	public IDishMenuItem getMenuItem() {
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
