package test;

import java.io.File;
import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;

import model.IModel;
import model.datamapper.menu.DishMenuItemDAO;
import model.dish.DishMenuItem;
import model.dish.DishMenuItemData;
import model.entity.AccumulatingAggregateEntry;
import model.order.OrderData;
import server.model.IServerModel;
import server.model.ServerModel;
import test.model.dish.DishMenuItemTestUtilityClass;

public abstract class TestTemplate {
	protected final String i1Name = "aaa";
	protected final BigDecimal i1PorSize = BigDecimal.valueOf(2.34);
	protected final BigDecimal i1Price = BigDecimal.valueOf(5);
	protected final BigDecimal i1ProCost = BigDecimal.valueOf(4);
	protected final String i1id = "item1";
	
	protected final String i2Name = "bbb";
	protected final BigDecimal i2PorSize = BigDecimal.valueOf(5.67);
	protected final BigDecimal i2Price = BigDecimal.valueOf(1);
	protected final BigDecimal i2ProCost = BigDecimal.valueOf(0.5);
	protected final String i2id = "item2";
	
	protected final String i3Name = "ccc";
	protected final BigDecimal i3PorSize = BigDecimal.valueOf(3.34);
	protected final BigDecimal i3Price = BigDecimal.valueOf(4);
	protected final BigDecimal i3ProCost = BigDecimal.valueOf(3.5);
	protected final String i3id = "item3";
	
	protected DishMenuItemData iData1;
	protected DishMenuItemData iData2;
	protected DishMenuItemData iData3;
	
	protected DishMenuItem i1;
	protected DishMenuItem i2;
	protected DishMenuItem i3;
	
	protected DishMenuItemDAO menuDAO;
	
	protected BigDecimal o1a1 = BigDecimal.valueOf(2);
	protected BigDecimal o2a1 = BigDecimal.valueOf(2);
	protected BigDecimal o2a2 = BigDecimal.valueOf(3);
	protected BigDecimal o3a3 = BigDecimal.valueOf(5);
	
	protected String o1id = "order1";
	protected String o2id = "order2";
	protected String o3id = "order3";
	
	protected OrderData oData1;
	protected OrderData oData2;
	protected OrderData oData3;
	
	protected AccumulatingAggregateEntry<DishMenuItemData> orderItem1;
	protected AccumulatingAggregateEntry<DishMenuItemData> orderItem2;
	protected AccumulatingAggregateEntry<DishMenuItemData> orderItem3;
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
	public IServerModel initServerModel() {
		IServerModel model = new ServerModel(this.testFolderAddress);
		menuDAO = GeneralTestUtilityClass.getPrivateFieldValue(model, "menuDAO");
		return model;
	}
	
	public String serialiseDate(String date) {
		String year = date.substring(0, 4);
		String month = date.substring(4, 6);
		String day = date.substring(6,8);
		String hour = date.substring(8, 10);
		String min = date.substring(10, 12);
		String sec = date.substring(12, 14);
		String ms = date.substring(14, 17);
		return day+"/"+month+"/"+year+hour+":"+min+":"+sec+":"+ms;
	}
	
	public void addOrdersToServerModel(IServerModel model) {
		oData1 = model.getOrderFactory().constructData(o1id, model.getDateSettings()
				.parseDate(this.serialiseDate("20200809112233343")), false, false);
		oData1.addOrderItem(iData1, o1a1);
		orderItem1 = oData1.getOrderedItem(iData1.getID());
		model.addUnconfirmedOrder(oData1);
		
		oData2 = model.getOrderFactory().constructData(o2id, model.getDateSettings()
				.parseDate(this.serialiseDate("20200809235959111")), true, false);
		oData2.addOrderItem(iData1, o2a1);
		oData2.addOrderItem(iData2, o2a2);
		orderItem2 = oData2.getOrderedItem(iData2.getID());
		model.addUnconfirmedOrder(oData2);
		
		oData3 = model.getOrderFactory().constructData(o3id, model.getDateSettings()
				.parseDate(this.serialiseDate("20200809000000222")), true, true);
		oData3.addOrderItem(iData3, o3a3);
		orderItem3 = oData3.getOrderedItem(iData3.getID());
		model.addUnconfirmedOrder(oData3);
	}
	
	public void addDishMenuToServerModel(IServerModel model) {
		this.addMenuItemToServerModel(model, i1Name, i1id, i1PorSize, i1Price, i1ProCost);
		this.addMenuItemToServerModel(model, i2Name, i2id, i2PorSize, i2Price, i2ProCost);
		this.addMenuItemToServerModel(model, i3Name, i3id, i3PorSize, i3Price, i3ProCost);
	
		iData1 = model.getMenuItem(i1id);
		iData2 = model.getMenuItem(i2id);
		iData3 = model.getMenuItem(i3id);
		
		i1 = iData1.getAssociatedItem(model.getActiveDishMenuItemFinder());
		i2 = iData2.getAssociatedItem(model.getActiveDishMenuItemFinder());
		i3 = iData3.getAssociatedItem(model.getActiveDishMenuItemFinder());
	}
	
	public IServerModel createMinimalServerModel() {
		return new ServerModel(this.testFolderAddress);
	}
	
	public void addMenuItemToServerModel(IServerModel model, String dishName, String id, BigDecimal portionSize, BigDecimal price,
			BigDecimal productionCost) {
		model.addMenuItem(model.getMenuItemFactory().constructData(dishName, id, portionSize, price, productionCost));
	}
	
	public String serialiseMenuItem(IServerModel model, String dishName, String id, BigDecimal portionSize, BigDecimal price,
			BigDecimal productionCost) {
		return model.serialiseMenuItem(model.getMenuItemFactory().constructData(dishName, id, portionSize, price, productionCost));
	}
	
	public void dishMenuItemDataAssertion(DishMenuItemData[] datas) {
		for (DishMenuItemData data : datas) {
			if (data.getID().toString().equals(i1id)) {
				DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data, i1Name, i1id, i1PorSize, i1Price, i1ProCost);
			} else if (data.getID().toString().equals(i2id)) {
				DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data, i2Name, i2id, i2PorSize, i2Price, i2ProCost);
			} else if (data.getID().toString().equals(i3id)) {
				DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data, i3Name, i3id, i3PorSize, i3Price, i3ProCost);
			} else {
				Assertions.fail("An id has been parsed wrong");
			}
		}
	}
}
