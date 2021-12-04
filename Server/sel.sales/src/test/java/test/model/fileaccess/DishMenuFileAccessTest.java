package test.model.fileaccess;

import java.io.File;
import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import model.dish.DishMenuData;
import server.model.IServerModel;
import server.model.ServerModel;
import test.GeneralTestUtilityClass;
//@Execution(value = ExecutionMode.SAME_THREAD)
class DishMenuFileAccessTest {
	private IServerModel model;
	
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
	
	private DishMenuFile dmf;
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
	private DishMenuData dishMenuData;
	
	@BeforeEach
	void prep() {
		GeneralTestUtilityClass.deletePathContent(new File(this.testFolderAddress));
		model = new ServerModel(this.testFolderAddress);
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i1Name, i1id, i1PorSize, i1ProCost, i1Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i2Name, i2id, i2PorSize, i2ProCost, i2Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i3Name, i3id, i3PorSize, i3ProCost, i3Price));
		
		dmf = new StandardDishMenuFile(this.testFolderAddress);
		this.fillMenuFile();
	}
	
	private void fillMenuFile() {
		dishMenuData = model.getMenuData();
		dmf.writeToFile(model.getDishMenuHelper().serialiseMenuForFile(dishMenuData));
	}

	@AfterEach
	void cleanUp() {
		dmf.close();
		model.close();
		dmf.deleteFile();
		GeneralTestUtilityClass.deletePathContent(new File(this.testFolderAddress));
	}
	
	@Test
	void loadTest() {
		DishMenuData s = model.getDishMenuHelper().parseFileMenuData(dmf.readFile());
		Assertions.assertTrue(dishMenuData.equals(s));
	}
}
