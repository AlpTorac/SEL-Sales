package model.order.datamapper;

import model.order.IOrderCollector;
import model.order.IOrderData;
import model.order.serialise.OrderAttributeFormat;
import model.util.ISerialiser;

public abstract class OrderAttributeGetter implements ISerialiser {
	private OrderAttributeFormat format = new OrderAttributeFormat();
	
	public String serialiseAll(IOrderCollector orderCollector) {
		String result = "";
		result += this.getFormat().getFileStart();
		String currentID;
		for (IOrderData data : orderCollector.getAllOrders()) {
			currentID = data.getID().toString();
			
//			result += this.getFormat().getFieldStart();
//			result += currentID;
//			result += this.getFormat().getFieldSeparator();
//			result += this.serialiseOrderAttribute(orderCollector, currentID);
//			result += this.getFormat().getFieldEnd();
			result += this.serialiseFor(orderCollector, currentID);
		}
		result += this.getFormat().getFileEnd();
		return result;
	}
	
	public String serialiseFor(IOrderCollector orderCollector, String orderID) {
		String result = "";
		result += this.getFormat().getFieldStart();
		result += orderID;
		result += this.getFormat().getFieldSeparator();
		result += this.serialiseOrderAttribute(orderCollector, orderID);
		result += this.getFormat().getFieldEnd();
		return result;
	}
	
	protected String serialiseOrderAttribute(IOrderCollector orderCollector, String orderID) {
		return this.serialiseValue(orderCollector.getOrderAttribute(orderID, this.getAssociatedOrderAttribute()));
	}
	
	protected abstract String serialiseValue(Object attributeValue);
	protected abstract OrderAttribute getAssociatedOrderAttribute();
	
	protected OrderAttributeFormat getFormat() {
		return this.format;
	}
}
