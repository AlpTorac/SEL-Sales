package view.repository;

public interface ITable extends IUIComponent, IEventShooterUIComponent, Attachable, ISizable {
	default public void addColumn(String title) {
		((ITable) this.getComponent()).addColumn(title);
	}
	default public void addColumns(String[] titles) {
		for (String t : titles) {
			this.addColumn(t);
		}
	}
	default public void addItem(Object item) {
		((ITable) this.getComponent()).addItem(item);
	}
	default public void addItem(Object[] item) {
		for (Object o : item) {
			this.addItem(o);
		}
	}
}
