package model.datamapper.order;

import java.time.LocalDateTime;

import model.datamapper.IAttribute;
import model.order.AccumulatingOrderItemAggregate;
import model.order.OrderStatus;

public enum OrderAttribute implements IAttribute {
	ORDER_ITEMS("items", new AccumulatingOrderItemAggregate()),
	IS_HERE("isHere", Boolean.valueOf(true)),
	IS_CASH("isCash", Boolean.valueOf(true)),
	DATE("date", LocalDateTime.MIN),
	STATUS("status", OrderStatus.UNDEFINED),
	IS_WRITTEN("isWritten", Boolean.valueOf(false)),
	TABLE_NUMBER("tableNumber", Integer.valueOf(-1)),
	NOTE("note", "");
	
	private String description;
	private Object defaultValue;
	
	private OrderAttribute(String description, Object defaultValue) {
		this.description = description;
		this.defaultValue = defaultValue;
	}
	
	@Override
	public String getDescription() {
		return this.description;
	}
	
	@Override
	public Object getDefaultValue() {
		return this.defaultValue;
	}
	
	@Override
	public IAttribute[] getAllAttributes() {
		return OrderAttribute.values();
	}

}