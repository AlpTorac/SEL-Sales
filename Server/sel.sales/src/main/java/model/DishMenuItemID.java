package model;

public class DishMenuItemID implements IDishMenuItemID {
	private Object id;
	
	DishMenuItemID(Object id) {
		this.id = id;
	}
	
	@Override
	public String getID() {
		return this.toString();
	}
	
	@Override
	public String toString() {
		return this.id.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof IDishMenuItemID)) {
			return false;
		}
		
		IDishMenuItemID id = (IDishMenuItemID) o;
		return this.id.equals(id);
	}
}
