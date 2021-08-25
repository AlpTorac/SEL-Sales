package view.repository;

public interface ILayout extends IUIComponent, ISizable {
	default public void setMarigins(double top, double right, double bottom, double left) {
		((ILayout) this.getComponent()).setMarigins(top, right, bottom, left);
	}
}
