package model.entity;

import java.util.Map;

import model.datamapper.IAttribute;
import model.entity.id.EntityID;

public abstract class Repository<A extends IAttribute, E extends Entity<A>> {
	private Map<EntityID, E> entities;
	
	private Map<EntityID, E> getEntityMap() {
		return this.entities;
	}
	
	public Object getAttributeValue(A attribute, EntityID id) {
		E element = this.getElement(id);
		if (element != null) {
			return element.getAttributeValue(attribute);
		}
		return null;
	}
	
	public void setAttributeValue(A attribute, EntityID id, Object value) {
		E element = this.getElement(id);
		if (element != null) {
			element.setAttributeValue(attribute, value);
		}
	}
	
	public E getElement(EntityID id) {
		return this.contains(id) ? this.getEntityMap().get(id) : null;
	}
	
	public E removeElement(EntityID id) {
		return this.getEntityMap().remove(id);
	}
	
	public E removeElementIfAttributeValueEquals(A attribute, EntityID id, Object attributeValue) {
		E element = this.getElement(id);
		if (element != null && element.getAttributeValue(attribute).equals(attributeValue)) {
			return this.getEntityMap().remove(id);
		}
		return null;
	}
	public void addElement(E element) {
		this.getEntityMap().put(element.getID(), element);
	}
	public boolean contains(EntityID id) {
		return this.getEntityMap().containsKey(id);
	}
	@SuppressWarnings("unchecked")
	public E[] getAllElements() {
		return (E[]) this.getEntityMap().values().toArray(Entity[]::new);
	}
	@SuppressWarnings("unchecked")
	public E[] getAllElementsByAttributeValue(A attribute, Object attributeValue) {
		return (E[]) this.getEntityMap().values().stream()
				.filter(v -> v.getAttributeValue(attribute).equals(attributeValue))
				.toArray(Entity[]::new);
	}
	public E getElementIfAttributeValueEquals(A attribute, EntityID id, Object attributeValue) {
		E element = this.getElement(id);
		if (element == null) {
			return null;
		}
		return element.getAttributeValue(attribute).equals(attributeValue) ? element : null;
	}
	public void removeAllElementsWithAttributeValue(A attribute, Object attributeValue) {
		this.getEntityMap().values().removeIf(v -> v.getAttributeValue(attribute).equals(attributeValue));
	}
	public boolean attributeValueEquals(A attribute, EntityID id, Object attributeValue) {
		E element = this.getElement(id);
		if (element != null && element.getAttributeValue(attribute).equals(attributeValue)) {
			return true;
		}
		return false;
	}
	public void clearAllElements() {
		this.getEntityMap().clear();
	}
}
