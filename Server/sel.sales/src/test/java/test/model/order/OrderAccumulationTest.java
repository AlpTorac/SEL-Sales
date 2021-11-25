package test.model.order;

import java.io.File;
import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import model.dish.IDishMenuItemData;
import model.order.IOrderData;
import model.order.IOrderItemData;
import server.model.IServerModel;
import server.model.ServerModel;
import test.GeneralTestUtilityClass;
@Execution(value = ExecutionMode.SAME_THREAD)
class OrderAccumulationTest {
	private static IServerModel model;
	
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
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
	@BeforeEach
	void startUp() {
		model = new ServerModel(this.testFolderAddress);
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i1Name, i1id, i1PorSize, i1ProCost, i1Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i2Name, i2id, i2PorSize, i2ProCost, i2Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i3Name, i3id, i3PorSize, i3ProCost, i3Price));
		
		item1 = model.getMenuItem(i1id);
		item2 = model.getMenuItem(i2id);
		item3 = model.getMenuItem(i3id);
		
		model.addUnconfirmedOrder("order2#20200809235959890#1#0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1");
	}

	@Test
	void test() {
		IOrderData[] orderData = model.getAllUnconfirmedOrders();
		
		Assertions.assertEquals(orderData.length, 1);
		IOrderItemData i1d = model.getOrderHelper().createOrderItemData(item1, BigDecimal.valueOf(9));
		IOrderItemData i2d = model.getOrderHelper().createOrderItemData(item2, BigDecimal.valueOf(3));
		IOrderItemData i3d = model.getOrderHelper().createOrderItemData(item3, BigDecimal.valueOf(6));
		IOrderItemData[] itemDatas = new IOrderItemData[] {i1d, i2d, i3d};
		
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(orderData[0].getOrderedItems(),
				itemDatas));
	}
}
