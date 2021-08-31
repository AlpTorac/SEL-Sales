package view.repository.uifx;

import java.util.Collection;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import view.repository.IDataCollectingUIComponent;
import view.repository.ITable;

public class FXTable<T> extends TableView<T> implements FXHasText, ITable<T> {
	
	FXTable() {
		super();
		super.setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
	}

	@Override
	public <O> void addColumn(String title, String fieldName) {
		TableColumn<T, O> col = new TableColumn<>(title);
		col.setCellValueFactory(new PropertyValueFactory<T, O>(fieldName));
		super.getColumns().add(col);
	}

	@Override
	public void addItem(T item) {
		super.getItems().add(item);
	}
	
	@Override
	public void clear() {
		super.getItems().clear();
	}
	
	@Override
	public void removeItem(T item) {
		super.getItems().remove(item);
	}
	
	@Override
	public Collection<T> getItems(int beginIndex, int endIndex) {
		return super.getItems().subList(beginIndex, endIndex);
	}
	
	@Override
	public int getSize() {
		return super.getItems().size();
	}
	
	@Override
	public boolean contains(T item) {
		return super.getItems().stream().anyMatch(t -> t.equals(item));
	}
}
