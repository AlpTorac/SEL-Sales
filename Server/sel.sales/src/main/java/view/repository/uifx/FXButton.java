package view.repository.uifx;

import java.awt.Event;
import java.awt.event.InputEvent;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import view.repository.IButton;
import view.repository.IEventShooterOnClickUIComponent;
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
	@Override
	public void performArtificialClick() {
		super.fire();
	}
}
