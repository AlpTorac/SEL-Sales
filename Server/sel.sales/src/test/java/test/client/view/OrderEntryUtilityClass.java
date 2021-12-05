package test.client.view;

import java.math.BigDecimal;
import java.util.Collection;

import org.junit.jupiter.api.Assertions;

import client.view.composites.MenuItemEntry;
import client.view.composites.OrderEntry;
import model.dish.DishMenuItemData;
import model.entity.AccumulatingAggregateEntry;
import model.order.OrderData;

public class OrderEntryUtilityClass {
	public static void assertOrderEntryDisplayEquals(OrderEntry entry, OrderData orderData) {
		Collection<MenuItemEntry> col = entry.cloneMenuItemEntries();
		
		for (AccumulatingAggregateEntry<DishMenuItemData> oid : orderData.getOrderedItems()) {
			DishMenuItemData item = oid.getItem();
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
