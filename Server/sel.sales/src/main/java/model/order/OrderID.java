package model.order;

public class OrderID implements IOrderID {
	private String id;
	
	OrderID(String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return this.id;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof IOrderID)) {
			return false;
		}
		
		IOrderID otherID = (IOrderID) o;
		String thisIDContent = this.toString();
		String otherIDContent = otherID.toString();
		boolean result = thisIDContent.equals(otherIDContent);
		return result;
	}
}
