package test.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.stage.Stage;
import model.dish.DishMenuData;
import model.dish.DishMenuItemData;
import model.settings.SettingsField;
import server.controller.IServerController;
import server.model.IServerModel;
import server.model.ServerModel;
import server.view.StandardServerView;
import test.FXTestTemplate;
import test.GeneralTestUtilityClass;
import test.StandardServerViewOperationsUtilityClass;
import test.model.dish.DishMenuItemTestUtilityClass;
//@Execution(value = ExecutionMode.SAME_THREAD)
class MenuItemOperationsTest extends FXTestTemplate {
	private IServerModel model;
	private IServerController controller;
	private StandardServerView view;
	
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
		model = this.initServerModel();
		controller = this.initServerController(model);
		view = this.initServerView(model, controller);
		view.startUp();
		this.setServerOpHelper(model, controller, view);
	}
	
	@Test
	void addMenuItemtest() {
		DishMenuItemData addedItem = serverOpHelper.addMenuItem(i1Name, i1id, i1Price, i1Price, i1PorSize, i1Disc);
		
		DishMenuItemData[] datas = model.getMenuData().getAllElements().toArray(DishMenuItemData[]::new);
		Assertions.assertEquals(datas.length, 1);
		
		Assertions.assertTrue(addedItem.equals(datas[0]));
	}
	
	@Test
	void removeMenuItemTest() {
		StandardServerViewOperationsUtilityClass opHelper = new StandardServerViewOperationsUtilityClass((StandardServerView) view, controller, model);
		DishMenuItemData addedItem = opHelper.addMenuItem(i1Name, i1id, i1Price, i1ProCost, i1PorSize, i1Disc);
		DishMenuItemData[] datas = model.getMenuData().getAllElements().toArray(DishMenuItemData[]::new);
		Assertions.assertEquals(datas.length, 1);
		
		Assertions.assertTrue(addedItem.equals(datas[0]));
		
		DishMenuItemData removedItem = opHelper.removeMenuItem(i1id);
		datas = model.getMenuData().getAllElements().toArray(DishMenuItemData[]::new);
		
		Assertions.assertTrue(addedItem.equals(removedItem));
		Assertions.assertEquals(datas.length, 0);
	}
	
	@Test
	void editMenuItemTest() {
		StandardServerViewOperationsUtilityClass opHelper = new StandardServerViewOperationsUtilityClass((StandardServerView) view, controller, model);
		DishMenuItemData addedItem = opHelper.addMenuItem(i1Name, i1id, i1Price, i1ProCost, i1PorSize, i1Disc);
		
		DishMenuItemData[] datas = model.getMenuData().getAllElements().toArray(DishMenuItemData[]::new);
		Assertions.assertEquals(datas.length, 1);
		
		Assertions.assertTrue(addedItem.equals(datas[0]));
		
		DishMenuItemData itemData = opHelper.editMenuItem(i2Name, i1id, i3Price, i2ProCost, i3PorSize, i2Disc);
//		DishMenuItemData data = model.getMenuItem(i1id);
//		Assertions.assertEquals(data.getDishName(), i2Name);
//		Assertions.assertEquals(data.getPortionSize().compareTo(i3PorSize), 0);
//		Assertions.assertEquals(data.getGrossPrice().compareTo(i3Price), 0);
//		Assertions.assertEquals(data.getProductionCost().compareTo(i2ProCost), 0);
		
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(itemData, i2Name, i1id, i3PorSize, i3Price, i2ProCost);
	}

	@Test
	void writeMenuTest() {
		Assertions.assertEquals(model.getMenuData().getAllElements().size(), 0);
		StandardServerViewOperationsUtilityClass opHelper = new StandardServerViewOperationsUtilityClass((StandardServerView) view, controller, model);
		DishMenuItemData addedItem1 = opHelper.addMenuItem(i1Name, i1id, i1Price, i1Price, i1PorSize, i1Disc);
		DishMenuItemData addedItem2 = opHelper.addMenuItem(i2Name, i2id, i2Price, i2Price, i2PorSize, i2Disc);
		DishMenuItemData addedItem3 = opHelper.addMenuItem(i3Name, i3id, i3Price, i3Price, i3PorSize, i3Disc);
		
		DishMenuData menu = model.getMenuData();
		
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
