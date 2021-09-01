package test.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.IModel;
import model.Model;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemIDFactory;

class DishMenuItemDataTest {

	private static IModel model;
	private static IDishMenuItemDataFactory menuItemDataFac;
	private static IDishMenuItemIDFactory menuItemIDFac;
	
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
	void startUp() {
		model = new Model();
		menuItemDataFac = model.getItemDataCommunicationProtocoll();
		menuItemIDFac = model.getItemIDCommunicationProtocoll();
		
		model.addMenuItem(menuItemDataFac.constructData(
				i1Name,
				i1PorSize,
				i1Price,
				i1ProCost,
				i1Disc,
				i1id, menuItemIDFac));
		
		model.addMenuItem(menuItemDataFac.constructData(
				i2Name,
				i2PorSize,
				i2Price,
				i2ProCost,
				i2Disc,
				i2id, menuItemIDFac));
		
		model.addMenuItem(menuItemDataFac.constructData(
				i3Name,
				i3PorSize,
				i3Price,
				i3ProCost,
				i3Disc,
				i3id, menuItemIDFac));
		
	}
	
	@Test
	void contentTest() {
		IDishMenuItemData d1 = model.getMenuItem(menuItemIDFac.createDishMenuItemID("item1")).getDishMenuItemData(menuItemDataFac);
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(d1, i1Name, i1id, i1PorSize, i1Price, i1ProCost);
		IDishMenuItemData d2 = model.getMenuItem(menuItemIDFac.createDishMenuItemID("item2")).getDishMenuItemData(menuItemDataFac);
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(d2, i2Name, i2id, i2PorSize, i2Price, i2ProCost);
		IDishMenuItemData d3 = model.getMenuItem(menuItemIDFac.createDishMenuItemID("item3")).getDishMenuItemData(menuItemDataFac);
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(d3, i3Name, i3id, i3PorSize, i3Price, i3ProCost);
	}
	
	@Test
	void pricesPerPortionTest() {
		IDishMenuItemData[] data = model.getMenuData();
		
		IDishMenuItemData d1 = data[0];
		IDishMenuItemData d2 = data[1];
		IDishMenuItemData d3 = data[2];
		
		DishMenuItemTestUtilityClass.assertMenuItemDataPricesEqual(d1, i1Disc, i1PorSize, i1Price);
		DishMenuItemTestUtilityClass.assertMenuItemDataPricesEqual(d2, i2Disc, i2PorSize, i2Price);
		DishMenuItemTestUtilityClass.assertMenuItemDataPricesEqual(d3, i3Disc, i3PorSize, i3Price);
	}

}
