package model.dish;

import java.math.BigDecimal;

public class DishMenuItem implements IDishMenuItem {
	private IDish dish;
	private BigDecimal portionSize;
	private BigDecimal price;
	private IDishMenuItemID id;
	private BigDecimal productionCost;
	
	DishMenuItem(IDish dish, BigDecimal portionSize, BigDecimal price, BigDecimal productionCost, IDishMenuItemID id) {
		this.dish = dish;
		this.portionSize = portionSize;
		this.price = price;
		this.productionCost = productionCost;
		this.id = id;
	}
	
	@Override
	public IDish getDish() {
		return dish;
	}

	@Override
	public BigDecimal getPortionSize() {
		return portionSize;
	}

	@Override
	public void setPortionSize(BigDecimal portionSize) {
		this.portionSize = portionSize;
	}

	@Override
	public BigDecimal getPrice() {
		return price;
	}

	@Override
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public String getID() {
		return this.id.toString();
	}

	@Override
	public BigDecimal getProductionCost() {
		return productionCost;
	}

	@Override
	public void setProductionCost(BigDecimal productionCost) {
		this.productionCost = productionCost;
	}
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof IDishMenuItem)) {
			return false;
		}
		
		IDishMenuItem otherItem = (IDishMenuItem) o;
		boolean result = this.getID().equals(otherItem.getID());
		return result;
	}
}
