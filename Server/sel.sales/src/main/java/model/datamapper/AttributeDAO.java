package model.datamapper;

import model.entity.Entity;
import model.entity.Repository;
import model.entity.ValueObject;
import model.entity.id.EntityID;
import model.entity.id.EntityIDFactory;
import model.entity.id.MinimalIDFactory;
import model.util.IParser;
import model.util.ISerialiser;

public abstract class AttributeDAO<A extends IAttribute, E extends Entity<A>, V extends ValueObject<A>, C extends Repository<A, E, V>> implements IParser, ISerialiser {
	private AttributeFormat attributeFormat = new AttributeFormat();
	private AggregateFormat aggregateFormat = new AggregateFormat();
	private EntityIDFactory idFac = new MinimalIDFactory();
	
	protected AttributeDAO() {
		
	}
	
	public void setAttributeAlgorithm(C col, EntityID attributeOwnerID, String serialisedValue) {
		col.setAttributeValue(this.getAssociatedAttribute(), attributeOwnerID, this.parseSerialisedValue(serialisedValue));
	}
	
	public void setAttributeAlgorithm(E attributeOwner, String serialisedValue) {
		attributeOwner.setAttributeValue(this.getAssociatedAttribute(), this.parseSerialisedValue(serialisedValue));
	}
	
	public void setAttributeAlgorithm(V attributeOwner, String serialisedValue) {
		attributeOwner.setAttributeValue(this.getAssociatedAttribute(), this.parseSerialisedValue(serialisedValue));
	}
	
	protected String serialiseNull(Object attributeValue) {
		return "";
	}
	
	protected String serialiseValue(Object attributeValue) {
		if (attributeValue == null) {
			return this.serialiseNull(attributeValue);
		}
		return this.serialiseNotNullValue(attributeValue);
	}
	protected Object parseSerialisedValue(String serialisedValue) {
		if (serialisedValue == null || serialisedValue.length() == 0) {
			return this.getAssociatedAttribute().getDefaultValue();
		}
		return this.parseNotNullSerialisedValue(serialisedValue);
	}
	protected abstract String serialiseNotNullValue(Object attributeValue);
	protected abstract Object parseNotNullSerialisedValue(String serialisedValue);
	
	protected abstract A getAssociatedAttribute();
	
	public String serialiseAll(C col) {
		String result = "";
		result += this.getAttributeFormat().getAttributeStart();
		EntityID currentID;
		for (E data : col.getAllElements()) {
			currentID = data.getID();
			
//			result += this.getFormat().getFieldStart();
//			result += currentID;
//			result += this.getFormat().getFieldSeparator();
//			result += this.serialiseOrderAttribute(orderCollector, currentID);
//			result += this.getFormat().getFieldEnd();
			result += this.serialiseAttributeOf(col, currentID);
		}
		result += this.getAttributeFormat().getAttributeEnd();
		return result;
	}
	
	public String serialiseAttributeOf(C col, String orderID) {
		return this.serialiseAttributeOf(col, this.idFac.createID(orderID));
	}
	
	public String serialiseAttributeOf(C col, EntityID orderID) {
		return this.getAttributeFormat().format(
				this.getAssociatedAttribute(),
				this.serialiseAttribute(col, orderID));
	}
	
	protected String serialiseAttribute(C col, EntityID orderID) {
		return this.getAttributeFormat().format(this.getAssociatedAttribute(),
				this.serialiseValue(col.getAttributeValue(this.getAssociatedAttribute(), orderID)));
	}
	
	public String serialiseAttribute(V valueObject) {
		return this.getAttributeFormat().format(this.getAssociatedAttribute(),
				this.serialiseValue(valueObject.getAttributeValue(this.getAssociatedAttribute())));
	}
	
	public String serialiseAttribute(E entity) {
		return this.getAttributeFormat().format(this.getAssociatedAttribute(),
				this.serialiseValue(entity.getAttributeValue(this.getAssociatedAttribute())));
	}
	
	protected AttributeFormat getAttributeFormat() {
		return this.attributeFormat;
	}
	
	protected AggregateFormat getAggregateFormat() {
		return this.aggregateFormat;
	}
	
	protected EntityID parseID(String id) {
		return this.idFac.createID(id);
	}
}
