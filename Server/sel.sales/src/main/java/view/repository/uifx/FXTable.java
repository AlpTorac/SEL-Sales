package view.repository.uifx;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import view.repository.ITable;
import view.repository.uiwrapper.ClickEventListener;

public class FXTable<T> extends TableView<T> implements FXAttachable, ITable<T> {
	
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

	@Override
	public <O> void addColumn(String title, String fieldName) {
		TableColumn<T, O> col = new TableColumn<>(title);
//		int index = this.getColumns().size();
//		col.setCellValueFactory((p)->{
//	        String[] x = p.getValue();
//	        return new SimpleStringProperty(x != null && x.length>0 ? x[index] : "<no name>");
//	});
		col.setCellValueFactory(new PropertyValueFactory<T, O>(fieldName));
		super.getColumns().add(col);
	}

	@Override
	public void addItem(T item) {
		super.getItems().add(item);
	}
	
	@Override
	public void clear() {
		super.getItems().clear();
	}
	
	@Override
	public void removeItem(T item) {
		super.getItems().remove(item);
	}
}
