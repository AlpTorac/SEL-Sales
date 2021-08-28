package model.order;

import java.math.BigDecimal;

import model.IDishMenuItemFinder;
import model.dish.IDishMenuItem;

public class OrderItem implements IOrderItem {
	private IDishMenuItem menuItem;
	private BigDecimal amount;
	private BigDecimal discount;
	
	OrderItem(IDishMenuItem menuItem, BigDecimal amount, BigDecimal discount) {
		this.menuItem = menuItem;
		this.amount = amount;
		this.discount = discount;
	}
	
	OrderItem(IDishMenuItemFinder finder, IOrderItemData data) {
		this.menuItem = finder.getDish(data.getItemData().getId());
		this.amount = data.getAmount();
		this.discount = data.getDiscount();
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
	public BigDecimal getTotalPortions() {
		BigDecimal totalPortions = this.getMenuItem().getPortionSize().multiply(this.getAmount());
		return totalPortions;
	}

	@Override
	public BigDecimal getDiscount() {
		return this.discount;
	}

	@Override
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Override
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	@Override
	public BigDecimal getOrderItemPrice() {
		return this.getMenuItem().getPrice().multiply(this.getAmount());
	}
}
