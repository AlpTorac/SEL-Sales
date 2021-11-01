package view.repository;

import java.util.Collection;

import view.repository.uiwrapper.ItemChangeListener;

public interface IDataCollectingUIComponent<T> extends ISizable {
	@SuppressWarnings("unchecked")
	default public void addItem(T item) {
		((IDataCollectingUIComponent<T>) this.getComponent()).addItem(item);
		this.refresh();
	}
	
	default public void addItems(T[] item) {
		for (T o : item) {
			this.addItem(o);
		}
	}
	
	default public void addItemIfNotPresent(T item) {
		if (!this.contains(item)) {
			this.addItem(item);
		}
	}
	
	default public void addItemsIfNotPresent(T[] item) {
		for (T o : item) {
			this.addItemIfNotPresent(o);
		}
	}
	
	default public void clear() {
		((IDataCollectingUIComponent<?>) this.getComponent()).clear();
		this.refresh();
	}
	
	@SuppressWarnings("unchecked")
	default public void removeItem(T item) {
		((IDataCollectingUIComponent<T>) this.getComponent()).removeItem(item);
		this.refresh();
	}
	
	default public int getSize() {
		return ((IDataCollectingUIComponent<?>) this.getComponent()).getSize();
	}
	
	@SuppressWarnings("unchecked")
	default public Collection<T> getAllItems() {
		return ((IDataCollectingUIComponent<T>) this.getComponent()).getAllItems();
	}
	
	@SuppressWarnings("unchecked")
	default public T getItem(int index) {
		return ((IDataCollectingUIComponent<T>) this.getComponent()).getItem(index);
	}
	
	@SuppressWarnings("unchecked")
	default public Collection<T> getItems(int beginIndex, int endIndex) {
		return ((IDataCollectingUIComponent<T>) this.getComponent()).getItems(beginIndex, endIndex);
	}
	
	@SuppressWarnings("unchecked")
	default public boolean contains(T item) {
		return ((IDataCollectingUIComponent<T>) this.getComponent()).contains(item);
	}
	
	@SuppressWarnings("unchecked")
	default public void addItemChangeListener(ItemChangeListener l) {
		((IDataCollectingUIComponent<T>) this.getComponent()).addItemChangeListener(l);
	}
	
	@SuppressWarnings("unchecked")
	default public void artificiallySelectItem(int index) {
		((IDataCollectingUIComponent<T>) this.getComponent()).artificiallySelectItem(index);
	}
	
	@SuppressWarnings("unchecked")
	default public void artificiallySelectItemProperty(int index, int itemPropertyIndex) {
		((IDataCollectingUIComponent<T>) this.getComponent()).artificiallySelectItemProperty(index, itemPropertyIndex);
	}
	
	@SuppressWarnings("unchecked")
	default public T getSelectedElement() {
		return ((IDataCollectingUIComponent<T>) this.getComponent()).getSelectedElement();
	}
	
	default public void refresh() {
		((IDataCollectingUIComponent<?>) this.getComponent()).refresh();
	}
}
