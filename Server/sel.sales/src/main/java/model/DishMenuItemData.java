package model;

public class DishMenuItemData implements IDishMenuItemData {
	private String dishName;
	private double portionSize;
	private double price;
	private double productionCost;
	private Object id;
	
	DishMenuItemData(String dishName, double portionSize, double price, double productionCost, Object id) {
		this.dishName = dishName;
		this.portionSize = portionSize;
		this.price = price;
		this.productionCost = productionCost;
		this.id = id;
	}

	public String getDishName() {
		return dishName;
	}

	public double getPortionSize() {
		return portionSize;
	}

	public double getPrice() {
		return price;
	}

	public double getProductionCost() {
		return productionCost;
	}

	public Object getId() {
		return id;
	}
}
