package client.view.composites;

public interface PriceUpdateTarget {
	void refreshPrice();
	void remove(Object referenceOfCaller);
}
