package view.repository;

import java.util.Collection;

public interface IDataCollectingUIComponent<T> extends IUIComponent {
	@SuppressWarnings("unchecked")
	default public void addItem(T item) {
		((IDataCollectingUIComponent<T>) this.getComponent()).addItem(item);
	}
	
	default public void addItems(T[] item) {
		for (T o : item) {
			this.addItem(o);
		}
	}
	
	@SuppressWarnings("unchecked")
	default public void addItemIfNotPresent(T item) {
		((IDataCollectingUIComponent<T>) this.getComponent()).addItemIfNotPresent(item);
	}
	
	default public void addItemsIfNotPresent(T[] item) {
		for (T o : item) {
			this.addItemIfNotPresent(o);
		}
	}
	
	default public void clear() {
		((IDataCollectingUIComponent<?>) this.getComponent()).clear();
	}
	
	@SuppressWarnings("unchecked")
	default public void removeItem(T item) {
		((IDataCollectingUIComponent<T>) this.getComponent()).removeItem(item);
	}
	
	default public int getSize() {
		return ((IDataCollectingUIComponent<?>) this.getComponent()).getSize();
	}
	
	@SuppressWarnings("unchecked")
	default public Collection<T> getAllItems() {
		IDataCollectingUIComponent<T> component = (IDataCollectingUIComponent<T>) this.getComponent();
		return component.getItems(0, component.getSize());
	}
	
	@SuppressWarnings("unchecked")
	default public Collection<T> getItems(int beginIndex, int endIndex) {
		return ((IDataCollectingUIComponent<T>) this.getComponent()).getItems(beginIndex, endIndex);
	}
	
	@SuppressWarnings("unchecked")
	default public boolean contains(T item) {
		return ((IDataCollectingUIComponent<T>) this.getComponent()).contains(item);
	}
}
