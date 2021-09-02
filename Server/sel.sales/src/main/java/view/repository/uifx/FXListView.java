package view.repository.uifx;

import java.util.Collection;

import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import view.repository.IListView;
import view.repository.uiwrapper.ClickEventListener;

public class FXListView<T> extends ListView<T> implements FXHasText, IListView<T> {
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
	public int getSize() {
		return super.getItems().size();
	}
	
	@Override
	public void addItemIfNotPresent(T item) {
		if (super.getItems().stream().noneMatch(i -> i.equals(item))) {
			this.addItem(item);
		}
	}
	@Override
	public void addClickListener(ClickEventListener l) {
		ListView<T> ref = this;
		EventHandler<MouseEvent> event = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click) {
				l.action(click.getClickCount(), ref.getSelectionModel().getSelectedItems().toArray(Object[]::new));
			}
		};
		super.setOnMouseClicked(event);
	}
	@Override
	public void removeClickListener(ClickEventListener l) {
		super.setOnMouseClicked(null);
	}
	
	@Override
	public Collection<T> getItems(int beginIndex, int endIndex) {
		return super.getItems().subList(beginIndex, endIndex);
	}
	
	@Override
	public boolean contains(T item) {
		return super.getItems().stream().anyMatch(t -> t.equals(item));
	}
	
	@Override
	public void performArtificialClick() {
		//ToDo
	}
}
