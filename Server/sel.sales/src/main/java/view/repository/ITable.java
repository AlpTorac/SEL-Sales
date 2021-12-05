package view.repository;

import java.util.function.Function;

public interface ITable<T> extends IDataCollectingUIComponent<T> {
	default public <O> void addColumn(String title, String fieldName) {
		((ITable<?>) this.getComponent()).addColumn(title, fieldName);
	}
	
	/**
	 * The function f is used to compute what will be displayed, namely
	 * {@code (result of f).toString()} will be displayed.
	 * @param <T> Input type (the element type stored in the table)
	 * @param <O> Output type
	 */
	@SuppressWarnings("unchecked")
	default public <O> void addColumn(String title, Function<T, O> f) {
		((ITable<T>) this.getComponent()).addColumn(title, f);
	}
}
