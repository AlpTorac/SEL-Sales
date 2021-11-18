package test.client.view;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.testfx.framework.junit5.ApplicationTest;

import client.view.composites.EditableMenuItemEntry;
import client.view.composites.MenuItemEntry;
import client.view.composites.PriceUpdateTarget;
import model.dish.IDishMenuData;
import model.dish.IDishMenuItemData;
import model.order.IOrderItemData;
import server.model.IServerModel;
import server.model.ServerModel;
import view.repository.uifx.FXUIComponentFactory;
@Execution(value = ExecutionMode.SAME_THREAD)
class EditableMenuItemEntryTest extends ApplicationTest {
	
	private IServerModel model;
	
	private IDishMenuItemData item1;
	private String i1Name = "aaa";
	private BigDecimal i1PorSize = BigDecimal.valueOf(2.34);
	private BigDecimal i1Price = BigDecimal.valueOf(5);
	private BigDecimal i1ProCost = BigDecimal.valueOf(4);
	private BigDecimal i1Disc = BigDecimal.valueOf(0);
	private String i1id = "item1";
	
	private IDishMenuItemData item2;
	private String i2Name = "bbb";
	private BigDecimal i2PorSize = BigDecimal.valueOf(5.67);
	private BigDecimal i2Price = BigDecimal.valueOf(1);
	private BigDecimal i2ProCost = BigDecimal.valueOf(0.5);
	private BigDecimal i2Disc = BigDecimal.valueOf(0.1);
	private String i2id = "item2";
	
	private IDishMenuItemData item3;
	private String i3Name = "ccc";
	private BigDecimal i3PorSize = BigDecimal.valueOf(3.34);
	private BigDecimal i3Price = BigDecimal.valueOf(4);
	private BigDecimal i3ProCost = BigDecimal.valueOf(3.5);
	private BigDecimal i3Disc = BigDecimal.valueOf(1);
	private String i3id = "item3";
	
	private PriceUpdateTarget<MenuItemEntry> u;
	private EditableMenuItemEntry entry;
	
	private boolean isPriceRefreshed;
	private boolean isRemoved;
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
	@BeforeEach
	void prep() {
		model = new ServerModel(this.testFolderAddress);
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i1Name, i1id, i1PorSize, i1ProCost, i1Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i2Name, i2id, i2PorSize, i2ProCost, i2Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i3Name, i3id, i3PorSize, i3ProCost, i3Price));
		
		item1 = model.getMenuItem(i1id);
		item2 = model.getMenuItem(i2id);
		item3 = model.getMenuItem(i3id);
		
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
		model.close();
		entry = null;
		u = null;
	}

	@Test
	void orderItemDisplayTest() {
		entry.refreshMenu(model.getMenuData());
		
		BigDecimal amount =  BigDecimal.valueOf(2);
		
		IOrderItemData itemData = model.getOrderHelper().createOrderItemData(item1, amount);
		entry.displayData(itemData);
		
		MenuItemEntryUtilityClass.assertMenuItemEntryEquals(entry, item1, amount);
		
//		Assertions.assertEquals(entry.getMenuItemChoiceBox().getSelectedElement(), item1);
//		Assertions.assertEquals(entry.getSelectedMenuItem(), item1);
//		Assertions.assertEquals(Integer.valueOf(entry.getAmountTextBox().getText()), amount.intValue());
//		Assertions.assertEquals(entry.getAmount().intValue(), amount.intValue());
//		Assertions.assertEquals(entry.getPrice().doubleValue(), item1.getGrossPrice().multiply(amount).doubleValue());
	}
	
	@Test
	void refreshMenuTest() {
		Assertions.assertNull(entry.getActiveMenu());
		
		IDishMenuData menuData = model.getMenuData();
		
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
		
		entry.getMenuItemChoiceBox().artificiallySelectItem(item2);
		
		MenuItemEntryUtilityClass.assertMenuItemEntryEquals(entry, item2, amount);
		
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
		
		entry.getMenuItemChoiceBox().artificiallySelectItem(item2);
		Assertions.assertEquals(entry.getPrice().doubleValue(), item2.getGrossPrice().multiply(amount).doubleValue());
		
		entry.getAmountIncButton().performArtificialClick();
		amount = amount.add(BigDecimal.ONE);
		
		MenuItemEntryUtilityClass.assertMenuItemEntryEquals(entry, item2, amount);
		
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
		
		entry.getMenuItemChoiceBox().artificiallySelectItem(item2);
		Assertions.assertEquals(entry.getPrice().doubleValue(), item2.getGrossPrice().multiply(amount).doubleValue());
		
		// amount = 0
		entry.getAmountDecButton().performArtificialClick();
		amount = amount.subtract(BigDecimal.ONE);
		
		MenuItemEntryUtilityClass.assertMenuItemEntryEquals(entry, item2, amount);
		
//		Assertions.assertEquals(Integer.valueOf(entry.getAmountTextBox().getText()), amount.intValue());
//		Assertions.assertEquals(entry.getAmount().intValue(), amount.intValue());
//		Assertions.assertEquals(entry.getPrice().doubleValue(), item2.getGrossPrice().multiply(amount).doubleValue());
		Assertions.assertTrue(isPriceRefreshed);
		isPriceRefreshed = false;
		
		// amount still = 0
		entry.getAmountDecButton().performArtificialClick();
		
		MenuItemEntryUtilityClass.assertMenuItemEntryEquals(entry, item2, amount);
		
//		Assertions.assertEquals(Integer.valueOf(entry.getAmountTextBox().getText()), amount.intValue());
//		Assertions.assertEquals(entry.getAmount().intValue(), amount.intValue());
//		Assertions.assertEquals(entry.getPrice().doubleValue(), item2.getGrossPrice().multiply(amount).doubleValue());
		Assertions.assertFalse(isPriceRefreshed);
	}
	
	@Test
	void cloneTest() {
		entry.refreshMenu(model.getMenuData());
		
		BigDecimal amount =  BigDecimal.valueOf(2);
		
		IOrderItemData itemData = model.getOrderHelper().createOrderItemData(item1, amount);
		entry.displayData(itemData);
		
		EditableMenuItemEntry clone = entry.clone();
		
		Assertions.assertFalse(clone == entry);
		
		MenuItemEntryUtilityClass.assertMenuItemEntryEquals(clone, item1, amount);
		
//		Assertions.assertEquals(clone.getMenuItemChoiceBox().getSelectedElement(), item1);
//		Assertions.assertEquals(clone.getSelectedMenuItem(), item1);
//		Assertions.assertEquals(Integer.valueOf(clone.getAmountTextBox().getText()), amount.intValue());
//		Assertions.assertEquals(clone.getAmount().intValue(), amount.intValue());
//		Assertions.assertEquals(clone.getPrice().doubleValue(), item1.getGrossPrice().multiply(amount).doubleValue());
	}
}
