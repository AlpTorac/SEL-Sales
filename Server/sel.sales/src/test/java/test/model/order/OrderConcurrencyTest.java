package test.model.order;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import model.IModel;
import model.Model;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemIDFactory;

class OrderConcurrencyTest {
	private static IModel model;
	private static IDishMenuItemDataFactory menuItemDataFac;
	private static IDishMenuItemIDFactory menuItemIDFac;
	
	private static ExecutorService pool = Executors.newFixedThreadPool(10);
	
	@BeforeAll
	static void startUp() {
		model = new Model();
		menuItemDataFac = model.getItemDataCommunicationProtocoll();
		menuItemIDFac = model.getItemIDCommunicationProtocoll();
		
		model.addMenuItem(menuItemDataFac.constructData(
				"aaa",
				BigDecimal.valueOf(2.34),
				BigDecimal.valueOf(5),
				BigDecimal.valueOf(4),
				"item1", menuItemIDFac));
		
		model.addMenuItem(menuItemDataFac.constructData(
				"bbb",
				BigDecimal.valueOf(5.67),
				BigDecimal.valueOf(1),
				BigDecimal.valueOf(0.5),
				"item2", menuItemIDFac));
		
		model.addMenuItem(menuItemDataFac.constructData(
				"ccc",
				BigDecimal.valueOf(3.34),
				BigDecimal.valueOf(4),
				BigDecimal.valueOf(3.5),
				"item3", menuItemIDFac));
	}
	
	@Test
	void test() {
		ClientSimulant c1 = new ClientSimulant("order1-20200809112233-0-0:item1,2;", model);
		ClientSimulant c2 = new ClientSimulant("order2-20200809235959-1-0:item1,12;item2,3;", model);
		ClientSimulant c3 = new ClientSimulant("order3-20210809000000-1-1:item3,5;item1,10;", model);
		ClientSimulant c4 = new ClientSimulant("order4-20200810112233-0-0:item1,2;item2,0;", model);
		ClientSimulant c5 = new ClientSimulant("order5-20200812235959-1-0:item1,2;item2,3;", model);
		ClientSimulant c6 = new ClientSimulant("order6-20200813000000-1-1:item3,5;item3,4;", model);
		ClientSimulant c7 = new ClientSimulant("order7-20200909112233-0-0:item1,2;item2,5;", model);
		ClientSimulant c8 = new ClientSimulant("order8-20210809235959-1-0:item1,2;item2,3;", model);
		ClientSimulant c9 = new ClientSimulant("order9-20210709000000-1-1:item3,5;", model);
		ClientSimulant c10 = new ClientSimulant("order10-20210809000000-1-1:item3,5;", model);
		
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
		
		Assertions.assertEquals(model.getAllOrders().length, 10);
	}
	
}
