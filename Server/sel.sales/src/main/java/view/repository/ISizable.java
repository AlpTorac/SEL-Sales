package view.repository;

public interface ISizable extends IUIComponent {
	default public void setLayoutX(double x) {
		((ISizable) this.getComponent()).setLayoutX(x);
	}
	default public void setLayoutY(double y) {
		((ISizable) this.getComponent()).setLayoutY(y);
	}
	default public void setPrefWidth(double width) {
		((ISizable) this.getComponent()).setPrefWidth(width);
	}
	default public void setPrefHeight(double height) {
		((ISizable) this.getComponent()).setPrefHeight(height);
	}
	default public void setPrefSize(double width, double height) {
		this.setPrefWidth(width);
		this.setPrefHeight(height);
	}
	default public void setLayoutPos(double x, double y) {
		this.setLayoutX(x);
		this.setLayoutY(y);
	}
}
