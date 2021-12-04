package test.model.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;

import model.order.OrderData;
import model.dish.DishMenuItemData;
import model.entity.AccumulatingAggregateEntry;

public final class OrderTestUtilityClass {
	public static void assertOrderDataEqual(OrderData data, BigDecimal[] amounts, String[] ids) {
		AccumulatingAggregateEntry<DishMenuItemData>[] itemData = data.getOrderedItems();
		for (int i = 0; i < itemData.length; i++) {
			Assertions.assertEquals(itemData[i].getAmount().compareTo(amounts[i]), 0);
			assertOrderItemIDEqual(itemData[i], ids[i]);
		}
	}
	
	public static void assertOrderItemIDEqual(AccumulatingAggregateEntry<DishMenuItemData> data, String id) {
		Assertions.assertEquals(data.getItem().getID().toString(), id);
	}
	
	public static void assertOrderItemEqual(AccumulatingAggregateEntry<DishMenuItemData> data, String id, BigDecimal amount) {
		assertOrderItemIDEqual(data, id);
		Assertions.assertEquals(data.getAmount().compareTo(amount), 0);
	}
	
	public static void assertDatesEqual(LocalDateTime date1, LocalDateTime date2) {
		Assertions.assertEquals(date1, date2);
	}
	
	public static void assertOrderDataEqual(OrderData order, String id, LocalDateTime date, boolean isCash, boolean isHere) {
		Assertions.assertEquals(id, order.getID().toString());
		assertDatesEqual(order.getDate(), date);
		Assertions.assertEquals(order.getIsCash(), isCash);
		Assertions.assertEquals(order.getIsHere(), isHere);
	}
	public static void assertOrderDataEqual(OrderData order, String id, LocalDateTime date, boolean isCash, boolean isHere, BigDecimal orderDiscount) {
		Assertions.assertEquals(id, order.getID().toString());
		assertDatesEqual(order.getDate(), date);
		Assertions.assertEquals(order.getIsCash(), isCash);
		Assertions.assertEquals(order.getIsHere(), isHere);
		Assertions.assertEquals(order.getOrderDiscount().compareTo(orderDiscount), 0);
	}
	
	public static void assertOrderDatasEqual(OrderData orderData1, OrderData orderData2) {
		assertOrderDataEqual(orderData1, orderData2.getID().toString(), orderData2.getDate(), orderData2.getIsCash(), orderData2.getIsHere());
	}
	
}
