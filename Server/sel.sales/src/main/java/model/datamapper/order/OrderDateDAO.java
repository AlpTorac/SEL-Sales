package model.datamapper.order;

import java.time.LocalDateTime;

import model.DateSettings;

public class OrderDateDAO extends OrderAttributeDAO {
	private DateSettings ds;
	
	protected OrderDateDAO(String fileAddress, String defaultFileName, DateSettings ds) {
		super(fileAddress, defaultFileName);
		this.ds = ds;
	}
	
	@Override
	protected String serialiseNotNullValue(Object attributeValue) {
		return this.ds.serialiseDate((LocalDateTime) attributeValue);
	}

	@Override
	protected LocalDateTime parseNotNullSerialisedValue(String serialisedValue) {
		return this.ds.parseDate(serialisedValue);
	}

	@Override
	protected OrderAttribute getAssociatedAttribute() {
		return OrderAttribute.DATE;
	}
}
