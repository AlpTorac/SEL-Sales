package view.repository;

public interface ITextBox extends IEventShooterOnClickUIComponent, HasPlaceholderText, ISizable {
	default void setWrapText(boolean wrapText) {
		((ITextBox) this.getComponent()).setWrapText(wrapText);
	}
}
