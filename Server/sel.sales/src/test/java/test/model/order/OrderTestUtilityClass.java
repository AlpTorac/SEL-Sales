package test.model.order;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.Assertions;

import model.order.IOrderData;
import model.order.IOrderItemData;

public final class OrderTestUtilityClass {
	public static void assertOrderDataEqual(IOrderData data, BigDecimal[] amounts, String[] ids) {
		IOrderItemData[] itemData = data.getOrderedItems();
		for (int i = 0; i < itemData.length; i++) {
			Assertions.assertEquals(itemData[i].getAmount().compareTo(amounts[i]), 0);
			assertOrderItemDataEqual(itemData[i], ids[i]);
		}
	}
	
	public static void assertOrderItemDataEqual(IOrderItemData data, String id) {
		Assertions.assertEquals(data.getItemData().getId().getID(), id);
	}
	
	public static void assertDatesEqual(Calendar date1, Calendar date2) {
		Assertions.assertEquals(date1.get(GregorianCalendar.YEAR), date2.get(GregorianCalendar.YEAR));
		Assertions.assertEquals(date1.get(GregorianCalendar.MONTH), date2.get(GregorianCalendar.MONTH));
		Assertions.assertEquals(date1.get(GregorianCalendar.DAY_OF_MONTH), date2.get(GregorianCalendar.DAY_OF_MONTH));
		Assertions.assertEquals(date1.get(GregorianCalendar.HOUR_OF_DAY), date2.get(GregorianCalendar.HOUR_OF_DAY));
		Assertions.assertEquals(date1.get(GregorianCalendar.MINUTE), date2.get(GregorianCalendar.MINUTE));
		Assertions.assertEquals(date1.get(GregorianCalendar.SECOND), date2.get(GregorianCalendar.SECOND));
	}
	
	public static void assertOrderDataEqual(IOrderData order, String id, Calendar date, boolean cashOrCard, boolean hereOrToGo) {
		Assertions.assertEquals(id, order.getID().getID());
		assertDatesEqual(order.getDate(), date);
		Assertions.assertEquals(order.getCashOrCard(), cashOrCard);
		Assertions.assertEquals(order.getHereOrToGo(), hereOrToGo);
	}
}
