package model;

public class DishMenuItemID {
	private Object id;
	
	DishMenuItemID(Object id) {
		this.id = id;
	}
	
	public String getID() {
		return this.toString();
	}
	
	@Override
	public String toString() {
		return this.id.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof DishMenuItemID)) {
			return false;
		}
		
		DishMenuItemID id = (DishMenuItemID) o;
		return this.id.equals(id);
	}
}
