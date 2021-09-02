package test.view;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
import test.MainViewOperationsUtilityClass;
import test.model.DishMenuItemTestUtilityClass;
import view.IView;
import view.MainView;
import view.composites.MenuDesignArea;
import view.repository.HasText;
import view.repository.IEventShooterOnClickUIComponent;
import view.repository.uifx.FXUIComponentFactory;

class MenuItemOperationsTest extends ApplicationTest {
	private static IModel model;
	private static IController controller;
	private static IView view;
//	private static MenuDesignArea mda;
//	
//	private static HasText dishNameBox;
//	private static HasText priceBox;
//	private static HasText idBox;
//	private static HasText prodCostBox;
//	private static HasText porSizeBox;
//	private static HasText discBox;
//	
//	private static IEventShooterOnClickUIComponent addButton;
//	private static IEventShooterOnClickUIComponent removeButton;
//	private static IEventShooterOnClickUIComponent editButton;
	
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
	
	@Override
	public void start(Stage stage) {
		model = new Model();
		controller = new MainController(model);
		view = new MainView(new FXUIComponentFactory(), controller, model);
		view.startUp();
//		
//		MainView mv = (MainView) view;
//		mda = GeneralTestUtilityClass.getPrivateFieldValue(mv, "mda");
//		dishNameBox = mda.getDishNameBox();
//		priceBox = mda.getPriceBox();
//		idBox = mda.getMenuItemIDBox();
//		prodCostBox = mda.getProductionCostBox();
//		porSizeBox = mda.getPortionBox();
//		discBox = mda.getDiscountBox();
//		
//		addButton = mda.getAddButton();
//		removeButton = mda.getRemoveButton();
//		editButton = mda.getEditButton();
	}
	
	@Test
	void addMenuItemtest() {
		MainViewOperationsUtilityClass opHelper = new MainViewOperationsUtilityClass((MainView) view, controller, model);
		IDishMenuItem addedItem = opHelper.addMenuItem(i1Name, i1id, i1Price, i1Price, i1PorSize, i1Disc);
		
		IDishMenuItemData[] datas = model.getMenuData();
		Assertions.assertEquals(datas.length, 1);
		
		DishMenuItemTestUtilityClass.assertMenuItemAndDataEqual(addedItem, datas[0]);
		model.removeMenuItem(model.getItemIDCommunicationProtocoll().createDishMenuItemID(i1id));
	}
	
	@Test
	void removeMenuItemTest() {
		MainViewOperationsUtilityClass opHelper = new MainViewOperationsUtilityClass((MainView) view, controller, model);
		IDishMenuItem addedItem = opHelper.addMenuItem(i1Name, i1id, i1Price, i1Price, i1PorSize, i1Disc);
		IDishMenuItemData[] datas = model.getMenuData();
		Assertions.assertEquals(datas.length, 1);
		
		DishMenuItemTestUtilityClass.assertMenuItemAndDataEqual(addedItem, datas[0]);
		
		IDishMenuItem removedItem = opHelper.removeMenuItem(i1id);
		datas = model.getMenuData();
		
		Assertions.assertEquals(addedItem, removedItem);
		Assertions.assertEquals(datas.length, 0);
	}
	
	@Test
	void editMenuItemTest() {
		MainViewOperationsUtilityClass opHelper = new MainViewOperationsUtilityClass((MainView) view, controller, model);
		IDishMenuItem addedItem = opHelper.addMenuItem(i1Name, i1id, i1Price, i1Price, i1PorSize, i1Disc);
		
		IDishMenuItemData[] datas = model.getMenuData();
		Assertions.assertEquals(datas.length, 1);
		
		DishMenuItemTestUtilityClass.assertMenuItemAndDataEqual(addedItem, datas[0]);
		
		IDishMenuItem editedItem = opHelper.editMenuItem(i2Name, i1id, i3Price, i2ProCost, i3PorSize, i2Disc);
		
		DishMenuItemTestUtilityClass.assertMenuItemEqual(editedItem, i2Name, i1id, i3PorSize, i3Price, i2ProCost, i2Disc);
		model.removeMenuItem(model.getItemIDCommunicationProtocoll().createDishMenuItemID(i1id));
	}

}
