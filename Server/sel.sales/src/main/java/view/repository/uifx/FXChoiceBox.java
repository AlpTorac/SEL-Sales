package view.repository.uifx;

import java.util.function.Function;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;
import view.repository.IChoiceBox;
import view.repository.uiwrapper.ItemChangeListener;

public class FXChoiceBox<T> extends ChoiceBox<T> implements FXDataCollectingUIComponent<T>, IChoiceBox<T> {
	
	FXChoiceBox() {
		super();
	}
	
	@Override
	public void setChoiceDisplay(Function<T, String> toStringFunction, Function<String, T> fromStringFunction) {
		super.setConverter(new StringConverter<T>() {
			@Override
			public String toString(T object) {
				return toStringFunction.apply(object);
			}

			@Override
			public T fromString(String string) {
				return fromStringFunction.apply(string);
			}
		});
	}
	
	@Override
	public ObservableList<T> getItemList() {
		return super.getItems();
	}
	
	@Override
	public void artificiallySelectItem(int index) {
		super.requestFocus();
		super.getSelectionModel().clearAndSelect(index);
	}
	
	@Override
	public void artificiallySelectItemProperty(int index, int itemPropertyIndex) {
		this.artificiallySelectItem(index);
	}
	
	@Override
	public void artificiallySelectItem(T item) {
		super.requestFocus();
		super.getSelectionModel().clearSelection();
		super.getSelectionModel().select(item);
	}
	
	@Override
	public T getSelectedElement() {
		return super.getSelectionModel().getSelectedItem();
	}
	
	@Override
	public void addItemChangeListener(ItemChangeListener l) {
		super.setOnAction(event -> {l.selectedItemChanged(this.getSelectedElement());});
		
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
					if (c.wasReplaced() && checkRanges(c)) {
						l.itemRemovedAction(c.getRemoved());
						l.itemAddedAction(c.getAddedSubList());
					}
					if (c.wasUpdated()) {
						l.itemEditedAction(c.getAddedSubList());
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
	public void refresh() {
		
	}
}
