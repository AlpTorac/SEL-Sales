package test.model.order;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.model.IServerModel;
import test.FXTestTemplate;
//@Execution(value = ExecutionMode.SAME_THREAD)
class OrderAccumulationTest extends FXTestTemplate {
	private static IServerModel model;
	
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
	void accumulationTest() {
		BigDecimal o1a12 = BigDecimal.valueOf(2);
		Assertions.assertTrue(oData1.getOrderedItemAmount(iData1.getID()).compareTo(o1a1) == 0);
		Assertions.assertEquals(oData1.getOrderedItems().length, 1);
		
		BigDecimal newAmount = o1a1.add(o1a12);
		oData1.addOrderItem(iData1, o1a12);
		Assertions.assertTrue(oData1.getOrderedItemAmount(iData1.getID()).compareTo(newAmount) == 0);
		Assertions.assertEquals(oData1.getOrderedItems().length, 1);
		
		BigDecimal o1a21 = BigDecimal.valueOf(5);
		oData1.addOrderItem(iData2, o1a21);
		Assertions.assertTrue(oData1.getOrderedItemAmount(iData1.getID()).compareTo(newAmount) == 0);
		Assertions.assertTrue(oData1.getOrderedItemAmount(iData2.getID()).compareTo(o1a21) == 0);
		Assertions.assertEquals(oData1.getOrderedItems().length, 2);
		
		oData1.removeOrderItem(iData2, o1a21);
		Assertions.assertTrue(oData1.getOrderedItemAmount(iData1.getID()).compareTo(newAmount) == 0);
		Assertions.assertEquals(oData1.getOrderedItems().length, 1);
		
		oData1.removeOrderItem(iData1, newAmount);
		Assertions.assertTrue(oData1.getOrderedItemAmount(iData1.getID()).compareTo(BigDecimal.ZERO) == 0);
		Assertions.assertEquals(oData1.getOrderedItems().length, 0);
	}
}
