package view.repository.uifx;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import view.repository.IListView;
import view.repository.uiwrapper.ClickEventListener;
import view.repository.uiwrapper.ItemChangeListener;

public class FXListView<T> extends ListView<T> implements FXHasText, IListView<T>, FXEventShooterOnClickUI, FXDataCollectingUIComponent<T> {
//	@Override
//	public synchronized void addItem(T item) {
//		super.getItems().add(item);
//	}
//	
//	@Override
//	public synchronized void clear() {
//		super.getItems().clear();
//	}
//	
//	@Override
//	public synchronized void removeItem(T item) {
//		super.getItems().remove(item);
//	}
//	
//	@Override
//	public synchronized int getSize() {
//		return super.getItems().size();
//	}
//	
//	@Override
//	public synchronized void addItemIfNotPresent(T item) {
//		if (super.getItems().stream().noneMatch(i -> i.equals(item))) {
//			this.addItem(item);
//		}
//	}
	@Override
	public void addClickListener(ClickEventListener l) {
		ListView<T> ref = this;
		EventHandler<MouseEvent> event = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click) {
				l.clickAction(click.getClickCount(), ref.getSelectionModel().getSelectedItems().toArray(Object[]::new));
			}
		};
		super.setOnMouseClicked(event);
	}
	@Override
	public void removeClickListener(ClickEventListener l) {
		super.setOnMouseClicked(null);
	}
	
	public ObservableList<T> getItemList() {
		return super.getItems();
	}
	
//	@Override
//	public synchronized Collection<T> getItems(int beginIndex, int endIndex) {
//		return super.getItems().subList(beginIndex, endIndex);
//	}
//	
//	@Override
//	public synchronized T getItem(int index) {
//		return super.getItems().get(index);
//	}
//	
//	@Override
//	public synchronized boolean contains(T item) {
//		return super.getItems().stream().anyMatch(t -> t.equals(item));
//	}
	
	@Override
	public void artificiallySelectItem(int index) {
		super.requestFocus();
		super.getSelectionModel().clearAndSelect(index);
		super.getFocusModel().focus(index);
	}
	
	@Override
	public void artificiallySelectItemProperty(int index, int itemPropertyIndex) {
		this.artificiallySelectItem(index);
	}
	
	@Override
	public void addItemChangeListener(ItemChangeListener l) {
		ListChangeListener<T> listener = new ListChangeListener<T>() {
			@Override
			public void onChanged(Change<? extends T> c) {
				while (c.next()) {
					if (c.wasAdded() && checkRanges(c)) {
						l.itemAddedAction(c.getAddedSubList());
					}
					if (c.wasRemoved()) {
						l.itemRemovedAction(c.getRemoved());
					}
					if (c.wasReplaced()) {
						l.itemEditedAction(c.getRemoved());
					}
				}
				c.reset();
			}
			
			private boolean checkRanges(Change<? extends T> c) {
				int size = c.getList().size();
				return size > 0 && c.getFrom() < size && c.getTo() <= size && c.getFrom() <= c.getTo();
			}
		};
		
		super.getItems().addListener(listener);
	}
	
	@Override
	public T getSelectedElement() {
		return super.getSelectionModel().getSelectedItem();
	}
}
