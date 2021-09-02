package model;

public interface Updatable {
	void subscribe();
	void refreshMenu();
	void refreshUnconfirmedOrders();
	void refreshConfirmedOrders();
}