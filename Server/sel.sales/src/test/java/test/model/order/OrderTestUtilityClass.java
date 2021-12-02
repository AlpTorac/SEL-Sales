package test.model.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;

import model.order.IOrderData;
import model.order.AccumulatingOrderItemAggregate;

public final class OrderTestUtilityClass {
	public static void assertOrderDataEqual(IOrderData data, BigDecimal[] amounts, String[] ids) {
		AccumulatingOrderItemAggregate[] itemData = data.getOrderedItems();
		for (int i = 0; i < itemData.length; i++) {
			Assertions.assertEquals(itemData[i].getAmount().compareTo(amounts[i]), 0);
			assertOrderItemIDEqual(itemData[i], ids[i]);
		}
	}
	
	public static void assertOrderItemIDEqual(AccumulatingOrderItemAggregate data, String id) {
		data.getItemData().getID().serialisedIDequals(id);
	}
	
	public static void assertOrderItemEqual(AccumulatingOrderItemAggregate data, String id, BigDecimal amount) {
		assertOrderItemIDEqual(data, id);
		Assertions.assertEquals(data.getAmount().compareTo(amount), 0);
	}
	
	public static void assertDatesEqual(LocalDateTime date1, LocalDateTime date2) {
		Assertions.assertEquals(date1, date2);
//		Assertions.assertEquals(date1.get(GregorianCalendar.MONTH), date2.get(GregorianCalendar.MONTH));
//		Assertions.assertEquals(date1.get(GregorianCalendar.DAY_OF_MONTH), date2.get(GregorianCalendar.DAY_OF_MONTH));
//		Assertions.assertEquals(date1.get(GregorianCalendar.HOUR_OF_DAY), date2.get(GregorianCalendar.HOUR_OF_DAY));
//		Assertions.assertEquals(date1.get(GregorianCalendar.MINUTE), date2.get(GregorianCalendar.MINUTE));
//		Assertions.assertEquals(date1.get(GregorianCalendar.SECOND), date2.get(GregorianCalendar.SECOND));
	}
	
	public static void assertOrderDataEqual(IOrderData order, String id, LocalDateTime date, boolean isCash, boolean isHere) {
		Assertions.assertEquals(id, order.getID().toString());
		assertDatesEqual(order.getDate(), date);
		Assertions.assertEquals(order.getIsCash(), isCash);
		Assertions.assertEquals(order.getIsHere(), isHere);
	}
	public static void assertOrderDataEqual(IOrderData order, String id, LocalDateTime date, boolean isCash, boolean isHere, BigDecimal orderDiscount) {
		Assertions.assertEquals(id, order.getID().toString());
		assertDatesEqual(order.getDate(), date);
		Assertions.assertEquals(order.getIsCash(), isCash);
		Assertions.assertEquals(order.getIsHere(), isHere);
		Assertions.assertEquals(order.getOrderDiscount().compareTo(orderDiscount), 0);
	}
	
	public static void assertOrderDatasEqual(IOrderData orderData1, IOrderData orderData2) {
		assertOrderDataEqual(orderData1, orderData2.getID().toString(), orderData2.getDate(), orderData2.getIsCash(), orderData2.getIsHere());
	}
	
}
