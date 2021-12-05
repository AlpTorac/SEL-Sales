package model.datamapper.order;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import model.DateSettings;
import model.datamapper.AttributeDAO;
import model.datamapper.EntityDAO;
import model.datamapper.IAttribute;
import model.dish.IDishMenuItemFinder;
import model.entity.IFactory;
import model.order.Order;
import model.order.OrderCollector;
import model.order.OrderData;
import model.order.OrderFactory;

public class OrderDAO extends EntityDAO<OrderAttribute, Order, OrderData, OrderCollector> {
	private IDishMenuItemFinder finder;
	
	public OrderDAO(String address) {
		super(address);
	}
	
	public void setFinder(IDishMenuItemFinder finder) {
		this.finder = finder;
	}
	
	public AttributeDAO<OrderAttribute, Order, OrderData, OrderCollector> getDAO(String serialisedDesc) {
		return this.getDAOMap().get(IAttribute.parseAttribute(OrderAttribute.values(), serialisedDesc));
	}
	
	protected void fillDAOList() {
		this.getDAOMap().put(OrderAttribute.ORDER_ITEMS,new OrderItemsDAO());
		this.getDAOMap().put(OrderAttribute.DATE,new OrderDateDAO(new DateSettings()));
		this.getDAOMap().put(OrderAttribute.IS_CASH,new OrderIsCashDAO());
		this.getDAOMap().put(OrderAttribute.IS_HERE,new OrderIsHereDAO());
		this.getDAOMap().put(OrderAttribute.IS_WRITTEN,new OrderIsWrittenDAO());
		this.getDAOMap().put(OrderAttribute.NOTE,new OrderNoteDAO());
		this.getDAOMap().put(OrderAttribute.STATUS,new OrderStatusDAO());
		this.getDAOMap().put(OrderAttribute.TABLE_NUMBER,new OrderTableNumberDAO());
	}

	@Override
	protected Map<OrderAttribute, AttributeDAO<OrderAttribute, Order, OrderData, OrderCollector>> initDAOMap() {
		return new ConcurrentSkipListMap<OrderAttribute, AttributeDAO<OrderAttribute, Order, OrderData, OrderCollector>>();
	}

	@Override
	protected IFactory<OrderAttribute, Order, OrderData> initFactory() {
		return new OrderFactory();
	}
	
	@Override
	public OrderData parseValueObject(String serialisedOrder) {
		OrderData od = super.parseValueObject(serialisedOrder);
		od.setFinder(finder);
		return od;
	}
}
