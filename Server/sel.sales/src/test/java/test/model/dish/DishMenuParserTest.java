package test.model.dish;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.dish.DishMenuItemData;
import server.model.IServerModel;
import test.TestTemplate;
//@Execution(value = ExecutionMode.SAME_THREAD)
class DishMenuParserTest extends TestTemplate {
	private IServerModel model;
	
	@BeforeEach
	void prep() {
		model = this.initServerModel();
		this.addDishMenuToServerModel(model);
	}
	
	@AfterEach
	void cleanUp() {
		model.close();
	}
	
	@Test
	void parserTest() {
		String serialisedMenu = this.model.serialiseMenuData();
		System.out.println(serialisedMenu);
		DishMenuItemData[] itemData = menuDAO.parseValueObjects(serialisedMenu).toArray(DishMenuItemData[]::new);
		
		Assertions.assertEquals(3, itemData.length);
		
		this.dishMenuItemDataAssertion(itemData);
	}

}
