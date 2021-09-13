package view.repository.uifx;

import java.util.Collection;

import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import view.repository.IDataCollectingUIComponent;
import view.repository.IListView;
import view.repository.uiwrapper.ClickEventListener;
import view.repository.uiwrapper.ItemChangeListener;

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
				l.clickAction(click.getClickCount(), ref.getSelectionModel().getSelectedItems().toArray(Object[]::new));
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
	public T getItem(int index) {
		return super.getItems().get(index);
	}
	
	@Override
	public boolean contains(T item) {
		return super.getItems().stream().anyMatch(t -> t.equals(item));
	}
	
	/**
	 * Performs a click on the middle of the list (the middle of the list component itself,
	 * not necessarily on an element)
	 * <p>
	 * Make sure that the list is in a Window.
	 */
	@Override
	public void performArtificialClicks(int clickCount) {
		double width = super.getMinWidth();
		double height = super.getMinHeight();
		
		Point2D sceneP = super.localToScene(0, 0); // top left corner in scene coords
		double sceneX = sceneP.getX();
		double sceneY = sceneP.getY();
		Point2D clickAreaOnScene = new Point2D(width / 2d + sceneX, height / 2d + sceneY);
		
		Point2D screenP = super.localToScreen(0, 0); // top left corner in screen coords
		double screenX = screenP.getX();
		double screenY = screenP.getY();
		Point2D clickAreaOnScreen = new Point2D(width / 2d + screenX, height / 2d + screenY);
		
		super.fireEvent(new MouseEvent(
				MouseEvent.MOUSE_CLICKED,
				clickAreaOnScene.getX(),
				clickAreaOnScene.getY(),
				clickAreaOnScreen.getX(),
				clickAreaOnScreen.getY(),
				MouseButton.PRIMARY,
				clickCount,
				false,
				false,
				false,
				false,
				false,
				false,
				false,
				false,
				false,
				false,
				false,
				true,
				null));
	}
	@Override
	public void artificiallySelectItem(int index, int itemPropertyIndex) {
		super.getSelectionModel().select(index);
	}
	
	@Override
	public void addItemChangeListener(ItemChangeListener l) {
		ListView<T> ref = this;
		
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
		
		ref.getItems().addListener(listener);
	}
}
