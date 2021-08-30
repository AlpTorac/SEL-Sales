package view.repository;

public interface IListView<T> extends Attachable, IEventShooterOnClickUIComponent {
	@SuppressWarnings("unchecked")
	default public void addItem(T item) {
		((IListView<T>) this.getComponent()).addItem(item);
	}
	
	default public void addItems(T[] item) {
		for (T o : item) {
			this.addItem(o);
		}
	}
	
	@SuppressWarnings("unchecked")
	default public void addItemIfNotPresent(T item) {
		((IListView<T>) this.getComponent()).addItemIfNotPresent(item);
	}
	
	default public void addItemsIfNotPresent(T[] item) {
		for (T o : item) {
			this.addItemIfNotPresent(o);
		}
	}
	
	default public void clear() {
		((IListView<?>) this.getComponent()).clear();
	}
	
	@SuppressWarnings("unchecked")
	default public void removeItem(T item) {
		((IListView<T>) this.getComponent()).removeItem(item);
	}
	
	@SuppressWarnings("unchecked")
	default public int getSize() {
		return ((IListView<T>) this.getComponent()).getSize();
	}
}
