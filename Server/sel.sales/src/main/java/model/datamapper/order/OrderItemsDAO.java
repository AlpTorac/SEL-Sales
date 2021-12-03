package model.datamapper.order;

import model.dish.DishMenuItemData;
import model.entity.AccumulatingAggregateEntry;
import model.order.AccumulatingOrderItemAggregate;

public class OrderItemsDAO extends OrderAttributeDAO {
	protected OrderItemsDAO(String fileAddress, String defaultFileName) {
		super(fileAddress, defaultFileName);
	}

	@Override
	protected String serialiseNotNullValue(Object attributeValue) {
		AccumulatingOrderItemAggregate aggr = (AccumulatingOrderItemAggregate) attributeValue;
		String result = "";
		for (AccumulatingAggregateEntry<DishMenuItemData> e : aggr.getAllEntries()) {
			result += e.getItem().getID().toString();
			result += this.getAggregateFormat().getFieldSeparator();
			result += this.serialiseBigDecimal(e.getAmount());
			result += this.getAggregateFormat().getEntrySeparator();
		}
		return result;
	}

	@Override
	protected AccumulatingOrderItemAggregate parseNotNullSerialisedValue(String serialisedValue) {
		AccumulatingOrderItemAggregate aggr = new AccumulatingOrderItemAggregate();
		String[] entries = serialisedValue.split(this.getAggregateFormat().getEntrySeparator());
		for (String e : entries) {
			if (e != null) {
				String[] fields = e.split(this.getAggregateFormat().getFieldSeparator());
				if (fields.length > 1) {
					//fields[0] = id, fields[1] = amount
					aggr.addElement(this.parseID(fields[0]), this.parseBigDecimal(fields[1]));
				}
			}
		}
		return aggr;
	}

	@Override
	protected OrderAttribute getAssociatedAttribute() {
		return OrderAttribute.ORDER_ITEMS;
	}

}
