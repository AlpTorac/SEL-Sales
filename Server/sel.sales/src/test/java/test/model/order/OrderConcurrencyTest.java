package test.model.order;

import java.io.File;
import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import server.model.IServerModel;
import server.model.ServerModel;
//@Execution(value = ExecutionMode.SAME_THREAD)
class OrderConcurrencyTest {
	private static IServerModel model;
	private static ExecutorService pool = Executors.newFixedThreadPool(10);
	
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
	void startUp() {
		model = new ServerModel(this.testFolderAddress);
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i1Name, i1id, i1PorSize, i1ProCost, i1Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i2Name, i2id, i2PorSize, i2ProCost, i2Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i3Name, i3id, i3PorSize, i3ProCost, i3Price));
	}
	
	@Test
	void test() {
		ClientSimulant c1 = new ClientSimulant("order1#20200809112233123#0#0:item1,2;", model);
		ClientSimulant c2 = new ClientSimulant("order2#20200809235959456#1#0:item1,12;item2,3;", model);
		ClientSimulant c3 = new ClientSimulant("order3#20210809000000789#1#1:item3,5;item1,10;", model);
		ClientSimulant c4 = new ClientSimulant("order4#20200810112233012#0#0:item1,2;item2,0;", model);
		ClientSimulant c5 = new ClientSimulant("order5#20200812235959475#1#0:item1,2;item2,3;", model);
		ClientSimulant c6 = new ClientSimulant("order6#20200813000000183#1#1:item3,5;item3,4;", model);
		ClientSimulant c7 = new ClientSimulant("order7#20200909112233937#0#0:item1,2;item2,5;", model);
		ClientSimulant c8 = new ClientSimulant("order8#20210809235959253#1#0:item1,2;item2,3;", model);
		ClientSimulant c9 = new ClientSimulant("order9#20210709000000745#1#1:item3,5;", model);
		ClientSimulant c10 = new ClientSimulant("order10#20210809000000111#1#1:item3,5;", model);
		
		pool.submit(c1);
		pool.submit(c2);
		pool.submit(c3);
		pool.submit(c4);
		pool.submit(c5);
		pool.submit(c6);
		pool.submit(c7);
		pool.submit(c8);
		pool.submit(c9);
		pool.submit(c10);
		
		try {
			pool.awaitTermination(1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Assertions.assertEquals(model.getAllUnconfirmedOrders().length, 10);
	}
	
}
