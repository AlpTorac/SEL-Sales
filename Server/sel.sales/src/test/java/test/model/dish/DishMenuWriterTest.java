package test.model.dish;

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
import model.dish.DishMenuItemData;
import server.model.IServerModel;
import test.GeneralTestUtilityClass;
import test.FXTestTemplate;
//@Execution(value = ExecutionMode.SAME_THREAD)
class DishMenuWriterTest extends FXTestTemplate {
	private IServerModel model;
	private BufferedReader r;
	
	@BeforeEach
	void startUp() {
		model = this.initServerModel();
		this.addDishMenuToServerModel(model);
	}
	
	@AfterEach
	void cleanUp() {
		this.closeModel(model);
	}

	@Test
	void dishMenuItemSerialisationTest() {
		DishMenuItemData[] ds = new DishMenuItemData[] {iData1, iData2, iData3};
		DishMenuItemData d1 = ds[0];
		String s1 = menuDAO.serialiseValueObject(d1);
		DishMenuItemData d2 = ds[1];
		String s2 = menuDAO.serialiseValueObject(d2);
		DishMenuItemData d3 = ds[2];
		String s3 = menuDAO.serialiseValueObject(d3);
		
		Assertions.assertTrue(d1.equals(menuDAO.parseValueObject(s1)));
		Assertions.assertTrue(d2.equals(menuDAO.parseValueObject(s2)));
		Assertions.assertTrue(d3.equals(menuDAO.parseValueObject(s3)));
	}
	
	@Test
	void writeTest() {
		DishMenuItemData[] ds = model.getMenuData().getAllElements().toArray(DishMenuItemData[]::new);
		Assertions.assertEquals(ds.length, 3);
		Assertions.assertTrue(model.writeDishMenu());
		this.fileCheck();
	}
	
	@Test
	void noDuplicateWriteTest() {
		Assertions.assertEquals(model.getMenuData().getAllElements().size(), 3);
		Assertions.assertTrue(model.writeDishMenu());
		this.fileCheck();
		model.close();
		model = this.initServerModel();
		this.addMenuItemToServerModel(model, i1Name, i1id, i1PorSize, i1Price, i1ProCost);
		this.addMenuItemToServerModel(model, i2Name, i2id, i2PorSize, i2Price, i2ProCost);
		Assertions.assertEquals(model.getMenuData().getAllElements().size(), 2);
		Assertions.assertTrue(model.writeDishMenu());
		this.fileCheck();
		model.close();
		model = this.initServerModel();
		this.addMenuItemToServerModel(model, i1Name, i1id, i1PorSize, i1Price, i1ProCost);
		this.addMenuItemToServerModel(model, i2Name, i2id, i2PorSize, i2Price, i2ProCost);
		this.addMenuItemToServerModel(model, i3Name, i3id, i3PorSize, i3Price, i3ProCost);
		Assertions.assertEquals(model.getMenuData().getAllElements().size(), 3);
		Assertions.assertTrue(model.writeDishMenu());
		this.fileCheck();
	}
	
	private void fileCheck() {
		File f = new File(this.testFolderAddress+File.separator+"menu.txt");
		try {
			try {
				r = new BufferedReader(new FileReader(f));
			} catch (FileNotFoundException e) {
				this.cleanTestFolder();
				fail();
			}
			String readContent = r.lines().reduce("", (l1,l2)->l1+System.lineSeparator()+l2);
			DishMenuItemData[] parsedDatas = menuDAO.parseValueObjects(readContent).toArray(DishMenuItemData[]::new);
			GeneralTestUtilityClass.arrayContentEquals(new DishMenuItemData[] {iData1, iData2, iData3}, parsedDatas);
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
