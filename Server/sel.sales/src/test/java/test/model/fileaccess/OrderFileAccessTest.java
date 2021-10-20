package test.model.fileaccess;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import model.IModel;
import model.Model;
import model.dish.serialise.IDishMenuItemSerialiser;
import model.filewriter.FileAccess;
import model.filewriter.OrderFile;
import model.filewriter.StandardOrderFile;
import model.order.IOrderData;
import test.GeneralTestUtilityClass;

@Execution(value = ExecutionMode.SAME_THREAD)
class OrderFileAccessTest {
	private IModel model;
	private static IDishMenuItemSerialiser serialiser;
	
	private String i1Name = "aaa";
	private BigDecimal i1PorSize = BigDecimal.valueOf(2.34);
	private BigDecimal i1Price = BigDecimal.valueOf(5);
	private BigDecimal i1ProCost = BigDecimal.valueOf(4);
	private String i1id = "item1";
	
	private String i2Name = "bbb";
	private BigDecimal i2PorSize = BigDecimal.valueOf(5.67);
	private BigDecimal i2Price = BigDecimal.valueOf(1);
	private BigDecimal i2ProCost = BigDecimal.valueOf(0.5);
	private String i2id = "item2";
	
	private String i3Name = "ccc";
	private BigDecimal i3PorSize = BigDecimal.valueOf(3.34);
	private BigDecimal i3Price = BigDecimal.valueOf(4);
	private BigDecimal i3ProCost = BigDecimal.valueOf(3.5);
	private String i3id = "item3";
	
	private OrderFile of;
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
//	private String orderFileNAE = OrderFile.getDefaultFileNameForClass()+FileAccess.getExtensionForClass();
	
	private IOrderData[] orderData;
	
//	private String getOrderAddress() {
//		return this.testFolderAddress+File.separator+this.orderFileNAE;
//	}
	
	@BeforeEach
	void prep() {
		model = new Model();
		serialiser = model.getDishMenuItemSerialiser();
		model.addMenuItem(serialiser.serialise(i1Name, i1id, i1PorSize, i1ProCost, i1Price));
		model.addMenuItem(serialiser.serialise(i2Name, i2id, i2PorSize, i2ProCost, i2Price));
		model.addMenuItem(serialiser.serialise(i3Name, i3id, i3PorSize, i3ProCost, i3Price));
		
		model.addUnconfirmedOrder("order1#20200809235959865#0#0:item3,2;");
		model.addUnconfirmedOrder("order2#20200809235959866#1#0:item1,2;item2,3;");
		model.addUnconfirmedOrder("order3#20200809235959867#1#1:item2,2;item3,3;");
		
		of = new StandardOrderFile(this.testFolderAddress,
				GeneralTestUtilityClass.getPrivateFieldValue(model, "finder"),
				GeneralTestUtilityClass.getPrivateFieldValue(model, "orderDataFac"),
				GeneralTestUtilityClass.getPrivateFieldValue(model, "dishMenuItemDataFac"));
		this.fillOrderFile();
	}
	
	private void fillOrderFile() {
		orderData = model.getAllUnconfirmedOrders();
		of.writeOrderData(orderData);
	}

	@AfterEach
	void cleanUp() {
		of.deleteFile();
		GeneralTestUtilityClass.deletePathContent(new File(this.testFolderAddress));
	}
	
//	@Test
//	void addressTest() {
//		Assertions.assertEquals(this.getOrderAddress(), of.getFilePath());
//	}
	
	@Test
	void loadTest() {
		IOrderData[] ss = of.loadOrders();
		for (int i = 0; i < ss.length; i++) {
			GeneralTestUtilityClass.arrayContains(orderData, ss);
		}
	}
	
//	@Test
//	void deleteTest() {
//		Assertions.assertTrue((new File(this.getOrderAddress())).exists());
//		Assertions.assertTrue(of.deleteFile());
//		Assertions.assertTrue(!(new File(this.getOrderAddress())).exists());
//	}
	
//	@Test
//	void changeFolderAddressTest() {
//		String newFolderAddress = this.testFolderAddress+File.separator+"subfolder";
//		File folder = new File(newFolderAddress);
//		folder.mkdir();
//		Assertions.assertTrue(folder.exists() && folder.isDirectory());
//		of.setFolderAddress(folder.getPath());
//		Assertions.assertEquals(of.getFolderAddress(), newFolderAddress);
//	}
	
//	@Test
//	void migrationTest() {
//		String newFolderAddress = this.testFolderAddress+File.separator+"subfolder";
//		File folder = new File(newFolderAddress);
//		folder.mkdir();
//		Assertions.assertTrue(folder.exists() && folder.isDirectory());
//		of.setFolderAddress(folder.getPath());
//		String newFileAddress = newFolderAddress+File.separator+this.orderFileNAE;
//		Assertions.assertEquals(of.getFilePath(), newFileAddress);
//		this.fillOrderFile();
////		Assertions.assertTrue(orderData.equals(of.loadOrders()));
//		Assertions.assertTrue(of.deleteFile());
//	}
}
