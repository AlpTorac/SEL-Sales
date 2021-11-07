package test.model.dish;

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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import model.IModel;
import model.Model;
import model.dish.IDishMenuData;
import model.dish.IDishMenuItemData;
import model.dish.serialise.FileDishMenuSerialiser;
import model.dish.serialise.IDishMenuItemSerialiser;
import model.filewriter.DishMenuFile;
import model.filewriter.FileDishMenuItemSerialiser;
import model.filewriter.OrderFile;
import model.settings.SettingsField;
import test.GeneralTestUtilityClass;
@Execution(value = ExecutionMode.SAME_THREAD)
class DishMenuWriterTest {
	private static IModel model;
	
	private BufferedReader r;
	
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
	
	private static String testFolder = "src"+File.separator+"test"+File.separator+"resources";
	
	private void initModel() {
		model = new Model();
		model.addSetting(SettingsField.DISH_MENU_FOLDER, testFolder);
	}
	
	@BeforeEach
	void startUp() {
		GeneralTestUtilityClass.deletePathContent(new File(testFolder));
		model = new Model();
		model.addSetting(SettingsField.DISH_MENU_FOLDER, testFolder);
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i1Name, i1id, i1PorSize, i1ProCost, i1Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i2Name, i2id, i2PorSize, i2ProCost, i2Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i3Name, i3id, i3PorSize, i3ProCost, i3Price));
	}
	
	@AfterEach
	void cleanUp() {
		model.close();
		GeneralTestUtilityClass.deletePathContent(new File(testFolder));
	}

	@Test
	void fileDishMenuItemSerialiserTest() {
		FileDishMenuItemSerialiser fdmis = new FileDishMenuItemSerialiser();
		IDishMenuItemData[] ds = model.getMenuData().getAllDishMenuItems();
		IDishMenuItemData d1 = ds[0];
		String s1 = fdmis.serialise(d1);
		Assertions.assertEquals(s1, i1Name+","+i1id+","+i1PorSize.toPlainString()+","+i1ProCost.toPlainString()+".0"+","+i1Price.toPlainString()+".0");
//		IDishMenuItemData d2 = ds[1];
//		String s2 = fdmis.serialise(d2);
//		Assertions.assertEquals(s2, i2Name+","+i2id+","+i2ProCost.toPlainString()+","+i2Price.toPlainString()+".0,"+i2PorSize.toPlainString());
//		IDishMenuItemData d3 = ds[2];
//		String s3 = fdmis.serialise(d3);
//		Assertions.assertEquals(s3, i3Name+","+i3id+","+i3ProCost.toPlainString()+","+i3Price.toPlainString()+".0,"+i3PorSize.toPlainString());
	}
	
	@Test
	void fileDishMenuSerialiserTest() {
		FileDishMenuSerialiser fdmis = new FileDishMenuSerialiser();
		String s = fdmis.serialise(model.getMenuData());
		Assertions.assertEquals(s, 
				i1Name+","+i1id+","+i1PorSize.toPlainString()+","+i1ProCost.toPlainString()+".0"+","+i1Price.toPlainString()+".0"+";"+System.lineSeparator()+
				i2Name+","+i2id+","+i2PorSize.toPlainString()+","+i2ProCost.toPlainString()+","+i2Price.toPlainString()+".0"+";"+System.lineSeparator()+
				i3Name+","+i3id+","+i3PorSize.toPlainString()+","+i3ProCost.toPlainString()+","+i3Price.toPlainString()+".0"+";"+System.lineSeparator()
		);
	}
	
	@Test
	void writeTest() {
		IDishMenuData menuData = model.getMenuData();
		IDishMenuItemData[] ds = menuData.getAllDishMenuItems();
		Assertions.assertEquals(ds.length, 3);
		Assertions.assertTrue(model.writeDishMenu());
		File f = new File(testFolder+File.separator+DishMenuFile.getDefaultFileNameForClass()+DishMenuFile.getExtensionForClass());
		try {
			try {
				r = new BufferedReader(new FileReader(f));
			} catch (FileNotFoundException e) {
				this.deleteFile(f);
				fail();
			}
			String[] ls = r.lines().toArray(String[]::new);
			if (ls.length != 3) {
				this.deleteFile(f);
				fail("Menu lines length (expected/actual): " + 3 + "/" + ls.length);
			}
			ArrayList<String> lCol = new ArrayList<String>();
			for (String l : ls) {
				lCol.add(l);
			}
			Assertions.assertTrue(lCol.contains("aaa,item1,2.34,4.0,5.0;"));
			Assertions.assertTrue(lCol.contains("bbb,item2,5.67,0.5,1.0;"));
			Assertions.assertTrue(lCol.contains("ccc,item3,3.34,3.5,4.0;"));
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
		Assertions.assertEquals(model.getMenuData().getAllDishMenuItems().length, 3);
		Assertions.assertTrue(model.writeDishMenu());
		model.close();
		this.initModel();
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i1Name, i1id, i1PorSize, i1ProCost, i1Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i2Name, i2id, i2PorSize, i2ProCost, i2Price));
		Assertions.assertEquals(model.getMenuData().getAllDishMenuItems().length, 2);
		Assertions.assertTrue(model.writeDishMenu());
		model.close();
		this.initModel();
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i1Name, i1id, i1PorSize, i1ProCost, i1Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i2Name, i2id, i2PorSize, i2ProCost, i2Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i3Name, i3id, i3PorSize, i3ProCost, i3Price));
		Assertions.assertEquals(model.getMenuData().getAllDishMenuItems().length, 3);
		Assertions.assertTrue(model.writeDishMenu());
		
		File f = new File(testFolder+File.separator+DishMenuFile.getDefaultFileNameForClass()+DishMenuFile.getExtensionForClass());
		try {
			try {
				r = new BufferedReader(new FileReader(f));
			} catch (FileNotFoundException e) {
				this.deleteFile(f);
				fail();
			}
			String[] ls = r.lines().toArray(String[]::new);
			if (ls.length != 3) {
				this.deleteFile(f);
				fail("Menu lines length (expected/actual): " + 3 + "/" + ls.length);
			}
			ArrayList<String> lCol = new ArrayList<String>();
			for (String l : ls) {
				lCol.add(l);
			}
			Assertions.assertTrue(lCol.contains("aaa,item1,2.34,4.0,5.0;"));
			Assertions.assertTrue(lCol.contains("bbb,item2,5.67,0.5,1.0;"));
			Assertions.assertTrue(lCol.contains("ccc,item3,3.34,3.5,4.0;"));
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
		try {
			r.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		f.delete();
		f.deleteOnExit();
		GeneralTestUtilityClass.deletePathContent(new File(testFolder));
	}
}
