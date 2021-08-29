package view.repository;

public interface ITable<T> extends IUIComponent, Attachable, ISizable {
	default public <O> void addColumn(String title, String fieldName) {
		((ITable<?>) this.getComponent()).addColumn(title, fieldName);
	}
	
	@SuppressWarnings("unchecked")
	default public void addItem(T item) {
		((ITable<T>) this.getComponent()).addItem(item);
	}
	
	default public void addItems(T[] item) {
		for (T o : item) {
			this.addItem(o);
		}
	}
	
	default public void clear() {
		((ITable<?>) this.getComponent()).clear();
	}
	
	@SuppressWarnings("unchecked")
	default public void removeItem(T item) {
		((ITable<T>) this.getComponent()).removeItem(item);
	}
}
