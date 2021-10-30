package model.dish;

import java.math.BigDecimal;

import model.dish.serialise.DishMenuParser;
import model.dish.serialise.ExternalDishMenuSerialiser;
import model.dish.serialise.FileDishMenuSerialiser;
import model.dish.serialise.IDishMenuDeserialiser;
import model.dish.serialise.IDishMenuParser;
import model.dish.serialise.IntraAppDishMenuItemSerialiser;
import model.dish.serialise.StandardDishMenuDeserialiser;
import model.filewriter.FileDishMenuFormat;
import model.id.EntityID;
import model.id.EntityIDFactory;
import model.id.FixIDFactory;

public class DishMenuHelper implements IDishMenuHelper {
	
	private EntityIDFactory entityIDFac;
	
	private IDishMenuFactory menuFac;
	private IDishMenuDataFactory menuDataFac;
	
	private IDishMenuItemFactory menuItemFac;
	private IDishMenuItemDataFactory menuItemDataFac;
	
	private IDishMenuDeserialiser deserialiser;
	private IDishMenuParser dishMenuParser;
	
	private ExternalDishMenuSerialiser externalDishMenuSerialiser;
	private IntraAppDishMenuItemSerialiser menuItemSerialiser;
	private FileDishMenuSerialiser fileMenuSerialiser;
	public DishMenuHelper() {
		this.entityIDFac = new FixIDFactory();
		
		this.menuFac = new DishMenuFactory();
		this.menuItemFac = new DishMenuItemFactory();
		this.menuItemDataFac = new DishMenuItemDataFactory();
		
		this.menuDataFac = new DishMenuDataFactory(this.menuItemDataFac);
		this.deserialiser = new StandardDishMenuDeserialiser();
		
		this.dishMenuParser = new DishMenuParser(new FileDishMenuFormat(), this.menuDataFac, this.entityIDFac);
		
		this.externalDishMenuSerialiser = new ExternalDishMenuSerialiser();
		this.menuItemSerialiser = new IntraAppDishMenuItemSerialiser();
		this.fileMenuSerialiser = new FileDishMenuSerialiser();
	}
	
	@Override
	public void setIDFactory(EntityIDFactory fac) {
		this.entityIDFac = fac;
	}

	@Override
	public void setDishMenuFactory(IDishMenuFactory fac) {
		this.menuFac = fac;
	}

	@Override
	public void setDishMenuDataFactory(IDishMenuDataFactory fac) {
		this.menuDataFac = fac;
	}

	@Override
	public void setDishMenuItemFactory(IDishMenuItemFactory fac) {
		this.menuItemFac = fac;
	}

	@Override
	public void setDishMenuItemDataFactory(IDishMenuItemDataFactory fac) {
		this.menuItemDataFac = fac;
	}

	@Override
	public void setDishMenuDeserialiser(IDishMenuDeserialiser deserialiser) {
		this.deserialiser = deserialiser;
	}
	
	@Override
	public void setAppSerialiser(IntraAppDishMenuItemSerialiser serialiser) {
		this.menuItemSerialiser = serialiser;
	}

	@Override
	public void setExternalSerialiser(ExternalDishMenuSerialiser serialiser) {
		this.externalDishMenuSerialiser = serialiser;
	}
	
	@Override
	public void setFileSerialiser(FileDishMenuSerialiser serialiser) {
		this.fileMenuSerialiser = serialiser;
	}
	
	@Override
	public IDishMenu createDishMenu() {
		return this.menuFac.createDishMenu();
	}

	@Override
	public IDishMenuData dishMenuToData(IDishMenu menu) {
		return this.menuDataFac.constructData(menu.getAllItems());
	}
	
	@Override
	public IDishMenuItem createDishMenuItem(String dishName, BigDecimal portionSize, BigDecimal price, BigDecimal productionCost, EntityID id) {
		return this.menuItemFac.createMenuItem(dishName, portionSize, price, productionCost, id);
	}

	@Override
	public IDishMenuItemData dishMenuItemToData(IDishMenuItem item) {
		return this.menuItemDataFac.menuItemToData(item);
	}

	@Override
	public IDishMenuItemData deserialiseDishMenuItem(String serialisedDishMenuItem) {
		return this.deserialiser.deserialise(serialisedDishMenuItem);
	}

	@Override
	public EntityID createID(Object... idParameters) {
		return this.entityIDFac.createID(idParameters);
	}

	@Override
	public IDishMenuData parseMenuData(String serialisedMenu) {
		return this.dishMenuParser.parseDishMenuData(serialisedMenu);
	}

	@Override
	public String serialiseMenuItemForApp(IDishMenuItemData menuItemData) {
		return this.menuItemSerialiser.serialise(menuItemData);
	}

	@Override
	public String serialiseForExternal(IDishMenuData menuData) {
		return this.externalDishMenuSerialiser.serialise(menuData);
	}

	@Override
	public String serialiseMenuItemForApp(String dishName, String id, BigDecimal portionSize, BigDecimal productionCost,
			BigDecimal price) {
		return this.menuItemSerialiser.serialise(dishName, id, portionSize, productionCost, price);
	}

	@Override
	public String serialiseMenuForFile(IDishMenuData menuData) {
		return this.fileMenuSerialiser.serialise(menuData);
	}
}
