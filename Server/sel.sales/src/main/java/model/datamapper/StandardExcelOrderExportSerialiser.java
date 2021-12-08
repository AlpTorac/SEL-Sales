package model.datamapper;

import java.util.ArrayList;
import java.util.function.Function;

import model.dish.DishMenuItemData;
import model.entity.AccumulatingAggregateEntry;
import model.order.OrderData;

public class StandardExcelOrderExportSerialiser extends ExcelOrderExportSerialiser {

	public StandardExcelOrderExportSerialiser() {
		super(new ArrayList<Function<OrderData, String>>(),
			new ArrayList<Function<AccumulatingAggregateEntry<DishMenuItemData>, String>>());
		
		this.getOrderSpecificFieldFunctions().add(v -> v.getID().toString());
		this.getOrderSpecificFieldFunctions().add(v -> String.valueOf(v.getDate().getDayOfMonth()));
		this.getOrderSpecificFieldFunctions().add(v -> String.valueOf(v.getDate().getHour())+String.valueOf(v.getDate().getMinute()));
		this.getOrderSpecificFieldFunctions().add(v -> this.serialiseBoolean(v.getIsCash()));
		this.getOrderSpecificFieldFunctions().add(v -> this.serialiseBoolean(v.getIsHere()));
		this.getOrderSpecificFieldFunctions().add(v -> this.serialiseBoolean(v.getIsDiscounted()));
		
		this.getItemSpecificFunctions().add(i -> i.getItem().getDishName());
		this.getItemSpecificFunctions().add(i -> i.getAmount().toPlainString());
		this.getItemSpecificFunctions().add(i -> i.getItem().getGrossPrice().toPlainString());
		this.getItemSpecificFunctions().add(i -> i.getItem().getGrossPrice().multiply(i.getAmount()).toPlainString());
	}
}
