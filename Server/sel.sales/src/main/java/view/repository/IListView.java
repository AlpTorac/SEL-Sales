package view.repository;

public interface IListView<T> extends Attachable {
	@SuppressWarnings("unchecked")
	default public void addItem(T item) {
		((IListView<T>) this.getComponent()).addItem(item);
	}
	
	default public void addItems(T[] item) {
		for (T o : item) {
			this.addItem(o);
		}
	}
	
	default public void clear() {
		((IListView<?>) this.getComponent()).clear();
	}
	
	@SuppressWarnings("unchecked")
	default public void removeItem(T item) {
		((IListView<T>) this.getComponent()).removeItem(item);
	}
}
