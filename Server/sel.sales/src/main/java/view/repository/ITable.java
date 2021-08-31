package view.repository;

public interface ITable<T> extends IUIComponent, Attachable, ISizable, IDataCollectingUIComponent<T> {
	default public <O> void addColumn(String title, String fieldName) {
		((ITable<?>) this.getComponent()).addColumn(title, fieldName);
	}
}
