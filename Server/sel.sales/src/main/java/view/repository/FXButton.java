package view.repository;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class FXButton extends Button implements IButton, FXAttachable {

	@Override
	public void addClickListener(ClickEventListener l) {
		Button ref = this;
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				l.action();
			}
		};
		ref.setOnAction(event);
	}

	@Override
	public void removeClickListener(ClickEventListener l) {
		this.setOnAction(null);
	}
	
	@Override
	public void setCaption(String caption) {
		this.setText(caption);
	}
}
