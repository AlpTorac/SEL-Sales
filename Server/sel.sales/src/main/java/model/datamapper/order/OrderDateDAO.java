package model.datamapper.order;

import java.time.LocalDateTime;

import model.DateSettings;
import model.datamapper.AttributeDAO;

public class OrderDateDAO extends OrderAttributeDAO {
	private DateSettings ds;
	
	protected OrderDateDAO(DateSettings ds) {
		super();
		this.ds = ds;
	}
	
	@Override
	protected String serialiseNotNullValue(Object attributeValue) {
		return this.ds.serialiseDateWithoutSeparators((LocalDateTime) attributeValue);
	}

	@Override
	protected LocalDateTime parseNotNullSerialisedValue(String serialisedValue) {
		return this.ds.parseDateWithoutSeparators(serialisedValue);
	}

	@Override
	protected OrderAttribute getAssociatedAttribute() {
		return OrderAttribute.DATE;
	}
}
