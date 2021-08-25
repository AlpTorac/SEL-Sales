package view.repository;

public interface IGridLayout extends ILayout {
	default public void setHSpacing(double space) {
		((IGridLayout) this.getComponent()).setHSpacing(space);
	}
	
	default public void setVSpacing(double space) {
		((IGridLayout) this.getComponent()).setVSpacing(space);
	}
	
	default public void addUIComponent(IUIComponent c, int row, int col, int hSize, int vSize) {
		((IGridLayout) this.getComponent()).addUIComponent(c, row, col, hSize, vSize);
	}
}
