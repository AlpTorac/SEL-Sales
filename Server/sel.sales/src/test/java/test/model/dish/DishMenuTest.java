package test.model.dish;

import java.io.File;
import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import model.dish.DishMenuItemData;
import server.model.IServerModel;
import server.model.ServerModel;
import test.TestTemplate;
//@Execution(value = ExecutionMode.SAME_THREAD)
class DishMenuTest extends TestTemplate {
	private static IServerModel model;
	
	@BeforeEach
	void startUp() {
		model = this.initServerModel();
		this.addDishMenuToServerModel(model);
	}
	
	@Test
	void addMenuItemTest() {
		DishMenuItemData[] data = model.getMenuData().getAllItems().toArray(DishMenuItemData[]::new);
		this.dishMenuItemDataAssertion(data);
	}

	@Test
	void removeMenuItemTest() {
		DishMenuItemData[] data = model.getMenuData().getAllItems().toArray(DishMenuItemData[]::new);
		this.dishMenuItemDataAssertion(data);
		Assertions.assertEquals(3, data.length);
		
		model.removeMenuItem("item1");
		data = model.getMenuData().getAllItems().toArray(DishMenuItemData[]::new);
		this.dishMenuItemDataAssertion(data);
		Assertions.assertEquals(2, data.length);
		
		model.removeMenuItem("item2");
		data = model.getMenuData().getAllItems().toArray(DishMenuItemData[]::new);
		this.dishMenuItemDataAssertion(data);
		Assertions.assertEquals(1, data.length);
		
		model.removeMenuItem("item3");
		data = model.getMenuData().getAllItems().toArray(DishMenuItemData[]::new);
		Assertions.assertEquals(0, data.length);
	}
	
	@Test
	void duplicateAddTest() {
		this.addMenuItemToServerModel(model, i3Name, i3id, i3PorSize, i3Price, i3ProCost);
		
		DishMenuItemData[] datas = model.getMenuData().getAllItems().toArray(DishMenuItemData[]::new);
		this.dishMenuItemDataAssertion(datas);
		Assertions.assertEquals(3, datas.length);
	}
	
	@Test
	void getTest() {
		DishMenuItemData i1 = model.getMenuItem("item1");
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(i1, i1Name, i1id, i1PorSize, i1Price, i1ProCost);
		DishMenuItemData i2 = model.getMenuItem("item2");
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(i2, i2Name, i2id, i2PorSize, i2Price, i2ProCost);
		DishMenuItemData i3 = model.getMenuItem("item3");
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(i3, i3Name, i3id, i3PorSize, i3Price, i3ProCost);
	}
}
