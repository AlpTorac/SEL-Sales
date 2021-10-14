package test.view;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.testfx.framework.junit5.ApplicationTest;

import controller.IController;
import controller.MainController;
import javafx.stage.Stage;
import model.IModel;
import model.Model;
import model.dish.IDishMenuItem;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemIDFactory;
import test.GeneralTestUtilityClass;
import test.UIOperationsUtilityClass;
import test.model.dish.DishMenuItemTestUtilityClass;
import view.IView;
import view.MainView;
import view.composites.MenuDesignArea;
import view.repository.HasText;
import view.repository.IEventShooterOnClickUIComponent;
import view.repository.uifx.FXAdvancedUIComponentFactory;
import view.repository.uifx.FXUIComponentFactory;
@Execution(value = ExecutionMode.SAME_THREAD)
class MenuItemOperationsTest extends ApplicationTest {
	private static IModel model;
	private static IController controller;
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
	
	@BeforeEach
	void prep() {
		model.removeAllOrders();
	}
	
	@Override
	public void start(Stage stage) {
		model = new Model();
		controller = new MainController(model);
		view = new MainView(new FXUIComponentFactory(), new FXAdvancedUIComponentFactory(), controller, model);
		view.startUp();
	}
	
	@Test
	void addMenuItemtest() {
		UIOperationsUtilityClass opHelper = new UIOperationsUtilityClass((MainView) view, controller, model);
		IDishMenuItemData addedItem = opHelper.addMenuItem(i1Name, i1id, i1Price, i1Price, i1PorSize, i1Disc);
		
		IDishMenuItemData[] datas = model.getMenuData().getAllDishMenuItems();
		Assertions.assertEquals(datas.length, 1);
		
		Assertions.assertTrue(addedItem.equals(datas[0]));
	}
	
	@Test
	void removeMenuItemTest() {
		UIOperationsUtilityClass opHelper = new UIOperationsUtilityClass((MainView) view, controller, model);
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
		UIOperationsUtilityClass opHelper = new UIOperationsUtilityClass((MainView) view, controller, model);
		IDishMenuItemData addedItem = opHelper.addMenuItem(i1Name, i1id, i1Price, i1Price, i1PorSize, i1Disc);
		
		IDishMenuItemData[] datas = model.getMenuData().getAllDishMenuItems();
		Assertions.assertEquals(datas.length, 1);
		
		Assertions.assertTrue(addedItem.equals(datas[0]));
		
		IDishMenuItemData editedItem = opHelper.editMenuItem(i2Name, i1id, i3Price, i2ProCost, i3PorSize, i2Disc);
		
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(editedItem, i2Name, i1id, i3PorSize, i3Price, i2ProCost, i2Disc);
	}

}
