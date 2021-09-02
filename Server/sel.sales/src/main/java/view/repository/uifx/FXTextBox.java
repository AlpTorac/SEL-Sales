package view.repository.uifx;

import java.awt.Event;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import view.repository.ITextBox;
import view.repository.uiwrapper.ClickEventListener;

public class FXTextBox extends TextField implements ITextBox, FXHasText {

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
	
	public void setCaption(String caption) {
		super.setText(caption);
	}
	
	public void removeClickListener(ClickEventListener l) {
		this.setOnAction(null);
	}
	
	public void clearText() {
		super.clear();
	}
	
	@Override
	public void performArtificialClick() {
		//ToDo
	}
}
