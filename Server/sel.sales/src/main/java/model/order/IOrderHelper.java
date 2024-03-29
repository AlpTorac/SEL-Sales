package model.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemFinder;
import model.filewriter.FileOrderSerialiser;
import model.id.EntityID;
import model.order.serialise.IOrderDeserialiser;
import model.order.serialise.IntraAppOrderSerialiser;

public interface IOrderHelper {
	void setFinder(IDishMenuItemFinder finder);
	void setOrderCollectorFactory(IOrderCollectorFactory fac);
	
	void setOrderFactory(IOrderFactory fac);
	void setOrderDataFactory(IOrderDataFactory fac);
	
	void setOrderItemDataFactory(IOrderItemDataFactory fac);
	
	void setOrderDeserialiser(IOrderDeserialiser deserialiser);
	
	void setAppSerialiser(IntraAppOrderSerialiser appOrderSerialiser);
	void setFileSerialiser(FileOrderSerialiser fileOrderSerialiser);
	
	String serialiseForApp(IOrderItemData[] orderData, LocalDateTime date, boolean isCash, boolean isHere, Object... idParameters);
	String serialiseForApp(IOrderItemData[] orderData, LocalDateTime date, boolean isCash, boolean isHere, EntityID orderID);
	String serialiseForApp(IOrderItemData[] orderData, LocalDateTime date, boolean isCash, boolean isHere);
	String serialiseForApp(IOrderData data);
	String serialiseForApp(IOrderData[] data);
	String serialiseForFile(IOrderData[] data);
	String serialiseForFile(IOrderData data);
	
	IOrderCollector createOrderCollector();
	IOrder createOrder(String serialisedOrder);
	IOrderData orderToData(IOrder order);
	IOrderData[] deserialiseOrderDatas(String serialisedOrders);
	IOrderData deserialiseOrderData(String serialisedOrder);
	IOrderItemData createOrderItemData(IDishMenuItemData menuItem, BigDecimal amount);
	String formatDate(LocalDateTime ldt);
}
