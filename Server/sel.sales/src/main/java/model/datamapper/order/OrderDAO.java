package model.datamapper.order;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import model.DateSettings;
import model.datamapper.AttributeFormat;
import model.entity.id.EntityIDFactory;
import model.entity.id.MinimalIDFactory;
import model.order.OrderData;
import model.order.OrderFactory;

public class OrderDAO {
	private AttributeFormat attributeFormat = new AttributeFormat();
	private EntityIDFactory idFac = new MinimalIDFactory();
	private OrderFactory orderFac = new OrderFactory();
	
	private String address;
	private Map<OrderAttribute, OrderAttributeDAO> list;
	
	public OrderDAO(String address) {
		this.address = address;
		this.list = this.initDAOMap();
	}
	
	protected Map<OrderAttribute, OrderAttributeDAO> getDAOMap() {
		return this.list;
	}
	
	/**
	 * Make sure that the map sorts the elements, so that the order of the maps stays the same.
	 * This way, the format of the serialised orders stays the same
	 */
	protected Map<OrderAttribute, OrderAttributeDAO> initDAOMap() {
		return new ConcurrentSkipListMap<OrderAttribute, OrderAttributeDAO>();
	}
	
	protected String getAddress() {
		return this.address;
	}
	
	protected void fillDAOList() {
		this.getDAOMap().put(OrderAttribute.ORDER_ITEMS,new OrderItemsDAO(this.getAddress(), "orderItems"));
		this.getDAOMap().put(OrderAttribute.DATE,new OrderDateDAO(this.getAddress(), "orderDate", new DateSettings()));
		this.getDAOMap().put(OrderAttribute.IS_CASH,new OrderIsCashDAO(this.getAddress(), "orderIsCash"));
		this.getDAOMap().put(OrderAttribute.IS_HERE,new OrderIsHereDAO(this.getAddress(), "orderIsHere"));
		this.getDAOMap().put(OrderAttribute.IS_WRITTEN,new OrderIsWrittenDAO(this.getAddress(), "orderIsWritten"));
		this.getDAOMap().put(OrderAttribute.NOTE,new OrderNoteDAO(this.getAddress(), "orderNote"));
		this.getDAOMap().put(OrderAttribute.STATUS,new OrderStatusDAO(this.getAddress(), "orderStatus"));
		this.getDAOMap().put(OrderAttribute.TABLE_NUMBER,new OrderTableNumberDAO(this.getAddress(), "orderTableNumber"));
	}
	
	public OrderAttributeDAO getDAO(OrderAttribute oa) {
		return this.getDAOMap().get(oa);
	}
	
	public String serialiseOrder(OrderData data) {
		String result = "";
		if (data != null) {
			result += data.getID().toString();
			result += this.attributeFormat.getFieldSeparator();
			for (OrderAttributeDAO dao : this.getDAOMap().values()) {
				result += dao.serialiseAttribute(data);
				result += this.attributeFormat.getFieldSeparator();
			}
			result = result.substring(0, result.length() - this.attributeFormat.getFieldSeparator().length());
		}
		return result;
	}
	
	public OrderData parseOrder(String serialisedOrder) {
		if (serialisedOrder != null) {
			String[] fields = serialisedOrder.split(this.attributeFormat.getFieldSeparator());
			if (fields.length > 0) {
				OrderData data = this.orderFac.constructMinimalData(this.idFac.createID(fields[0]));
				int i = 1;
				Iterator<OrderAttributeDAO> it = this.getDAOMap().values().iterator();
				while (it.hasNext()) {
					it.next().setAttributeAlgorithm(data, fields[i]);
					i++;
				}
				return data;
			}
		}
		return null;
	}
}
