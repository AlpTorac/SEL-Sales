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

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof IOrderItemData)) {
			return false;
		}
		IOrderItemData castedO = (IOrderItemData) o;
		return this.getAmount().compareTo(castedO.getAmount()) == 0 && this.getItemData().equals(castedO.getItemData());
	}
	
	@Override
	public IOrderItemData combine(IOrderItemData data) {
		if (!data.getItemData().equals(this.menuItemData)) {
			throw new IllegalArgumentException("Cannot combine order item data containing different dishes.");
		}
		return new OrderItemData(this.getItemData(), this.getAmount().add(data.getAmount()));
	}
}
