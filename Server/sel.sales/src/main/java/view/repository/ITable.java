package view.repository;

public interface ITable<T> extends IDataCollectingUIComponent<T> {
	default public <O> void addColumn(String title, String fieldName) {
		((ITable<?>) this.getComponent()).addColumn(title, fieldName);
	}
	default public <O> void addColumn(String title) {
		((ITable<?>) this.getComponent()).addColumn(title);
	}
}
