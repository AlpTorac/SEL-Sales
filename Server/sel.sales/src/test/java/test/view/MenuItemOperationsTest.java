package test.view;

import java.io.File;
import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.stage.Stage;
import model.dish.DishMenu;
import model.dish.IDishMenu;
import model.dish.IDishMenuData;
import model.dish.IDishMenuItemData;
import model.settings.ISettings;
import model.settings.Settings;
import model.settings.SettingsField;
import server.controller.IServerController;
import server.controller.StandardServerController;
import server.model.IServerModel;
import server.model.ServerModel;
import server.view.StandardServerView;
import test.GeneralTestUtilityClass;
import test.StandardServerViewOperationsUtilityClass;
import test.model.dish.DishMenuItemTestUtilityClass;
import view.IView;
import view.repository.uifx.FXAdvancedUIComponentFactory;
import view.repository.uifx.FXUIComponentFactory;
@Execution(value = ExecutionMode.SAME_THREAD)
class MenuItemOperationsTest extends ApplicationTest {
	private static IServerModel model;
	private static IServerController controller;
	private static IView view;
	
	private String i1Name = "aaa";
	private BigDecimal i1PorSize = BigDecimal.valueOf(2.34);
	private BigDecimal i1Price = BigDecimal.valueOf(5);
	private BigDecimal i1ProCost = BigDecimal.valueOf(4);
	private BigDecimal i1Disc = BigDecimal.valueOf(0);
	private String i1id = "item1";
	
	private String i2Name = "bbb";
	private BigDecimal i2PorSize = BigDecimal.valueOf(5.67);
	private BigDecimal i2Price = BigDecimal.valueOf(1);
	private BigDecimal i2ProCost = BigDecimal.valueOf(0.5);
	private BigDecimal i2Disc = BigDecimal.valueOf(0.1);
	private String i2id = "item2";
	
	private String i3Name = "ccc";
	private BigDecimal i3PorSize = BigDecimal.valueOf(3.34);
	private BigDecimal i3Price = BigDecimal.valueOf(4);
	private BigDecimal i3ProCost = BigDecimal.valueOf(3.5);
	private BigDecimal i3Disc = BigDecimal.valueOf(1);
	private String i3id = "item3";
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
	@BeforeEach
	void prep() {
		GeneralTestUtilityClass.deletePathContent(testFolderAddress);
		model.removeAllOrders();
	}
	
	@AfterEach
	void cleanUp() {
		model.close();
		GeneralTestUtilityClass.deletePathContent(testFolderAddress);
	}
	
	@Override
	public void start(Stage stage) {
		model = new ServerModel(this.testFolderAddress);
		controller = new StandardServerController(model);
		view = new StandardServerView(new FXUIComponentFactory(), new FXAdvancedUIComponentFactory(), controller, model);
		view.startUp();
	}
	
	@Test
	void addMenuItemtest() {
		StandardServerViewOperationsUtilityClass opHelper = new StandardServerViewOperationsUtilityClass((StandardServerView) view, controller, model);
		IDishMenuItemData addedItem = opHelper.addMenuItem(i1Name, i1id, i1Price, i1Price, i1PorSize, i1Disc);
		
		IDishMenuItemData[] datas = model.getMenuData().getAllDishMenuItems();
		Assertions.assertEquals(datas.length, 1);
		
		Assertions.assertTrue(addedItem.equals(datas[0]));
	}
	
	@Test
	void removeMenuItemTest() {
		StandardServerViewOperationsUtilityClass opHelper = new StandardServerViewOperationsUtilityClass((StandardServerView) view, controller, model);
		IDishMenuItemData addedItem = opHelper.addMenuItem(i1Name, i1id, i1Price, i1ProCost, i1PorSize, i1Disc);
		IDishMenuItemData[] datas = model.getMenuData().getAllDishMenuItems();
		Assertions.assertEquals(datas.length, 1);
		
		Assertions.assertTrue(addedItem.equals(datas[0]));
		
		IDishMenuItemData removedItem = opHelper.removeMenuItem(i1id);
		datas = model.getMenuData().getAllDishMenuItems();
		
		Assertions.assertTrue(addedItem.equals(removedItem));
		Assertions.assertEquals(datas.length, 0);
	}
	
	@Test
	void editMenuItemTest() {
		StandardServerViewOperationsUtilityClass opHelper = new StandardServerViewOperationsUtilityClass((StandardServerView) view, controller, model);
		IDishMenuItemData addedItem = opHelper.addMenuItem(i1Name, i1id, i1Price, i1Price, i1PorSize, i1Disc);
		
		IDishMenuItemData[] datas = model.getMenuData().getAllDishMenuItems();
		Assertions.assertEquals(datas.length, 1);
		
		Assertions.assertTrue(addedItem.equals(datas[0]));
		
		IDishMenuItemData editedItem = opHelper.editMenuItem(i2Name, i1id, i3Price, i2ProCost, i3PorSize, i2Disc);
		
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(editedItem, i2Name, i1id, i3PorSize, i3Price, i2ProCost);
	}

	@Test
	void writeMenuTest() {
		Assertions.assertEquals(model.getMenuData().getAllDishMenuItems().length, 0);
		StandardServerViewOperationsUtilityClass opHelper = new StandardServerViewOperationsUtilityClass((StandardServerView) view, controller, model);
		IDishMenuItemData addedItem1 = opHelper.addMenuItem(i1Name, i1id, i1Price, i1Price, i1PorSize, i1Disc);
		IDishMenuItemData addedItem2 = opHelper.addMenuItem(i2Name, i2id, i2Price, i2Price, i2PorSize, i2Disc);
		IDishMenuItemData addedItem3 = opHelper.addMenuItem(i3Name, i3id, i3Price, i3Price, i3PorSize, i3Disc);
		
		IDishMenuData menu = model.getMenuData();
		
		GeneralTestUtilityClass.performWait(300);
		GeneralTestUtilityClass.deletePathContent(testFolderAddress);
		model.addSetting(SettingsField.DISH_MENU_FOLDER, testFolderAddress);
		opHelper.writeDishMenu();
		
		IServerModel model2 = new ServerModel(testFolderAddress);
		model2.addSetting(SettingsField.DISH_MENU_FOLDER, testFolderAddress);
		model2.loadDishMenu(testFolderAddress);
		Assertions.assertTrue(model2.getMenuData().equals(menu));
		
		model2.close();
	}
}
