package view.repository.uifx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import view.repository.IButton;
import view.repository.uiwrapper.ClickEventListener;

public class FXButton extends Button implements IButton, FXHasText {

	@Override
	public void addClickListener(ClickEventListener l) {
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				l.action();
			}
		};
		super.setOnAction(event);
	}

	@Override
	public void removeClickListener(ClickEventListener l) {
		this.setOnAction(null);
	}
}
