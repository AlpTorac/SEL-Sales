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
class OrderNoDiscountCalculatedDataTest {
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
	
	private BigDecimal o1a1 = BigDecimal.valueOf(2);
	private BigDecimal o2a1 = BigDecimal.valueOf(2);
	private BigDecimal o2a2 = BigDecimal.valueOf(3);
	private BigDecimal o3a3 = BigDecimal.valueOf(5);
	
	@BeforeEach
	void startUp() {
		model = new Model();
		serialiser = model.getDishMenuItemSerialiser();
		model.addMenuItem(serialiser.serialise(i1Name, i1id, i1PorSize, i1ProCost, i1Price, i1Disc));
		model.addMenuItem(serialiser.serialise(i2Name, i2id, i2PorSize, i2ProCost, i2Price, i2Disc));
		model.addMenuItem(serialiser.serialise(i3Name, i3id, i3PorSize, i3ProCost, i3Price, i3Disc));
		
		model.addUnconfirmedOrder("order1-20200809112233343-0-0:item1,"+o1a1.toPlainString()+";");
		model.addUnconfirmedOrder("order2-20200809235959111-1-0:item1,"+o2a1.toPlainString()+";item2,"+o2a2.toPlainString()+";");
		model.addUnconfirmedOrder("order3-20200809000000222-1-1:item3,"+o3a3.toPlainString()+";");
	}
	@Test
	void totalDiscountTest() {
		IOrderData[] orderData = model.getAllUnconfirmedOrders();
		
		Assertions.assertEquals(BigDecimal.valueOf(0).compareTo(orderData[0].getTotalDiscount()), 0);
		Assertions.assertEquals(BigDecimal.valueOf(0.3).compareTo(orderData[1].getTotalDiscount()), 0);
		orderData[2].setOrderDiscount(BigDecimal.valueOf(2));
		Assertions.assertEquals(BigDecimal.valueOf(5+2).compareTo(orderData[2].getTotalDiscount()), 0);
	}

	@Test
	void grossSumTest() {
		IOrderData[] orderData = model.getAllUnconfirmedOrders();
		
		Assertions.assertEquals(BigDecimal.valueOf(10).compareTo(orderData[0].getGrossSum()), 0);
		Assertions.assertEquals(BigDecimal.valueOf(12.7).compareTo(orderData[1].getGrossSum()), 0);
		Assertions.assertEquals(BigDecimal.valueOf(15).compareTo(orderData[2].getGrossSum()), 0);
	}
	
	@Test
	void netSumTest() {
		IOrderData[] orderData = model.getAllUnconfirmedOrders();
		orderData[0].setOrderDiscount(BigDecimal.valueOf(2));
		Assertions.assertEquals(BigDecimal.valueOf(8).compareTo(orderData[0].getNetSum()), 0);
		orderData[1].setOrderDiscount(BigDecimal.valueOf(1));
		Assertions.assertEquals(BigDecimal.valueOf(11.7).compareTo(orderData[1].getNetSum()), 0);
		orderData[2].setOrderDiscount(BigDecimal.valueOf(3));
		Assertions.assertEquals(BigDecimal.valueOf(12).compareTo(orderData[2].getNetSum()), 0);
	}
	
}
