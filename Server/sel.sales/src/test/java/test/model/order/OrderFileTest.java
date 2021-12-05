package test.model.order;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.order.OrderData;
import server.model.IServerModel;
import test.GeneralTestUtilityClass;
import test.FXTestTemplate;
//@Execution(value = ExecutionMode.SAME_THREAD)
class OrderFileTest extends FXTestTemplate {
	private IServerModel model;
	
	@BeforeEach
	void startUp() {
		this.cleanTestFolder();
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
	void orderSerialisationTest() {
		OrderData[] ds = new OrderData[] {oData1, oData2, oData3};
		OrderData d1 = ds[0];
		String s1 = orderDAO.serialiseValueObject(d1);
		OrderData d2 = ds[1];
		String s2 = orderDAO.serialiseValueObject(d2);
		OrderData d3 = ds[2];
		String s3 = orderDAO.serialiseValueObject(d3);
		
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(d1.getOrderedItems(), orderDAO.parseValueObject(s1).getOrderedItems()));
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(d2.getOrderedItems(), orderDAO.parseValueObject(s2).getOrderedItems()));
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(d3.getOrderedItems(), orderDAO.parseValueObject(s3).getOrderedItems()));
		Assertions.assertTrue(d1.equals(orderDAO.parseValueObject(s1)));
		Assertions.assertTrue(d2.equals(orderDAO.parseValueObject(s2)));
		Assertions.assertTrue(d3.equals(orderDAO.parseValueObject(s3)));
	}
	
	@Test
	void writeTest() {
		model.confirmOrder(oData1);
		model.confirmOrder(oData2);
		model.confirmOrder(oData3);
		Assertions.assertEquals(model.getAllConfirmedOrders().length, 3);
		Assertions.assertTrue(model.writeOrders());
		this.fileCheck();
	}
	
	@Test
	void noDuplicateWriteTest() {
		model.confirmOrder(oData1);
		model.confirmOrder(oData2);
		model.confirmOrder(oData3);
		Assertions.assertEquals(model.getAllConfirmedOrders().length, 3);
		Assertions.assertTrue(model.writeOrders());
		Assertions.assertEquals(model.getAllWrittenOrders().length, 3);
		this.fileCheck();
		model.close();
		model = this.initServerModel();
		model.loadSaved();
		Assertions.assertEquals(model.getAllWrittenOrders().length, 3);
		model.confirmOrder(oData1);
		model.confirmOrder(oData2);
		Assertions.assertEquals(model.getAllConfirmedOrders().length, 2);
		Assertions.assertTrue(model.writeOrders());
		this.fileCheck();
		model.close();
		model = this.initServerModel();
		model.loadSaved();
		Assertions.assertEquals(model.getAllWrittenOrders().length, 3);
		model.confirmOrder(oData2);
		model.confirmOrder(oData3);
		Assertions.assertEquals(model.getAllConfirmedOrders().length, 2);
		Assertions.assertTrue(model.writeOrders());
		this.fileCheck();
	}
	
	private void fileCheck() {
		File f = new File(this.testFolderAddress+File.separator+"orders.txt");
		try {
			BufferedReader r = null;
			try {
				r = new BufferedReader(new FileReader(f));
			} catch (FileNotFoundException e) {
				this.cleanTestFolder();
				fail();
			}
			String readContent = r.lines().reduce("", (l1,l2)->l1+System.lineSeparator()+l2);
			OrderData[] parsedDatas = orderDAO.parseValueObjects(readContent).toArray(OrderData[]::new);
			GeneralTestUtilityClass.arrayContentEquals(new OrderData[] {oData1, oData2, oData3}, parsedDatas);
			try {
				r.close();
			} catch (IOException e) {
				this.cleanTestFolder();
				fail();
			}
		} catch (Exception e) {
			this.cleanTestFolder();
			fail();
		}
	}
}
