package model.datamapper;

import model.entity.Entity;
import model.entity.Repository;
import model.entity.ValueObject;
import model.entity.id.EntityID;
import model.entity.id.MinimalID;

public abstract class AttributeDAO<A extends IAttribute, D extends Entity<A>, C extends Repository<A, D>> {
	private AttributeFormat format = new AttributeFormat();
	
	public void setAttributeAlgorithm(C col, EntityID attributeOwnerID, String serialisedValue) {
		col.setAttributeValue(this.getAssociatedAttribute(), attributeOwnerID, serialisedValue);
	}
	
	protected abstract String serialiseValue(Object attributeValue);
	protected abstract Object parseSerialisedValue(String serialisedValue);
	protected abstract A getAssociatedAttribute();
	
	protected String getDataBody(String data) {
		int begin = 0;
		int end = 0;
		
		if (data.startsWith(format.getFileStart())) {
			begin = format.getFileStart().length();
		}
		
		if (data.endsWith(format.getFileEnd())) {
			end = data.length() - format.getFileEnd().length();
		}
		
		return data.substring(begin, end);
	}
	
	public void setAttributesFromFile(C col, String readFile) {
		if (readFile == null) {
			return;
		}
		String body = this.getDataBody(readFile);
		String[] entries = body.split(format.getFieldEnd());
		String[] idNAttr;
		EntityID orderID;
		String serialisedAttribute;
		for (String e : entries) {
			if (e != null) {
				idNAttr = e.split(format.getFieldSeparator());
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
		result += this.getFormat().getFileStart();
		EntityID currentID;
		for (D data : col.getAllElements()) {
			currentID = data.getID();
			
//			result += this.getFormat().getFieldStart();
//			result += currentID;
//			result += this.getFormat().getFieldSeparator();
//			result += this.serialiseOrderAttribute(orderCollector, currentID);
//			result += this.getFormat().getFieldEnd();
			result += this.serialiseFor(col, currentID);
		}
		result += this.getFormat().getFileEnd();
		return result;
	}
	
	public String serialiseFor(C col, EntityID orderID) {
		String result = "";
		result += this.getFormat().getFieldStart();
		result += orderID;
		result += this.getFormat().getFieldSeparator();
		result += this.serialiseOrderAttribute(col, orderID);
		result += this.getFormat().getFieldEnd();
		return result;
	}
	
	protected String serialiseOrderAttribute(C col, EntityID orderID) {
		return this.serialiseValue(col.getAttributeValue(this.getAssociatedAttribute(), orderID));
	}
	
	protected AttributeFormat getFormat() {
		return this.format;
	}
	
	protected EntityID parseID(String id) {
		return new MinimalID(id);
	}
}
