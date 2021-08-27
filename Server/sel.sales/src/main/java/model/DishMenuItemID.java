package model;

public class DishMenuItemID implements IDishMenuItemID {
	private String id;
	
	DishMenuItemID(String id) {
		this.id = id;
	}
	
	@Override
	public String getID() {
		return this.id;
	}
	
	@Override
	public String toString() {
		return this.getID();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof IDishMenuItemID)) {
			return false;
		}
		
		IDishMenuItemID otherID = (IDishMenuItemID) o;
		String thisIDContent = this.getID();
		String otherIDContent = otherID.getID();
		boolean result = thisIDContent.equals(otherIDContent);
		return result;
	}
}
