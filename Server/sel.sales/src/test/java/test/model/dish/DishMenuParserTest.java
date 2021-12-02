package test.model.dish;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import model.dish.DishMenuDataFactory;
import model.dish.DishMenuItemDataFactory;
import model.dish.IDishMenuData;
import model.dish.IDishMenuDataFactory;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemDataFactory;
import model.dish.serialise.DishMenuParser;
import model.dish.serialise.ExternalDishMenuItemSerialiser;
import model.dish.serialise.IDishMenuFormat;
import model.dish.serialise.IDishMenuItemSerialiser;
import model.dish.serialise.IDishMenuParser;
import model.dish.serialise.IntraAppDishMenuFormat;
import model.entity.id.MinimalIDFactory;
//@Execution(value = ExecutionMode.SAME_THREAD)
class DishMenuParserTest {

	private IDishMenuFormat format = new IntraAppDishMenuFormat();
	private IDishMenuItemDataFactory itemDataFac = new DishMenuItemDataFactory();
	private IDishMenuDataFactory dataFac = new DishMenuDataFactory(itemDataFac);
	
	private IDishMenuParser parser = new DishMenuParser(format, dataFac, new MinimalIDFactory());
	private IDishMenuItemSerialiser serialiser = new ExternalDishMenuItemSerialiser();
	
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
	
	@Test
	void parserTest() {
		String serialisedMenu = serialiser.serialise(i1Name, i1id, i1PorSize, i1ProCost, i1Price)
				+ format.getDishMenuItemDataFieldEnd()
				+ serialiser.serialise(i2Name, i2id, i2PorSize, i2ProCost, i2Price)
				+ format.getDishMenuItemDataFieldEnd()
				+ serialiser.serialise(i3Name, i3id, i3PorSize, i3ProCost, i3Price)
				+ format.getDishMenuItemDataFieldEnd();
		IDishMenuData data = parser.parseDishMenuData(serialisedMenu);
		
		IDishMenuItemData[] itemData = data.getAllItems();
		
		Assertions.assertEquals(3, itemData.length);
		
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(itemData[0], i1Name, i1id, i1PorSize, i1Price, i1ProCost);
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(itemData[1], i2Name, i2id, i2PorSize, i2Price, i2ProCost);
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(itemData[2], i3Name, i3id, i3PorSize, i3Price, i3ProCost);
	}

}
