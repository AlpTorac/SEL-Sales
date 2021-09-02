package model.dish;

import java.math.BigDecimal;

public class DishMenuItemData implements IDishMenuItemData {
	private String dishName;
	private BigDecimal portionSize;
	private BigDecimal price;
	private BigDecimal productionCost;
	private BigDecimal discount;
	private String id;
	
//	DishMenuItemData(String dishName, BigDecimal portionSize, BigDecimal price, BigDecimal productionCost, IDishMenuItemID id) {
//		this.dishName = dishName;
//		this.portionSize = portionSize;
//		this.price = price;
//		this.productionCost = productionCost;
//		this.id = id;
//		this.discount = BigDecimal.ZERO;
//	}
	
	DishMenuItemData(String dishName, BigDecimal portionSize, BigDecimal price, BigDecimal productionCost, BigDecimal discount, String id) {
		this.dishName = dishName;
		this.portionSize = portionSize;
		this.price = price;
		this.productionCost = productionCost;
		this.id = id;
		this.discount = discount;
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

	public String getId() {
		return this.id;
	}

	@Override
	public BigDecimal getDiscount() {
		return discount;
	}
	
	@Override
	public String toString() {
		return this.getId().toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof IDishMenuItemData)) {
			return false;
		}
		
		return this.compareTo(((IDishMenuItemData) o)) == 0;
	}
}
