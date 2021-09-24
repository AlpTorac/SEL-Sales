package test.model;

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
import model.dish.IDishMenuData;
import model.dish.IDishMenuItemData;
import model.dish.serialise.FileDishMenuSerialiser;
import model.dish.serialise.IDishMenuItemSerialiser;
import model.filewriter.FileDishMenuItemSerialiser;

class DishMenuWriterTest {
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
	}

	@Test
	void fileDishMenuItemSerialiserTest() {
		FileDishMenuItemSerialiser fdmis = new FileDishMenuItemSerialiser();
		IDishMenuItemData[] ds = model.getMenuData().getAllDishMenuItems();
		IDishMenuItemData d1 = ds[0];
		String s1 = fdmis.serialise(d1);
		Assertions.assertEquals(s1, i1id+","+i1Name+","+i1Disc.toPlainString()+".0,"+i1Price.toPlainString()+".0,"+i1PorSize.toPlainString()+","+i1ProCost.toPlainString()+".0");
		IDishMenuItemData d2 = ds[1];
		String s2 = fdmis.serialise(d2);
		Assertions.assertEquals(s2, i2id+","+i2Name+","+i2Disc.toPlainString()+","+i2Price.toPlainString()+".0,"+i2PorSize.toPlainString()+","+i2ProCost.toPlainString());
		IDishMenuItemData d3 = ds[2];
		String s3 = fdmis.serialise(d3);
		Assertions.assertEquals(s3, i3id+","+i3Name+","+i3Disc.toPlainString()+".0,"+i3Price.toPlainString()+".0,"+i3PorSize.toPlainString()+","+i3ProCost.toPlainString());
	}
	
	@Test
	void fileDishMenuSerialiserTest() {
		FileDishMenuSerialiser fdmis = new FileDishMenuSerialiser();
		String s = fdmis.serialise(model.getMenuData());
		Assertions.assertEquals(s, 
				i1id+","+i1Name+","+i1Disc.toPlainString()+".0,"+i1Price.toPlainString()+".0,"+i1PorSize.toPlainString()+","+i1ProCost.toPlainString()+".0;\n"+
				i2id+","+i2Name+","+i2Disc.toPlainString()+","+i2Price.toPlainString()+".0,"+i2PorSize.toPlainString()+","+i2ProCost.toPlainString()+";\n"+
				i3id+","+i3Name+","+i3Disc.toPlainString()+".0,"+i3Price.toPlainString()+".0,"+i3PorSize.toPlainString()+","+i3ProCost.toPlainString()+";\n"
		);
	}
	
	@Test
	void writeTest() {
		IDishMenuData menuData = model.getMenuData();
		IDishMenuItemData[] ds = menuData.getAllDishMenuItems();
		Assertions.assertEquals(ds.length, 3);
		Assertions.assertTrue(model.writeDishMenu());
		File f = new File("src/main/resources/dishMenuItems/file.txt");
		try {
			BufferedReader r = null;
			try {
				r = new BufferedReader(new FileReader(f));
			} catch (FileNotFoundException e) {
				f.delete();
				fail();
			}
			String[] ls = r.lines().toArray(String[]::new);
			if (ls.length != 3) {
				f.delete();
				fail();
			}
			ArrayList<String> lCol = new ArrayList<String>();
			for (String l : ls) {
				lCol.add(l);
			}
			Assertions.assertTrue(lCol.contains("item1,aaa,0.0,5.0,2.34,4.0;"));
			Assertions.assertTrue(lCol.contains("item2,bbb,0.1,1.0,5.67,0.5;"));
			Assertions.assertTrue(lCol.contains("item3,ccc,1.0,4.0,3.34,3.5;"));
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
