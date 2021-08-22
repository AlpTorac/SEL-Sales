package model;

import java.math.BigDecimal;

public class DishMenuItem {
	private Dish dish;
	private BigDecimal amount;
	private BigDecimal price;
	private DishMenuItemID id;
	
	DishMenuItem(Dish dish, double amount, double price) {
		this.dish = dish;
		this.amount = BigDecimal.valueOf(amount);
		this.price = BigDecimal.valueOf(price);
	}
	
	DishMenuItem(Dish dish, BigDecimal amount, BigDecimal price) {
		this.dish = dish;
		this.amount = amount;
		this.price = price;
	}
	
	public Dish getDish() {
		return this.dish;
	}
	
	public BigDecimal getAmount() {
		return this.amount;
	}
	
	public BigDecimal getPrice() {
		return this.price;
	}
	
	public DishMenuItemID getID() {
		return this.id;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof DishMenuItem)) {
			return false;
		} else {
			DishMenuItem item = (DishMenuItem) o;
			return this.getID().equals(item.getID());
		}
	}
}
