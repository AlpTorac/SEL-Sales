package view.repository.uifx;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import view.repository.ISingleRowTextBox;
import view.repository.uiwrapper.ClickEventListener;

public class FXSingleRowTextBox extends TextField implements ISingleRowTextBox, FXHasPlaceholderText, FXEventShooterOnClickUI {
	
	public void addClickListener(ClickEventListener l) {
		TextField ref = this;
		EventHandler<MouseEvent> event = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				l.clickAction();
			}
		};
		ref.setOnMouseClicked(event);
	}
	
	public void setCaption(String caption) {
		super.setText(caption);
	}
	
	public void removeClickListener(ClickEventListener l) {
		this.setOnMouseClicked(null);
	}
	
	public void clearText() {
		super.clear();
	}
}
