package view.repository;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class FXGrid extends GridPane implements IGridLayout, IFXLayout, FXComponent {
	@Override
	public void addUIComponent(IUIComponent c, int row, int col, int hSize, int vSize) {
		this.add((Node) c.getComponent(), row, col, hSize, vSize);
	}
	
	@Override
	public void setHSpacing(double space) {
		super.setHgap(space);
	}
	
	@Override
	public void setVSpacing(double space) {
		super.setVgap(space);
	}
}
