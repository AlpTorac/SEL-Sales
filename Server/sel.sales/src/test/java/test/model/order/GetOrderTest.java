package test.model.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.order.OrderData;
import server.model.IServerModel;
import test.GeneralTestUtilityClass;
import test.FXTestTemplate;
//@Execution(value = ExecutionMode.SAME_THREAD)
class GetOrderTest extends FXTestTemplate {
	private IServerModel model;
	
	@BeforeEach
	void startUp() {
		model = this.initServerModel();
		this.addDishMenuToServerModel(model);
		this.initOrders(model);
		this.addOrdersToModel(model);
	}
	
	@AfterEach
	void cleanUp() {
		this.closeModel(model);
	}
	
	@Test
	void getUnconfirmedOrderTest() {
		OrderData[] data = model.getAllUnconfirmedOrders();
		Assertions.assertEquals(data.length, 3);
		OrderData[] gottenOrders = {model.getOrder(o1id), model.getOrder(o2id), model.getOrder(o3id)};
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(data, gottenOrders));
//		Assertions.assertTrue(model.getOrder(o1id).equals(data[0]));
//		Assertions.assertTrue(model.getOrder(o2id).equals(data[1]));
//		Assertions.assertTrue(model.getOrder(o3id).equals(data[2]));
	}
	@Test
	void getConfirmedOrderTest() {
		model.confirmAllOrders();
		OrderData[] data = model.getAllConfirmedOrders();
		Assertions.assertEquals(data.length, 3);
		OrderData[] gottenOrders = {model.getOrder(o1id), model.getOrder(o2id), model.getOrder(o3id)};
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(data, gottenOrders));
//		Assertions.assertTrue(model.getOrder(o1id).equals(data[0]));
//		Assertions.assertTrue(model.getOrder(o2id).equals(data[1]));
//		Assertions.assertTrue(model.getOrder(o3id).equals(data[2]));
	}
	@Test
	void getMixedOrderTest1() {
		this.confirmOrder(model, o1id);
//		model.confirmOrder(o1id+"#20200809112233343#0#0:item1,"+o1a1.toPlainString()+";");
		OrderData[] dataC = model.getAllConfirmedOrders();
		OrderData[] dataU = model.getAllUnconfirmedOrders();
		Assertions.assertEquals(dataC.length, 1);
		Assertions.assertEquals(dataU.length, 2);
		OrderData[] gottenCOrders = new OrderData[] {model.getOrder(o1id)};
		OrderData[] gottenUOrders = new OrderData[] {model.getOrder(o2id), model.getOrder(o3id)};
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(dataC, gottenCOrders));
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(dataU, gottenUOrders));
//		Assertions.assertTrue(model.getOrder(o1id).equals(dataC[0]));
//		Assertions.assertTrue(model.getOrder(o2id).equals(dataU[0]));
//		Assertions.assertTrue(model.getOrder(o3id).equals(dataU[1]));
	}
	@Test
	void getMixedOrderTest2() {
		this.confirmOrder(model, o1id);
		this.confirmOrder(model, o2id);
//		model.confirmOrder(o1id+"#20200809112233343#0#0:item1,"+o1a1.toPlainString()+";");
//		model.confirmOrder(o2id+"#20200809235959111#1#0:item1,"+o2a1.toPlainString()+";item2,"+o2a2.toPlainString()+";");
		OrderData[] dataC = model.getAllConfirmedOrders();
		OrderData[] dataU = model.getAllUnconfirmedOrders();
		Assertions.assertEquals(dataC.length, 2);
		Assertions.assertEquals(dataU.length, 1);
		OrderData[] gottenCOrders = new OrderData[] {model.getOrder(o1id), model.getOrder(o2id)};
		OrderData[] gottenUOrders = new OrderData[] {model.getOrder(o3id)};
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(dataC, gottenCOrders));
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(dataU, gottenUOrders));
//		Assertions.assertTrue(model.getOrder(o1id).equals(dataC[0]));
//		Assertions.assertTrue(model.getOrder(o2id).equals(dataC[1]));
//		Assertions.assertTrue(model.getOrder(o3id).equals(dataU[0]));
	}
	@Test
	void getNonExistentOrder() {
		Assertions.assertNull(model.getOrder("gfhidfklhfdhkgfsg"));
	}
}
