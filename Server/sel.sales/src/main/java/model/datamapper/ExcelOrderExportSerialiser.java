package model.datamapper;

import java.io.File;
import java.util.Collection;
import java.util.function.Function;

import model.datamapper.order.OrderAttribute;
import model.dish.DishMenuItemData;
import model.entity.AccumulatingAggregateEntry;
import model.order.OrderData;

public abstract class ExcelOrderExportSerialiser extends ExportSerialiser<OrderAttribute, OrderData> {

	private Collection<Function<OrderData, String>> orderSpecificFieldFunctions;
	private Collection<Function<AccumulatingAggregateEntry<DishMenuItemData>, String>> itemSpecificFunctions;

	public ExcelOrderExportSerialiser(Collection<Function<OrderData, String>> orderSpecificFieldFunctions,
			Collection<Function<AccumulatingAggregateEntry<DishMenuItemData>, String>> itemSpecificFunctions) {
		this.orderSpecificFieldFunctions = orderSpecificFieldFunctions;
		this.itemSpecificFunctions = itemSpecificFunctions;
	}

	public Collection<Function<OrderData, String>> getOrderSpecificFieldFunctions() {
		return this.orderSpecificFieldFunctions;
	}
	
	public Collection<Function<AccumulatingAggregateEntry<DishMenuItemData>, String>> getItemSpecificFunctions() {
		return this.itemSpecificFunctions;
	}
	
	@Override
	public String serialise(OrderData valueObject) {
		String result = "";
		
		for (AccumulatingAggregateEntry<DishMenuItemData> item : valueObject.getOrderedItems()) {
			result += this.getOrderSpecificFieldFunctions().stream()
					.map(f -> f.apply(valueObject))
					.reduce((f1,f2)->f1+this.getFieldSeparator()+f2).get();
			result += this.getFieldSeparator();
			result += this.getItemSpecificFunctions().stream()
					.map(f -> f.apply(item))
					.reduce((f1,f2)->f1+this.getFieldSeparator()+f2).get();
			result += this.getRowEnd() + System.lineSeparator();
		}
		
		return result;
	}

}