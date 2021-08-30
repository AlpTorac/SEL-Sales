package view.repository.uifx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import view.repository.IEventShooterOnClickUIComponent;
import view.repository.IListView;
import view.repository.uiwrapper.ClickEventListener;

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
}
