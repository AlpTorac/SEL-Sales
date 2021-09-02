package model.dish;

import java.math.BigDecimal;

public class DishMenuItem implements IDishMenuItem {
	private IDish dish;
	private BigDecimal portionSize;
	private BigDecimal price;
	private IDishMenuItemID id;
	private BigDecimal productionCost;
	private IDishMenuItemIDFactory fac;
	private BigDecimal discount;
	
//	DishMenuItem(IDish dish, BigDecimal portionSize, BigDecimal price, BigDecimal productionCost, IDishMenuItemID id) {
//		this.dish = dish;
//		this.portionSize = portionSize;
//		this.price = price;
//		this.productionCost = productionCost;
//		this.id = id;
//		this.discount = BigDecimal.ZERO;
//	}
//	DishMenuItem(IDish dish, BigDecimal portionSize, BigDecimal price, BigDecimal productionCost, IDishMenuItemIDFactory fac) {
//		this.dish = dish;
//		this.portionSize = portionSize;
//		this.price = price;
//		this.productionCost = productionCost;
//		this.fac = fac;
//		this.id = this.fac.createDishMenuItemID();
//		this.discount = BigDecimal.ZERO;
//	}
	DishMenuItem(IDish dish, BigDecimal portionSize, BigDecimal price, BigDecimal productionCost, BigDecimal discount, IDishMenuItemID id) {
		this.dish = dish;
		this.portionSize = portionSize;
		this.price = price;
		this.productionCost = productionCost;
		this.id = id;
		this.discount = discount;
	}
//	DishMenuItem(IDish dish, BigDecimal portionSize, BigDecimal price, BigDecimal productionCost, BigDecimal discount, IDishMenuItemIDFactory fac) {
//		this.dish = dish;
//		this.portionSize = portionSize;
//		this.price = price;
//		this.productionCost = productionCost;
//		this.fac = fac;
//		this.id = this.fac.createDishMenuItemID();
//		this.discount = discount;
//	}
	
	@Override
	public IDish getDish() {
		return dish;
	}

	@Override
	public void setDish(IDish dish) {
		this.dish = dish;
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
	public IDishMenuItemID getID() {
		return id;
	}

	@Override
	public void setID(IDishMenuItemID id) {
		this.id = id;
	}

	@Override
	public BigDecimal getProductionCost() {
		return productionCost;
	}

	@Override
	public void setProductionCost(BigDecimal productionCost) {
		this.productionCost = productionCost;
	}
	
	public BigDecimal getDiscount() {
		return discount;
	}
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
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
