package test.model.settings;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.IModel;
import model.settings.SettingsField;
import model.settings.TableNumberContainer;
import server.model.ServerModel;
import test.GeneralTestUtilityClass;

class TableNumberTest {
	
	private IModel model;
	
	private String tableRanges = "4-6,1-2,1,2,10,90";
	private Integer[] existingNumbers = new Integer[] {1,2,4,5,6,10,90};
	
	private Collection<Integer> actualExistingNumbers;
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
	@BeforeEach
	void prep() {
		GeneralTestUtilityClass.deletePathContent(testFolderAddress);
		model = new ServerModel(this.testFolderAddress);
		model.addSetting(SettingsField.TABLE_NUMBERS, tableRanges);
		this.actualExistingNumbers = model.getTableNumbers();
	}

	@AfterEach
	void cleanUp() {
		model.close();
		GeneralTestUtilityClass.deletePathContent(testFolderAddress);
	}

	private boolean checkTableNumber(int number) {
		return actualExistingNumbers.stream().anyMatch(i -> i.intValue() == number);
	}
	
	@Test
	void initTest() {
		boolean tableExists;
		
		for (int i = 0; i < 100; i++) {
			tableExists = GeneralTestUtilityClass.arrayContains(existingNumbers, Integer.valueOf(i));
			Assertions.assertEquals(this.checkTableNumber(i), tableExists, "Table " + i);
			Assertions.assertEquals(this.model.tableExists(i), tableExists, "Table " + i);
		}
	}

}
