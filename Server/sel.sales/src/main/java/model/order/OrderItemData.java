package model.order;

import java.math.BigDecimal;

import model.dish.IDishMenuItemData;

public class OrderItemData implements IOrderItemData {
	private IDishMenuItemData menuItemData;
	private BigDecimal amount;
	private BigDecimal discount;
	
	OrderItemData(IDishMenuItemData menuItemData, BigDecimal amount) {
		this.menuItemData = menuItemData;
		this.amount = amount;
		this.discount = BigDecimal.ZERO;
	}
	
	OrderItemData(IDishMenuItemData menuItemData, BigDecimal amount, BigDecimal discount) {
		this.menuItemData = menuItemData;
		this.amount = amount;
		this.discount = discount;
	}

	@Override
	public BigDecimal getAmount() {
		return this.amount;
	}

	@Override
	public BigDecimal getPortionSize() {
		return this.menuItemData.getPortionSize().multiply(this.getAmount());
	}

	@Override
	public BigDecimal getPricePerPortion() {
		return this.menuItemData.getPrice();
	}

	@Override
	public BigDecimal getTotalPrice() {
		return this.getPricePerPortion().multiply(this.getAmount());
	}

	@Override
	public BigDecimal getDiscount() {
		return this.discount;
	}

	@Override
	public IDishMenuItemData getItemData() {
		return this.menuItemData;
	}

}
