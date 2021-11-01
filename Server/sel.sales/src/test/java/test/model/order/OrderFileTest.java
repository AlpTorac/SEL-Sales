package test.model.order;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import model.IModel;
import model.Model;
import model.dish.serialise.IDishMenuItemSerialiser;
import model.filewriter.FileOrderSerialiser;
import model.filewriter.OrderFile;
import model.filewriter.StandardOrderFile;
import model.order.IOrderData;
import test.GeneralTestUtilityClass;
@Execution(value = ExecutionMode.SAME_THREAD)
class OrderFileTest {
	private static IModel model;
	
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
	
	private String testFolder = "src"+File.separator+"test"+File.separator+"resources";
	
	private void initModel() {
		model = new Model();
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i1Name, i1id, i1PorSize, i1ProCost, i1Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i2Name, i2id, i2PorSize, i2ProCost, i2Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i3Name, i3id, i3PorSize, i3ProCost, i3Price));
		
		model.addUnconfirmedOrder("order1#20200809112233343#0#0:item1,"+o1a1.toPlainString()+";");
		model.addUnconfirmedOrder("order2#20200809235959111#1#0:item1,"+o2a1.toPlainString()+";item2,"+o2a2.toPlainString()+";");
		model.addUnconfirmedOrder("order3#20200809000000222#1#1:item3,"+o3a3.toPlainString()+";");
		
		model.setOrderFolderAddress(testFolder);
	}
	
	@BeforeEach
	void startUp() {
		GeneralTestUtilityClass.deletePathContent(new File(this.testFolder));
		this.initModel();
	}

	@AfterEach
	void cleanUp() {
		model.close();
		GeneralTestUtilityClass.deletePathContent(new File(this.testFolder));
	}
	
	@Test
	void fileOrderSerialiserTest() {
		FileOrderSerialiser fos = new FileOrderSerialiser();
		IOrderData[] ds = model.getAllUnconfirmedOrders();
		IOrderData d1 = ds[0];
		String s1 = fos.serialiseOrderData(d1);
		Assertions.assertEquals(s1, "order1#20200809112233343#0#0#0:item1,2.0;"+System.lineSeparator());
		IOrderData d2 = ds[1];
		String s2 = fos.serialiseOrderData(d2);
		Assertions.assertEquals(s2, "order2#20200809235959111#1#0#0:item1,2.0;" + System.lineSeparator()
				+ "order2#20200809235959111#1#0#0:item2,3.0;"+System.lineSeparator());
		IOrderData d3 = ds[2];
		String s3 = fos.serialiseOrderData(d3);
		Assertions.assertEquals(s3, "order3#20200809000000222#1#1#0:item3,5.0;"+System.lineSeparator());
	}
	
	@Test
	void writeTest() {
		model.confirmOrder("order1#20200809112233343#0#0:item1,"+o1a1.toPlainString()+";");
		model.confirmOrder("order2#20200809235959111#1#0:item1,"+o2a1.toPlainString()+";item2,"+o2a2.toPlainString()+";");
		model.confirmOrder("order3#20200809000000222#1#1:item3,"+o3a3.toPlainString()+";");
		Assertions.assertEquals(model.getAllConfirmedOrders().length, 3);
		Assertions.assertTrue(model.writeOrders());
		File f = new File(this.testFolder+File.separator+OrderFile.getDefaultFileNameForClass()+OrderFile.getExtensionForClass());
		try {
			BufferedReader r = null;
			try {
				r = new BufferedReader(new FileReader(f));
			} catch (FileNotFoundException e) {
				this.deleteFile(f);
				fail();
			}
			String[] ls = r.lines().toArray(String[]::new);
			if (ls.length != 4) {
				this.deleteFile(f);
				fail();
			}
			ArrayList<String> lCol = new ArrayList<String>();
			for (String l : ls) {
				lCol.add(l);
			}
			Assertions.assertTrue(lCol.contains("order1#20200809112233343#0#0#0:item1,2.0;"));
			Assertions.assertTrue(lCol.contains("order2#20200809235959111#1#0#0:item1,2.0;"));
			Assertions.assertTrue(lCol.contains("order2#20200809235959111#1#0#0:item2,3.0;"));
			Assertions.assertTrue(lCol.contains("order3#20200809000000222#1#1#0:item3,5.0;"));
			try {
				r.close();
			} catch (IOException e) {
				this.deleteFile(f);
				fail();
			}
		} catch (Exception e) {
			this.deleteFile(f);
			fail();
		}
		this.deleteFile(f);
	}
	
	@Test
	void noDuplicateWriteTest() {
		model.confirmOrder("order1#20200809112233343#0#0:item1,"+o1a1.toPlainString()+";");
		model.confirmOrder("order2#20200809235959111#1#0:item1,"+o2a1.toPlainString()+";item2,"+o2a2.toPlainString()+";");
		model.confirmOrder("order3#20200809000000222#1#1:item3,"+o3a3.toPlainString()+";");
		Assertions.assertEquals(model.getAllConfirmedOrders().length, 3);
		Assertions.assertTrue(model.writeOrders());
		model.close();
		this.initModel();
		model.loadSaved();
		model.confirmOrder("order1#20200809112233343#0#0:item1,"+o1a1.toPlainString()+";");
		model.confirmOrder("order2#20200809235959111#1#0:item1,"+o2a1.toPlainString()+";item2,"+o2a2.toPlainString()+";");
		Assertions.assertEquals(model.getAllConfirmedOrders().length, 2);
		Assertions.assertTrue(model.writeOrders());
		model.close();
		this.initModel();
		model.loadSaved();
		model.confirmOrder("order2#20200809235959111#1#0:item1,"+o2a1.toPlainString()+";item2,"+o2a2.toPlainString()+";");
		model.confirmOrder("order3#20200809000000222#1#1:item3,"+o3a3.toPlainString()+";");
		Assertions.assertEquals(model.getAllConfirmedOrders().length, 2);
		Assertions.assertTrue(model.writeOrders());
		
		File f = new File(this.testFolder+File.separator+OrderFile.getDefaultFileNameForClass()+OrderFile.getExtensionForClass());
		try {
			BufferedReader r = null;
			try {
				r = new BufferedReader(new FileReader(f));
			} catch (FileNotFoundException e) {
				this.deleteFile(f);
				fail();
			}
			String[] ls = r.lines().toArray(String[]::new);
			if (ls.length != 4) {
				this.deleteFile(f);
				fail();
			}
			ArrayList<String> lCol = new ArrayList<String>();
			for (String l : ls) {
				lCol.add(l);
			}
			Assertions.assertTrue(lCol.contains("order1#20200809112233343#0#0#0:item1,2.0;"));
			Assertions.assertTrue(lCol.contains("order2#20200809235959111#1#0#0:item1,2.0;"));
			Assertions.assertTrue(lCol.contains("order2#20200809235959111#1#0#0:item2,3.0;"));
			Assertions.assertTrue(lCol.contains("order3#20200809000000222#1#1#0:item3,5.0;"));
			try {
				r.close();
			} catch (IOException e) {
				this.deleteFile(f);
				fail();
			}
		} catch (Exception e) {
			this.deleteFile(f);
			fail();
		}
		this.deleteFile(f);
	}
	private void deleteFile(File f) {
		GeneralTestUtilityClass.deletePathContent(new File(this.testFolder));
	}
}
