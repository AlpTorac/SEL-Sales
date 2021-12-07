package test.client.view;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import client.view.composites.EditableMenuItemEntry;
import client.view.composites.MenuItemEntry;
import client.view.composites.PriceUpdateTarget;
import model.dish.DishMenuData;
import model.dish.DishMenuItemData;
import model.entity.AccumulatingAggregateEntry;
import server.model.IServerModel;
import test.FXTestTemplate;
import view.repository.uifx.FXUIComponentFactory;
//@Execution(value = ExecutionMode.SAME_THREAD)
class EditableMenuItemEntryTest extends FXTestTemplate {
	
	private IServerModel model;
	
	private PriceUpdateTarget<MenuItemEntry> u;
	private EditableMenuItemEntry entry;
	
	private boolean isPriceRefreshed;
	private boolean isRemoved;
	
	@BeforeEach
	void prep() {
		model = this.initServerModel();
		this.initDishMenuItems(model);
		this.addDishMenuToServerModel(model);
		
		isPriceRefreshed = false;
		isRemoved = false;
		
		u = new PriceUpdateTarget<MenuItemEntry>() {
			@Override
			public void refreshPrice() {
				isPriceRefreshed = true;
			}

			@Override
			public void remove(MenuItemEntry referenceOfCaller) {
				isRemoved = true;
			}
		};
		entry = new EditableMenuItemEntry(new FXUIComponentFactory(), u);
	}

	@AfterEach
	void cleanUp() {
		this.closeModel(model);
		entry = null;
		u = null;
	}

	@Test
	void orderItemDisplayTest() {
		entry.refreshMenu(model.getMenuData());
		
		BigDecimal amount =  BigDecimal.valueOf(2);
		
		AccumulatingAggregateEntry<DishMenuItemData> itemData = new AccumulatingAggregateEntry<DishMenuItemData>(iData1, amount);
		entry.displayData(itemData);
		
		MenuItemEntryUtilityClass.assertMenuItemEntryEquals(entry, iData1, amount);
		
//		Assertions.assertEquals(entry.getMenuItemChoiceBox().getSelectedElement(), item1);
//		Assertions.assertEquals(entry.getSelectedMenuItem(), item1);
//		Assertions.assertEquals(Integer.valueOf(entry.getAmountTextBox().getText()), amount.intValue());
//		Assertions.assertEquals(entry.getAmount().intValue(), amount.intValue());
//		Assertions.assertEquals(entry.getPrice().doubleValue(), item1.getGrossPrice().multiply(amount).doubleValue());
	}
	
	@Test
	void refreshMenuTest() {
		Assertions.assertNull(entry.getActiveMenu());
		
		DishMenuData menuData = model.getMenuData();
		
		entry.refreshMenu(menuData);
		Assertions.assertEquals(entry.getActiveMenu(), menuData);
		Assertions.assertTrue(isPriceRefreshed);
		
		Assertions.assertEquals(entry.getPrice().intValue(), BigDecimal.ZERO.intValue());
	}
	
	@Test
	void removeTest() {
		entry.getRemoveButton().performArtificialClick();
		Assertions.assertTrue(this.isRemoved);
		Assertions.assertTrue(this.isPriceRefreshed);
	}
	
	@Test
	void selectItemTest() {
		entry.refreshMenu(model.getMenuData());
		
		BigDecimal amount =  BigDecimal.valueOf(1);
		
		Assertions.assertNull(entry.getSelectedMenuItem());
		Assertions.assertEquals(entry.getAmount().intValue(), amount.intValue());
		
		entry.getMenuItemChoiceBox().artificiallySelectItem(iData2);
		
		MenuItemEntryUtilityClass.assertMenuItemEntryEquals(entry, iData2, amount);
		
//		Assertions.assertEquals(entry.getMenuItemChoiceBox().getSelectedElement(), item2);
//		Assertions.assertEquals(entry.getSelectedMenuItem(), item2);
//		Assertions.assertEquals(entry.getPrice().doubleValue(), item2.getGrossPrice().multiply(amount).doubleValue());
		Assertions.assertTrue(isPriceRefreshed);
	}
	
