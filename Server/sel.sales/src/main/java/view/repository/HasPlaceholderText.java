package view.repository;

public interface HasPlaceholderText extends HasText {
	default void setPlaceholderText(String placeholder) {
		((HasPlaceholderText) this.getComponent()).setPlaceholderText(placeholder);
	}
	default String getPlaceholderText() {
		return ((HasPlaceholderText) this.getComponent()).getPlaceholderText();
	}
	default void clearPlaceholderText() {
		((HasPlaceholderText) this.getComponent()).clearPlaceholderText();
	}
}
