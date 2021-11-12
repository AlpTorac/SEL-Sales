package client.view.composites;

public interface PriceUpdateTarget<T> {
	void refreshPrice();
	void remove(T referenceOfCaller);
}
