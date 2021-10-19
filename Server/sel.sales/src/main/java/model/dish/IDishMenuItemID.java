package model.dish;

public interface IDishMenuItemID extends Comparable<IDishMenuItemID> {
	String toString();

	boolean equals(Object o);

	default public int compareTo(IDishMenuItemID o) {
		return this.toString().compareTo(o.toString());
	}
}