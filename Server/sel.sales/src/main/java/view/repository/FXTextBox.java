package view.repository;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

public class FXTextBox extends TextField implements ITextBox, FXAttachable {

	public void addClickListener(ClickEventListener l) {
		TextField ref = this;
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				l.action();
			}
		};
		ref.setOnAction(event);
	}

	public void removeClickListener(ClickEventListener l) {
		this.setOnAction(null);
	}
	
	public void setCaption(String caption) {
		this.setText(caption);
	}
}
