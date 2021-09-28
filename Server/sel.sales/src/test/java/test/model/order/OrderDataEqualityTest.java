package test.model.order;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import model.IModel;
import model.Model;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemIDFactory;
import model.dish.serialise.IDishMenuItemSerialiser;
import model.order.IOrderData;
@Execution(value = ExecutionMode.SAME_THREAD)
class OrderDataEqualityTest {
	private static IModel model;
	private static IDishMenuItemSerialiser serialiser;
	
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
		serialiser = model.getDishMenuItemSerialiser();
		model.addMenuItem(serialiser.serialise(i1Name, i1id, i1PorSize, i1ProCost, i1Price, i1Disc));
		model.addMenuItem(serialiser.serialise(i2Name, i2id, i2PorSize, i2ProCost, i2Price, i2Disc));
		model.addMenuItem(serialiser.serialise(i3Name, i3id, i3PorSize, i3ProCost, i3Price, i3Disc));
		
		model.addUnconfirmedOrder("order1-20200809112233000-0-0:item1,2;");
		model.addUnconfirmedOrder("order2-20200809235959866-1-0:item1,2;item2,3;");
		model.addUnconfirmedOrder("order3-20200809000000675-1-1:item3,5;");
	}
	
	@Test
	void notEqualTest() {
		IOrderData[] orderData = model.getAllUnconfirmedOrders();
		
		Assertions.assertFalse(orderData[0].equals(orderData[1]));
		Assertions.assertFalse(orderData[0].equals(orderData[2]));
		Assertions.assertFalse(orderData[1].equals(orderData[0]));
		Assertions.assertFalse(orderData[1].equals(orderData[2]));
		Assertions.assertFalse(orderData[2].equals(orderData[0]));
		Assertions.assertFalse(orderData[2].equals(orderData[1]));
	}
	
	@Test
	void equalTest() {
		IOrderData[] orderData1 = model.getAllUnconfirmedOrders();
		IOrderData[] orderData2 = model.getAllUnconfirmedOrders();
		
		// Make sure references are different
		Assertions.assertFalse(orderData1[0] == orderData2[0]);
		Assertions.assertFalse(orderData1[1] == orderData2[1]);
		Assertions.assertFalse(orderData1[2] == orderData2[2]);
		
		Assertions.assertTrue(orderData1[0].equals(orderData2[0]));
		Assertions.assertTrue(orderData1[1].equals(orderData2[1]));
		Assertions.assertTrue(orderData1[2].equals(orderData2[2]));
	}
}