	@Test
	void increaseAmountTest() {
		entry.refreshMenu(model.getMenuData());
		
		BigDecimal amount =  BigDecimal.valueOf(1);
		
		Assertions.assertNull(entry.getSelectedMenuItem());
		Assertions.assertEquals(entry.getAmount().intValue(), amount.intValue());
		
		entry.getMenuItemChoiceBox().artificiallySelectItem(iData2);
		Assertions.assertEquals(entry.getPrice().doubleValue(), iData2.getGrossPrice().multiply(amount).doubleValue());
		
		entry.getAmountIncButton().performArtificialClick();
		amount = amount.add(BigDecimal.ONE);
		
		MenuItemEntryUtilityClass.assertMenuItemEntryEquals(entry, iData2, amount);
		
//		Assertions.assertEquals(Integer.valueOf(entry.getAmountTextBox().getText()), amount.intValue());
//		Assertions.assertEquals(entry.getAmount().intValue(), amount.intValue());
//		Assertions.assertEquals(entry.getPrice().doubleValue(), item2.getGrossPrice().multiply(amount).doubleValue());
		Assertions.assertTrue(isPriceRefreshed);
	}
	
	@Test
	void decreaseAmountTest() {
		entry.refreshMenu(model.getMenuData());
		
		BigDecimal amount =  BigDecimal.valueOf(1);
		
		Assertions.assertNull(entry.getSelectedMenuItem());
		Assertions.assertEquals(entry.getAmount().intValue(), amount.intValue());
		
		entry.getMenuItemChoiceBox().artificiallySelectItem(iData2);
		Assertions.assertEquals(entry.getPrice().doubleValue(), iData2.getGrossPrice().multiply(amount).doubleValue());
		
		// amount = 0
		entry.getAmountDecButton().performArtificialClick();
		amount = amount.subtract(BigDecimal.ONE);
		
		MenuItemEntryUtilityClass.assertMenuItemEntryEquals(entry, iData2, amount);
		
//		Assertions.assertEquals(Integer.valueOf(entry.getAmountTextBox().getText()), amount.intValue());
//		Assertions.assertEquals(entry.getAmount().intValue(), amount.intValue());
//		Assertions.assertEquals(entry.getPrice().doubleValue(), item2.getGrossPrice().multiply(amount).doubleValue());
		Assertions.assertTrue(isPriceRefreshed);
		isPriceRefreshed = false;
		
		// amount still = 0
		entry.getAmountDecButton().performArtificialClick();
		
		MenuItemEntryUtilityClass.assertMenuItemEntryEquals(entry, iData2, amount);
		
//		Assertions.assertEquals(Integer.valueOf(entry.getAmountTextBox().getText()), amount.intValue());
//		Assertions.assertEquals(entry.getAmount().intValue(), amount.intValue());
//		Assertions.assertEquals(entry.getPrice().doubleValue(), item2.getGrossPrice().multiply(amount).doubleValue());
		Assertions.assertFalse(isPriceRefreshed);
	}
	
	@Test
	void cloneTest() {
		entry.refreshMenu(model.getMenuData());
		
		BigDecimal amount =  BigDecimal.valueOf(2);
		
		AccumulatingAggregateEntry<DishMenuItemData> itemData = new AccumulatingAggregateEntry<DishMenuItemData>(iData1, amount);
		entry.displayData(itemData);
		
		EditableMenuItemEntry clone = entry.clone();
		
		Assertions.assertFalse(clone == entry);
		
		MenuItemEntryUtilityClass.assertMenuItemEntryEquals(clone, iData1, amount);
		
//		Assertions.assertEquals(clone.getMenuItemChoiceBox().getSelectedElement(), item1);
//		Assertions.assertEquals(clone.getSelectedMenuItem(), item1);
//		Assertions.assertEquals(Integer.valueOf(clone.getAmountTextBox().getText()), amount.intValue());
//		Assertions.assertEquals(clone.getAmount().intValue(), amount.intValue());
//		Assertions.assertEquals(clone.getPrice().doubleValue(), item1.getGrossPrice().multiply(amount).doubleValue());
	}
}
