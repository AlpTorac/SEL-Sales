package model.order;

public interface IOrderID extends Comparable<IOrderID> {
	String toString();
	boolean equals(Object o);
	
	default public int compareTo(IOrderID o) {
		return this.toString().compareTo(o.toString());
	}
}
