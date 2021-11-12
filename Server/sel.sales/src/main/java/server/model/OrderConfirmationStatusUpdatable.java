package server.model;

import model.OrderUpdatable;

public interface OrderConfirmationStatusUpdatable extends OrderUpdatable {
	default void refreshOrders() {
		this.refreshUnconfirmedOrders();
		this.refreshConfirmedOrders();
	}
	void refreshUnconfirmedOrders();
	void refreshConfirmedOrders();
	void refreshConfirmMode();
}