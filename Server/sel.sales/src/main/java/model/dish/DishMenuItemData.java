package model.dish;

import java.math.BigDecimal;

public class DishMenuItemData implements IDishMenuItemData {
	private String dishName;
	private BigDecimal portionSize;
	private BigDecimal price;
	private BigDecimal productionCost;
	private IDishMenuItemID id;
	
	DishMenuItemData(String dishName, BigDecimal portionSize, BigDecimal price, BigDecimal productionCost, IDishMenuItemID id) {
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

	public BigDecimal getPrice() {
		return price;
	}

	public BigDecimal getProductionCost() {
		return productionCost;
	}

	public IDishMenuItemID getId() {
		return id;
	}
}
