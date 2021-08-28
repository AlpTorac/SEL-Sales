package model.dish;

public interface IDishMenuItemID extends Comparable<IDishMenuItemID> {

	String getID();

	String toString();

	boolean equals(Object o);

	default public int compareTo(IDishMenuItemID o) {
		return this.getID().compareTo(o.getID());
	}
}