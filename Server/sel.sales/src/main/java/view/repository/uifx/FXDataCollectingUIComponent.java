package view.repository.uifx;

import java.util.Collection;

import javafx.collections.ObservableList;
import view.repository.IDataCollectingUIComponent;
import view.repository.uiwrapper.ItemChangeListener;

public interface FXDataCollectingUIComponent<T> extends IDataCollectingUIComponent<T> {
	@SuppressWarnings("unchecked")
	default public void addItem(T item) {
		((FXDataCollectingUIComponent<T>) this.getComponent()).getItemList().add(item);
	}
	
	default public void clear() {
		((FXDataCollectingUIComponent<?>) this.getComponent()).getItemList().clear();
	}
	
	@SuppressWarnings("unchecked")
	default public void removeItem(T item) {
		((FXDataCollectingUIComponent<T>) this.getComponent()).getItemList().remove(item);
	}
	
	default public int getSize() {
		return ((FXDataCollectingUIComponent<?>) this.getComponent()).getItemList().size();
	}
	
	@SuppressWarnings("unchecked")
	default public Collection<T> getAllItems() {
		FXDataCollectingUIComponent<T> component = (FXDataCollectingUIComponent<T>) this.getComponent();
		return component.getItems(0, component.getSize());
	}
	
	@SuppressWarnings("unchecked")
	default public T getItem(int index) {
		return ((FXDataCollectingUIComponent<T>) this.getComponent()).getItemList().get(index);
	}
	
	@SuppressWarnings("unchecked")
	default public Collection<T> getItems(int beginIndex, int endIndex) {
		return ((FXDataCollectingUIComponent<T>) this.getComponent()).getItemList().subList(beginIndex, endIndex);
	}
	
	@SuppressWarnings("unchecked")
	default public ObservableList<T> getItemList() {
		return ((FXDataCollectingUIComponent<T>) this.getComponent()).getItemList();
	}
	
	@SuppressWarnings("unchecked")
	default public boolean contains(T item) {
		return ((FXDataCollectingUIComponent<T>) this.getComponent()).getItemList().contains(item);
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
