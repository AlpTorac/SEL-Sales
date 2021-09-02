package model.order;

public interface IOrderItemFactory {
	IOrderItem createOrderItem(IOrderItemData item);
}
