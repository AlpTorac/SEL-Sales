package view.repository.uifx;

import javafx.scene.control.ListView;
import view.repository.IListView;

public class FXListView<T> extends ListView<T> implements FXAttachable, IListView<T> {
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
}
