package model.datamapper;

import model.entity.Entity;
import model.entity.Repository;
import model.entity.ValueObject;
import model.entity.id.EntityID;
import model.entity.id.EntityIDFactory;
import model.entity.id.MinimalIDFactory;
import model.filewriter.FileAccess;
import model.filewriter.StandardFileAccess;
import model.util.IParser;
import model.util.ISerialiser;

public abstract class AttributeDAO<A extends IAttribute, E extends Entity<A>, V extends ValueObject<A>, C extends Repository<A, E, V>> implements IParser, ISerialiser {
	private FileAccess fa;
	private AttributeFormat attributeFormat = new AttributeFormat();
	private AggregateFormat aggregateFormat = new AggregateFormat();
	private EntityIDFactory idFac = new MinimalIDFactory();
	
	protected AttributeDAO(String fileAddress, String defaultFileName) {
		this.fa = this.initFileAccess(fileAddress, defaultFileName);
	}
	
	protected FileAccess getFileAccess() {
		return this.fa;
	}
	
	protected FileAccess initFileAccess(String fileAddress, String defaultFileName) {
		return new StandardFileAccess(fileAddress, defaultFileName);
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
	
	public void setAttributesFromFile(C col, String readFile) {
		if (readFile == null) {
			return;
		}
		String body = this.getDataBody(readFile, this.getAttributeFormat().getFileStart(), this.getAttributeFormat().getFieldEnd());
		String[] entries = body.split(this.getAttributeFormat().getFieldEnd());
		String[] idNAttr;
		EntityID orderID;
		String serialisedAttribute;
		for (String e : entries) {
			if (e != null) {
				idNAttr = e.split(this.getAttributeFormat().getFieldSeparator());
//				System.out.println("Setting attribute, entry: " + e);
//				System.out.println("Setting fields: " + idNAttr[0] + ", " + idNAttr[1]);
				if (col.contains(orderID = this.parseID(idNAttr[0])) && idNAttr.length > 1) {
					serialisedAttribute = idNAttr[1];
//					System.out.println("Setting attribute, entry: " + e);
//					System.out.println("Setting fields: " + idNAttr[0] + ", " + idNAttr[1]);
					this.setAttributeAlgorithm(col, orderID, serialisedAttribute);
				}
			}
		}
	}
	
	public String serialiseAll(C col) {
		String result = "";
		result += this.getAttributeFormat().getFileStart();
		EntityID currentID;
		for (E data : col.getAllElements()) {
			currentID = data.getID();
			
//			result += this.getFormat().getFieldStart();
//			result += currentID;
//			result += this.getFormat().getFieldSeparator();
//			result += this.serialiseOrderAttribute(orderCollector, currentID);
//			result += this.getFormat().getFieldEnd();
			result += this.serialiseFor(col, currentID);
		}
		result += this.getAttributeFormat().getFileEnd();
		return result;
	}
	
	public String serialiseFor(C col, String orderID) {
		return this.serialiseFor(col, this.idFac.createID(orderID));
	}
	
	public String serialiseFor(C col, EntityID orderID) {
		String result = "";
		result += this.getAttributeFormat().getFieldStart();
		result += orderID.toString();
		result += this.getAttributeFormat().getFieldSeparator();
		result += this.serialiseAttribute(col, orderID);
		result += this.getAttributeFormat().getFieldEnd();
		return result;
	}
	
	protected String serialiseAttribute(C col, EntityID orderID) {
		return this.serialiseValue(col.getAttributeValue(this.getAssociatedAttribute(), orderID));
	}
	
	public String serialiseAttribute(V valueObject) {
		return this.serialiseValue(valueObject.getAttributeValue(this.getAssociatedAttribute()));
	}
	
	public String serialiseAttribute(E entity) {
		return this.serialiseValue(entity.getAttributeValue(this.getAssociatedAttribute()));
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
	
	public boolean writeToFileFor(C col, EntityID id) {
		return this.getFileAccess().writeToFile(this.serialiseFor(col, id));
	}
}
