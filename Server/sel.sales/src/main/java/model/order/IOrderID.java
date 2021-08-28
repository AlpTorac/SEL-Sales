package model.order;

public interface IOrderID extends Comparable<IOrderID> {
	String getID();
	String toString();
	boolean equals(Object o);
	
	default public int compareTo(IOrderID o) {
		return this.getID().compareTo(o.getID());
	}
}
