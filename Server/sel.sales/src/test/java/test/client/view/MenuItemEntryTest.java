package test.client.view;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import client.view.composites.MenuItemEntry;
import client.view.composites.PriceUpdateTarget;
import model.dish.DishMenuData;
import model.dish.DishMenuItemData;
import model.entity.AccumulatingAggregateEntry;
import server.model.IServerModel;
import test.FXTestTemplate;
import view.repository.uifx.FXUIComponentFactory;
//@Execution(value = ExecutionMode.SAME_THREAD)
class MenuItemEntryTest extends FXTestTemplate {
	
	private IServerModel model;
	
	private PriceUpdateTarget<MenuItemEntry> u;
	private MenuItemEntry entry;
	
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
		entry = new MenuItemEntry(new FXUIComponentFactory(), u);
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
		
		Assertions.assertEquals(entry.getPrice().intValue(), BigDecimal.ZERO.intValue());
	}
	
	@Test
	void refreshMenuNullTest() {
		Assertions.assertNull(entry.getActiveMenu());
		entry.refreshMenu(null);
		Assertions.assertNull(entry.getActiveMenu());
		
		Assertions.assertEquals(entry.getPrice().intValue(), BigDecimal.ZERO.intValue());
	}
	
	@Test
	void selectionPersistenceTest() {
		Assertions.assertNull(entry.getActiveMenu());
		
		DishMenuData menuData = model.getMenuData();
		
		entry.refreshMenu(menuData);
		Assertions.assertEquals(entry.getActiveMenu(), menuData);
		Assertions.assertEquals(entry.getPrice().intValue(), BigDecimal.ZERO.intValue());
		
		entry.getMenuItemChoiceBox().artificiallySelectItem(iData2);
		Assertions.assertEquals(iData2, entry.getSelectedMenuItem());
		
		entry.refreshMenu(menuData);
		Assertions.assertEquals(entry.getActiveMenu(), menuData);
		Assertions.assertEquals(iData2, entry.getSelectedMenuItem());
	}
	
	@Test
	void cloneTest() {
		entry.refreshMenu(model.getMenuData());
		
		BigDecimal amount =  BigDecimal.valueOf(2);
		
		AccumulatingAggregateEntry<DishMenuItemData> itemData = new AccumulatingAggregateEntry<DishMenuItemData>(iData1, amount);
		entry.displayData(itemData);
		
		MenuItemEntry clone = entry.clone();
		
		Assertions.assertFalse(clone == entry);
		
		MenuItemEntryUtilityClass.assertMenuItemEntryEquals(clone, iData1, amount);
		
//		Assertions.assertEquals(clone.getMenuItemChoiceBox().getSelectedElement(), item1);
//		Assertions.assertEquals(clone.getSelectedMenuItem(), item1);
//		Assertions.assertEquals(Integer.valueOf(clone.getAmountTextBox().getText()), amount.intValue());
//		Assertions.assertEquals(clone.getAmount().intValue(), amount.intValue());
//		Assertions.assertEquals(clone.getPrice().doubleValue(), item1.getGrossPrice().multiply(amount).doubleValue());
	}
	
	@Test
	void displayNullOrderItemTest() {
		Assertions.assertNull(entry.getSelectedMenuItem());
		entry.displayData(null);
		Assertions.assertNull(entry.getSelectedMenuItem());
		Assertions.assertEquals(entry.getAmount().intValue(), 1);
	}
}
