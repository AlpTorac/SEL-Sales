package model;

public interface OrderUpdatable extends Updatable{
	void refreshUnconfirmedOrders();
	void refreshConfirmedOrders();
}