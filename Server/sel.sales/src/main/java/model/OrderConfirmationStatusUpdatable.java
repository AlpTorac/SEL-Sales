package model;

public interface OrderConfirmationStatusUpdatable extends Updatable {
	void refreshUnconfirmedOrders();
	void refreshConfirmedOrders();
	void refreshConfirmMode();
}