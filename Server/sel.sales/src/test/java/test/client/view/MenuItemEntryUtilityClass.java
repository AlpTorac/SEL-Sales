package test.client.view;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;

import client.view.composites.MenuItemEntry;
import model.dish.IDishMenuItemData;

public class MenuItemEntryUtilityClass {
	public static void assertMenuItemEntryEquals(MenuItemEntry entry, IDishMenuItemData item, BigDecimal amount) {
		Assertions.assertEquals(entry.getMenuItemChoiceBox().getSelectedElement(), item);
		Assertions.assertEquals(entry.getSelectedMenuItem(), item);
		Assertions.assertEquals(Integer.valueOf(entry.getAmountTextBox().getText()), amount.intValue());
		Assertions.assertEquals(entry.getAmount().intValue(), amount.intValue());
		Assertions.assertEquals(entry.getPrice().doubleValue(), item.getGrossPrice().multiply(amount).doubleValue());
	}
}
