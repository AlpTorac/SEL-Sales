package model.dish;

public class DishMenuItemID implements IDishMenuItemID {
	private String id;
	
	DishMenuItemID(String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return this.id;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof IDishMenuItemID)) {
			return false;
		}
		
		IDishMenuItemID otherID = (IDishMenuItemID) o;
		String thisIDContent = this.toString();
		String otherIDContent = otherID.toString();
		boolean result = thisIDContent.equals(otherIDContent);
		return result;
	}
}
