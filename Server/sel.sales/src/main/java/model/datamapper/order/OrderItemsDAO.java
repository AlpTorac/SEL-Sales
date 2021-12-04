package model.datamapper.order;

import model.dish.DishMenuItemData;
import model.entity.AccumulatingAggregateEntry;
import model.order.AccumulatingOrderItemAggregate;

public class OrderItemsDAO extends OrderAttributeDAO {
	@Override
	protected String serialiseNotNullValue(Object attributeValue) {
		AccumulatingOrderItemAggregate aggr = (AccumulatingOrderItemAggregate) attributeValue;
		String result = "";
		for (AccumulatingAggregateEntry<DishMenuItemData> e : aggr.getAllEntries()) {
			result += this.getAggregateFormat().format(e.getItem().getID().toString(), this.serialiseBigDecimal(e.getAmount()));
		}
		return result;
	}

	@Override
	protected AccumulatingOrderItemAggregate parseNotNullSerialisedValue(String serialisedValue) {
		AccumulatingOrderItemAggregate aggr = new AccumulatingOrderItemAggregate();
		String[] entries = this.getAggregateFormat().getMatches(serialisedValue).toArray(String[]::new);
		for (String e : entries) {
			if (e != null) {
				String[] fields = this.getAggregateFormat().getFields(e);
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
