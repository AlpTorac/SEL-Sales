package model.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public interface IOrderData {
	IOrderItemData[] getOrderedItems();
	LocalDateTime getDate();

	boolean getIsCash();
	boolean getIsHere();
	boolean getIsDiscounted();
	String getID();
	BigDecimal getGrossSum();
	BigDecimal getOrderDiscount();
	Collection<IOrderItemData> getOrderItems();
	default BigDecimal getNetSum() {
		return this.getGrossSum().subtract(this.getOrderDiscount());
	}
	default Collection<IOrderItemData> combineData(IOrderData data) {
//		Map<String, IOrderItemData> result = new HashMap<String, IOrderItemData>();
//		Collection<IOrderItemData> orderItems = new ArrayList<IOrderItemData>();
//		for (IOrderItemData oid : this.getOrderedItems()) {
//			orderItems.add(oid);
//		}
//		orderItems.forEach(i -> result.put(i.getItemData().getId(), i));
//		IOrderItemData inResult;
//		for (IOrderItemData oid : data.getOrderedItems()) {
//			inResult = result.get(oid.getItemData().getId());
//			if (inResult != null) {
//				result.put(oid.getItemData().getId(), inResult.combine(oid));
//			} else {
//				result.put(oid.getItemData().getId(), oid);
//			}
//		}
//		return result.values();
		Collection<IOrderItemData> orderItems = new ArrayList<IOrderItemData>();
		for (IOrderItemData oid : this.getOrderedItems()) {
			orderItems.add(oid);
		}
		for (IOrderItemData oid : data.getOrderedItems()) {
			orderItems.add(oid);
		}
		Map<String, IOrderItemData> newOrderItems = new HashMap<String, IOrderItemData>();
		orderItems.forEach(oid -> {
			String id;
			if (newOrderItems.containsKey(id = oid.getItemData().getId())) {
				newOrderItems.put(id, newOrderItems.get(id).combine(oid));
			} else {
				newOrderItems.put(id, oid);
			}
		});
		return newOrderItems.values();
	}
	IOrderData combine(IOrderData data);
	String toString();
	boolean equals(Object o);
}
