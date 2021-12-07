package view.repository;

import java.util.function.Function;

public interface IChoiceBox<T> extends IDataCollectingUIComponent<T> {
	@SuppressWarnings("unchecked")
	default void setChoiceDisplay(Function<T, String> toStringFunction, Function<String, T> fromStringFunction) {
		((IChoiceBox<T>) this.getComponent()).setChoiceDisplay(toStringFunction, fromStringFunction);
	}
	default void setChoiceDisplay(Function<T, String> toStringFunction) {
		this.setChoiceDisplay(toStringFunction, (s)->null);
	}
}
