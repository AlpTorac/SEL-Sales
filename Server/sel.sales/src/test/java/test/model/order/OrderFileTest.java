package test.model.order;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.IModel;
import model.Model;
import model.dish.serialise.IDishMenuItemSerialiser;
import model.filewriter.FileOrderSerialiser;
import model.order.IOrderData;

class OrderFileTest {
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
	void fileOrderSerialiserTest() {
		FileOrderSerialiser fos = new FileOrderSerialiser();
		IOrderData[] ds = model.getAllUnconfirmedOrders();
		IOrderData d1 = ds[0];
		String s1 = fos.serialiseOrderData(d1);
		Assertions.assertEquals(s1, "order1,20200809,112233" +","+i1id+","+o1a1.toPlainString()+".0,"+"0,0,0;\n");
		IOrderData d2 = ds[1];
		String s2 = fos.serialiseOrderData(d2);
		Assertions.assertEquals(s2, "order2,20200809,235959" +","+i1id+","+o2a1.toPlainString()+".0,"+"1,0,0;\n"
		+"order2,20200809,235959" +","+i2id+","+o2a2.toPlainString()+".0,"+"1,0,1;\n");
		IOrderData d3 = ds[2];
		String s3 = fos.serialiseOrderData(d3);
		Assertions.assertEquals(s3, "order3,20200809,000000" +","+i3id+","+o3a3.toPlainString()+".0,"+"1,1,1;\n");
	}
	
	@Test
	void writeTest() {
		model.confirmOrder("order1-20200809112233343-0-0:item1,"+o1a1.toPlainString()+";");
		model.confirmOrder("order2-20200809235959111-1-0:item1,"+o2a1.toPlainString()+";item2,"+o2a2.toPlainString()+";");
		model.confirmOrder("order3-20200809000000222-1-1:item3,"+o3a3.toPlainString()+";");
		Assertions.assertEquals(model.getAllConfirmedOrders().length, 3);
		Assertions.assertTrue(model.writeOrders());
		File f = new File("src/main/resources/orders/file.txt");
		try {
			BufferedReader r = null;
			try {
				r = new BufferedReader(new FileReader(f));
			} catch (FileNotFoundException e) {
				f.delete();
				fail();
			}
			String[] ls = r.lines().toArray(String[]::new);
			Assertions.assertEquals(ls.length, 4);
			ArrayList<String> lCol = new ArrayList<String>();
			for (String l : ls) {
				lCol.add(l);
			}
			Assertions.assertTrue(lCol.contains("order1,20200809,112233,item1,2.0,0,0,0;"));
			Assertions.assertTrue(lCol.contains("order2,20200809,235959,item1,2.0,1,0,0;"));
			Assertions.assertTrue(lCol.contains("order2,20200809,235959,item2,3.0,1,0,1;"));
			Assertions.assertTrue(lCol.contains("order3,20200809,000000,item3,5.0,1,1,1;"));
			try {
				r.close();
			} catch (IOException e) {
				f.delete();
				fail();
			}
		} catch (Exception e) {
			f.delete();
			fail();
		}
		f.delete();
	}
	
}