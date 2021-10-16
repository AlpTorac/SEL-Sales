package view.repository;

public interface ISizable extends Attachable {
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
	default public void setMinWidth(double width) {
		((ISizable) this.getComponent()).setMinWidth(width);
	}
	default public void setMinHeight(double height) {
		((ISizable) this.getComponent()).setMinHeight(height);
	}
	default public void setMinSize(double width, double height) {
		this.setMinWidth(width);
		this.setMinHeight(height);
	}
	default public void setLayoutPos(double x, double y) {
		this.setLayoutX(x);
		this.setLayoutY(y);
	}
}
