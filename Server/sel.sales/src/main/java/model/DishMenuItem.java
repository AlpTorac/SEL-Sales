package model;

import java.math.BigDecimal;

public class DishMenuItem implements IDishMenuItem {
	private IDish dish;
	private BigDecimal portionSize;
	private BigDecimal price;
	private IDishMenuItemID id;
	private BigDecimal productionCost;
	private IDishMenuItemIDFactory fac;
	
	DishMenuItem(IDish dish, double portionSize, double price, double productionCost, Object id) {
		this.dish = dish;
		this.portionSize = BigDecimal.valueOf(portionSize);
		this.price = BigDecimal.valueOf(price);
		this.productionCost = BigDecimal.valueOf(productionCost);
		this.id = new DishMenuItemID(id);
	}
	DishMenuItem(IDish dish, double portionSize, double price, double productionCost, IDishMenuItemIDFactory fac) {
		this.dish = dish;
		this.portionSize = BigDecimal.valueOf(portionSize);
		this.price = BigDecimal.valueOf(price);
		this.productionCost = BigDecimal.valueOf(productionCost);
		this.fac = fac;
		this.id = this.fac.createDishMenuItemID();
	}
	
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

	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof DishMenuItem)) {
			return false;
		} else {
			IDishMenuItem item = (IDishMenuItem) o;
			return this.getID().equals(item.getID());
		}
	}
}
