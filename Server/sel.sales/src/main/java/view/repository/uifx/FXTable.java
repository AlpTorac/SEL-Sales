package view.repository.uifx;

import java.util.function.Function;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import view.repository.ITable;
import view.repository.uiwrapper.ItemChangeListener;

public class FXTable<T> extends TableView<T> implements FXHasText, ITable<T>, FXEventShooterOnClickUI, FXDataCollectingUIComponent<T> {
	
	FXTable() {
		super();
		super.setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
	}

	@Override
	public <O> void addColumn(String title, String fieldName) {
		super.getColumns().add(this.makePropertyValueColumn(title, fieldName));
	}

	@Override
	public <O> void addColumn(String title, Function<T, O> f) {
		super.getColumns().add(this.makeSimpleValueColumn(title, f));
	}
	
	protected <O> TableColumn<T, O> makePropertyValueColumn(String title, String fieldName) {
		TableColumn<T, O> col = new TableColumn<>(title);
		col.setCellValueFactory(new PropertyValueFactory<T, O>(fieldName));
		return col;
	}
	
	protected <O> TableColumn<T, O> makeSimpleValueColumn(String title, Function<T, O> f) {
		TableColumn<T, O> col = new TableColumn<>(title);
		col.setCellValueFactory(cd -> new SimpleObjectProperty<O>(f.apply(cd.getValue())));
		col.setCellFactory(c -> new TableCell<T, O>() {
			@Override
			protected void updateItem(O item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.toString());
			}
		});
		return col;
	}
	
//	@Override
//	public void addItem(T item) {
//		super.getItems().add(item);
//	}
//	
//	@Override
//	public void clear() {
//		super.getItems().clear();
//	}
//	
//	@Override
//	public void removeItem(T item) {
//		super.getItems().remove(item);
//	}
//	
//	@Override
//	public Collection<T> getItems(int beginIndex, int endIndex) {
//		return super.getItems().subList(beginIndex, endIndex);
//	}
//	
//	@Override
//	public T getItem(int index) {
//		return super.getItems().get(index);
//	}
//	
//	@Override
//	public int getSize() {
//		return super.getItems().size();
//	}
//	
//	@Override
//	public boolean contains(T item) {
//		return super.getItems().stream().anyMatch(t -> t.equals(item));
//	}
	
	@Override
	public ObservableList<T> getItemList() {
		return super.getItems();
	}
	
	@Override
	public void artificiallySelectItem(int index) {
		super.requestFocus();
		super.getSelectionModel().clearAndSelect(index);
		super.getFocusModel().focus(index);
	}
	
	@Override
	public void artificiallySelectItemProperty(int index, int itemPropertyIndex) {
		super.requestFocus();
		super.getSelectionModel().clearAndSelect(index,
				super.getColumns().get(itemPropertyIndex));
		super.getFocusModel().focus(index,
				super.getColumns().get(itemPropertyIndex));
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
		super.refresh();
	}
}
