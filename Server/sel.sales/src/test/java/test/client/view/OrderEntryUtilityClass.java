package test.client.view;

import java.math.BigDecimal;
import java.util.Collection;

import org.junit.jupiter.api.Assertions;

import client.view.composites.MenuItemEntry;
import client.view.composites.OrderEntry;
import model.dish.IDishMenuItemData;
import model.order.IOrderData;
import model.order.IOrderItemData;

public class OrderEntryUtilityClass {
	public static void assertOrderEntryDisplayEquals(OrderEntry entry, IOrderData orderData) {
		Collection<MenuItemEntry> col = entry.cloneMenuItemEntries();
		
		for (IOrderItemData oid : orderData.getOrderedItems()) {
			IDishMenuItemData item = oid.getItemData();
			BigDecimal amount = oid.getAmount();
			Assertions.assertEquals(col.stream().filter(mie -> mie.getSelectedMenuItem().equals(item)).count(), 1);
			col.stream().filter(mie -> mie.getSelectedMenuItem().equals(item))
			.forEach(mie -> {
				Assertions.assertEquals(mie.getMenuItemChoiceBox().getSelectedElement(), item);
				Assertions.assertEquals(mie.getSelectedMenuItem(), item);
				Assertions.assertEquals(Integer.valueOf(mie.getAmountTextBox().getText()), amount.intValue());
				Assertions.assertEquals(mie.getAmount().intValue(), amount.intValue());
				Assertions.assertEquals(mie.getPrice().doubleValue(), item.getGrossPrice().multiply(amount).doubleValue());
			});
		}
		
		Assertions.assertEquals(entry.getNetPrice().doubleValue(), orderData.getNetSum().doubleValue());
	}
}
