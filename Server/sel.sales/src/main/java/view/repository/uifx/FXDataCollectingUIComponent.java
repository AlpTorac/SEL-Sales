package view.repository.uifx;

import java.util.Collection;

import javafx.collections.ObservableList;
import view.repository.IDataCollectingUIComponent;
import view.repository.uiwrapper.ItemChangeListener;

public interface FXDataCollectingUIComponent<T> extends IDataCollectingUIComponent<T> {
	default public void addItem(T item) {
		this.getItemList().add(item);
	}
	
	default public void clear() {
		this.getItemList().clear();
	}
	
	default public void removeItem(T item) {
		this.getItemList().remove(item);
	}
	
	default public int getSize() {
		return this.getItemList().size();
	}
	
	default public Collection<T> getAllItems() {
		return this.getItemList().subList(0, this.getSize());
	}
	
	default public T getItem(int index) {
		return this.getItemList().get(index);
	}
	
	default public Collection<T> getItems(int beginIndex, int endIndex) {
		return this.getItemList().subList(beginIndex, endIndex);
	}
	
	@SuppressWarnings("unchecked")
	default public ObservableList<T> getItemList() {
		return ((FXDataCollectingUIComponent<T>) this.getComponent()).getItemList();
	}
	
	default public boolean contains(T item) {
		return this.getItemList().contains(item);
	}
	
	@SuppressWarnings("unchecked")
	default public void addItemChangeListener(ItemChangeListener l) {
		((FXDataCollectingUIComponent<T>) this.getComponent()).addItemChangeListener(l);
	}
	
	@SuppressWarnings("unchecked")
	default public void artificiallySelectItem(int index) {
		((FXDataCollectingUIComponent<T>) this.getComponent()).artificiallySelectItem(index);
	}
	
	@SuppressWarnings("unchecked")
	default public void artificiallySelectItemProperty(int index, int itemPropertyIndex) {
		((FXDataCollectingUIComponent<T>) this.getComponent()).artificiallySelectItemProperty(index, itemPropertyIndex);
	}
	
	@SuppressWarnings("unchecked")
	default public T getSelectedElement() {
		return ((FXDataCollectingUIComponent<T>) this.getComponent()).getSelectedElement();
	}
}
