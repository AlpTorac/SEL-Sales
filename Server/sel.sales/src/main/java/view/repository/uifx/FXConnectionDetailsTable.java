package view.repository.uifx;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;
import view.repository.IConnectionDetailsTable;

public class FXConnectionDetailsTable<T> extends FXTable<T> implements IConnectionDetailsTable<T> {
	FXConnectionDetailsTable(String isAllowedColumnTitle, String isAllowedColumnFieldName, String connStatusColumnTitle, String connStatusColumnFieldName) {
		super();
		this.init(isAllowedColumnTitle, isAllowedColumnFieldName, connStatusColumnTitle, connStatusColumnFieldName);
	}
	
	protected void init(String isAllowedColumnTitle, String isAllowedColumnFieldName, String connStatusColumnTitle, String connStatusColumnFieldName) {
		this.initIsAllowedCol(isAllowedColumnTitle, isAllowedColumnFieldName);
		this.initConnStatusCol(connStatusColumnTitle, connStatusColumnFieldName);
	}
	
	private void initIsAllowedCol(String isAllowedColumnTitle, String isAllowedColumnFieldName) {
		TableColumn<T, Boolean> isAllowedCol = this.makePropertyValueColumn(isAllowedColumnTitle, isAllowedColumnFieldName);
		super.getColumns().add(isAllowedCol);
	}
	
	private void initConnStatusCol(String connStatusColumnTitle, String connStatusColumnFieldName) {
		TableColumn<T, Boolean> connStatusCol = this.makePropertyValueColumn(connStatusColumnTitle, connStatusColumnFieldName);
//		connStatusCol.setCellFactory(initConnStatusColCellFactory());
		super.getColumns().add(connStatusCol);
	}
	
//	private Callback<TableColumn<T, Boolean>, TableCell<T, Boolean>> initConnStatusColCellFactory() {
//		return new Callback<TableColumn<T, Boolean>, TableCell<T, Boolean>>() {
//			@Override
//			public TableCell<T, Boolean> call(TableColumn<T, Boolean> param) {
//				return makeConnStatusTableCell();
//			}
//		};
//	}
//	
//	private TableCell<T, Boolean> makeConnStatusTableCell() {
//		return new TableCell<T, Boolean>() {
//            @Override
//            public void updateItem(Boolean item, boolean empty) {
//                super.updateItem(item, empty);
//                if (!empty) {
//                	if (item.booleanValue()) {
//                		setStyle("-fx-background-color: green");
//                	} else {
//                		setStyle("-fx-background-color: red");
//                	}
//                } else {
//                    setGraphic(null);
//                }
//            }
//		};
//	}
	
	protected int getLastAvailableIndex() {
		return this.getColumns().size() - 2;
	}
	
	@Override
	public <O> void addColumn(String title) {
		super.getColumns().add(this.getLastAvailableIndex(), super.makeSimpleValueColumn(title));
	}
	
	@Override
	public <O> void addColumn(String title, String fieldName) {
		super.getColumns().add(this.getLastAvailableIndex(), super.makePropertyValueColumn(title, fieldName));
	}
}
