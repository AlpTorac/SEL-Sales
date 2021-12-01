package archive.fields;

public class DishMenuItemInfo extends FieldInfo {
	DishMenuItemInfo() {
		super();
		this.addFieldSeparator("");
		this.addField(DishMenuItemFieldNames.DISH_NAME, FieldClass.STRING);
		this.addFieldSeparator(",");
		this.addField(DishMenuItemFieldNames.DISH_ID, FieldClass.STRING);
		this.addFieldSeparator(",");
		this.addField(DishMenuItemFieldNames.PORTION_SIZE, FieldClass.BIGDECIMAL);
		this.addFieldSeparator(",");
		this.addField(DishMenuItemFieldNames.PRODUCTION_COST, FieldClass.BIGDECIMAL);
		this.addFieldSeparator(",");
		this.addField(DishMenuItemFieldNames.PRICE, FieldClass.BIGDECIMAL);
		this.addFieldSeparator(";"+System.lineSeparator());
	}
}
