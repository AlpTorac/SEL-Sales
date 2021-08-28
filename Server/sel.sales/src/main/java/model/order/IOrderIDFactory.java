package model.order;

public interface IOrderIDFactory {
	IOrderID createOrderID();
	IOrderID createOrderID(String id);
}
