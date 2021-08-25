package view.repository;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class FXTable extends TableView implements FXAttachable, ITable {
	
	@SuppressWarnings("unchecked")
	FXTable() {
		super();
		super.setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
	}
	
	@Override
	public void addClickListener(ClickEventListener l) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeClickListener(ClickEventListener l) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addColumn(String o) {
		TableColumn col = new TableColumn(o);
		this.getColumns().add(col);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addItem(Object item) {
		this.getItems().add(item);
	}
}
