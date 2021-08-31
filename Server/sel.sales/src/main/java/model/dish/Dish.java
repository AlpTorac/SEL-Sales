package model.dish;

public class Dish implements IDish {
	private String dishName;
	
	Dish(String dishName) {
		this.dishName = dishName;
	}
	
	@Override
	public String getName() {
		return this.dishName;
	}

	@Override
	public void setName(String name) {
		this.dishName = name;
	}
}
