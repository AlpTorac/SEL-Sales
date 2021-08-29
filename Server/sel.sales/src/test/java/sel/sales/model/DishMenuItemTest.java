package sel.sales.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import model.IModel;
import model.Model;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemIDFactory;

class DishMenuItemTest {
	private static IModel model;
	private static IDishMenuItemDataFactory menuItemDataFac;
	private static IDishMenuItemIDFactory menuItemIDFac;
	
	@BeforeAll
	static void startUp() {
		model = new Model();
		menuItemDataFac = model.getItemDataCommunicationProtocoll();
		menuItemIDFac = model.getItemIDCommunicationProtocoll();
		
		model.addMenuItem(menuItemDataFac.constructData(
				"aaa",
				BigDecimal.valueOf(2.34),
				BigDecimal.valueOf(5),
				BigDecimal.valueOf(4),
				"item1", menuItemIDFac));
		
		model.addMenuItem(menuItemDataFac.constructData(
				"bbb",
				BigDecimal.valueOf(5.67),
				BigDecimal.valueOf(1),
				BigDecimal.valueOf(0.5),
				"item2", menuItemIDFac));
		
		model.addMenuItem(menuItemDataFac.constructData(
				"ccc",
				BigDecimal.valueOf(3.34),
				BigDecimal.valueOf(4),
				BigDecimal.valueOf(3.5),
				"item3", menuItemIDFac));
	}
	
	@Test
	void test() {
		IDishMenuItemData[] data = model.getMenuData();
		
		assertMenuItemDataEqual(data[0], "aaa", "item1", BigDecimal.valueOf(2.34), BigDecimal.valueOf(5), BigDecimal.valueOf(4));
		assertMenuItemDataEqual(data[1], "bbb", "item2", BigDecimal.valueOf(5.67), BigDecimal.valueOf(1), BigDecimal.valueOf(0.5));
		assertMenuItemDataEqual(data[2], "ccc", "item3", BigDecimal.valueOf(3.34), BigDecimal.valueOf(4), BigDecimal.valueOf(3.5));
	}
	
	private static void assertMenuItemDataEqual(IDishMenuItemData data, String dishName, String id, BigDecimal portionSize, BigDecimal price, BigDecimal productionCost) {
		Assertions.assertEquals(data.getDishName(), dishName);
		Assertions.assertEquals(data.getId().getID(), id);
		Assertions.assertEquals(data.getPortionSize().compareTo(portionSize), 0);
		Assertions.assertEquals(data.getPrice().compareTo(price), 0);
		Assertions.assertEquals(data.getProductionCost().compareTo(productionCost), 0);
	}

}
