package model;

public class Dish implements IDish {
	private String dishName;
	
	Dish(String dishName) {
		this.dishName = dishName;
	}
	
	@Override
	public String getName() {
		return this.dishName;
	}
}
