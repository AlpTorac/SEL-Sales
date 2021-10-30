package model.dish;

import java.math.BigDecimal;

import model.id.EntityID;

public class DishMenuItemData implements IDishMenuItemData {
	private String dishName;
	private BigDecimal portionSize;
	private BigDecimal price;
	private BigDecimal productionCost;
	private EntityID id;
	
	DishMenuItemData(String dishName, BigDecimal portionSize, BigDecimal price, BigDecimal productionCost, EntityID id) {
		this.dishName = dishName;
		this.portionSize = portionSize;
		this.price = price;
		this.productionCost = productionCost;
		this.id = id;
	}

	public String getDishName() {
		return dishName;
	}

	public BigDecimal getPortionSize() {
		return portionSize;
	}

	public BigDecimal getGrossPrice() {
		return price;
	}

	public BigDecimal getProductionCost() {
		return productionCost;
	}

	public EntityID getID() {
		return this.id;
	}

	@Override
	public String toString() {
		return this.getID().toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof IDishMenuItemData)) {
			return false;
		}
		
		return this.compareTo(((IDishMenuItemData) o)) == 0;
	}
}
