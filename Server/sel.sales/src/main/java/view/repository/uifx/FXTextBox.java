package view.repository.uifx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import view.repository.ITextBox;
import view.repository.uiwrapper.ClickEventListener;

public class FXTextBox extends TextField implements ITextBox, FXHasText, FXEventShooterOnClickUI {

	public void addClickListener(ClickEventListener l) {
		TextField ref = this;
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				l.clickAction();
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
}
