package view.repository.uifx;

import view.repository.HasPlaceholderText;

import javafx.scene.control.TextInputControl;

public interface FXHasPlaceholderText extends HasPlaceholderText, FXHasText {
	@Override
	default void setPlaceholderText(String placeholder) {
		((TextInputControl) this.getComponent()).setPromptText(placeholder);
	}
	
	@Override
	default String getPlaceholderText() {
		return ((TextInputControl) this.getComponent()).getPromptText();
	}
	
	@Override
	default void clearPlaceholderText() {
		((TextInputControl) this.getComponent()).setPromptText(null);
	}
}
