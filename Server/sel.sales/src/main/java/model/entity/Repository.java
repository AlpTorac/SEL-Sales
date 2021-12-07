package model.entity;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import model.datamapper.IAttribute;
import model.entity.id.EntityID;
import model.entity.id.EntityIDFactory;
import model.entity.id.MinimalIDFactory;

public abstract class Repository<A extends IAttribute, E extends Entity<A>, V extends ValueObject<A>> {
	private EntityIDFactory idFac;
	private Map<EntityID, E> entities;
	private IFactory<A, E, V> fac;
	
	protected Repository() {
		this.entities = this.initEntityMap();
		this.fac = this.getDefaultFactory();
		this.idFac = this.initIDFactory();
	}
	
	public void setFactory(IFactory<A,E,V> fac) {
		this.fac = fac;
	}
	
	protected Map<EntityID, E> getEntityMap() {
		return this.entities;
	}
	
	protected Map<EntityID, E> initEntityMap() {
		return new ConcurrentHashMap<EntityID, E>();
	}
	
	protected EntityIDFactory initIDFactory() {
		return new MinimalIDFactory();
	}
	
	protected IFactory<A, E, V> getFactory() {
		return this.fac;
	}
	
	protected abstract IFactory<A, E, V> getDefaultFactory();
	
	public V getValueObjectFor(E entity) {
		return this.getFactory().entityToValue(entity);
	}
	
	public V toValueObject(E entity) {
		if (entity != null) {
			return this.getFactory().entityToValue(entity);
		}
		return null;
	}
	
	public Collection<V> toValueObjectArray(Collection<E> entities) {
		Collection<V> result = new CopyOnWriteArrayList<V>();
		entities.forEach(e -> result.add(this.toValueObject(e)));
		return result;
	}
	
	public V getElementAsValueObject(EntityID id) {
		E element = this.getElement(id);
		if (element == null) {
			return null;
		}
		return this.getValueObjectFor(element);
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
		Optional<Entry<EntityID, E>> o = this.getEntityMap().entrySet().stream()
				.filter(e -> e.getKey().equals(id))
				.findFirst();
		
		return o.isPresent() ? o.get().getValue() : null;
	}
	
	public E removeElement(String id) {
		Optional<Entry<EntityID, E>> o = this.getEntityMap().entrySet().stream()
				.filter(e -> e.getKey().equals(this.idFac.createID(id)))
				.findFirst();
		
		return o.isPresent() ? this.getEntityMap().remove(o.get().getKey()) : null;
	}
	
	public E removeElement(EntityID id) {
		Optional<Entry<EntityID, E>> o = this.getEntityMap().entrySet().stream()
				.filter(e -> e.getKey().equals(id))
				.findFirst();
		
		return o.isPresent() ? this.getEntityMap().remove(o.get().getKey()) : null;
	}
	
	public E removeElementIfAttributeValueEquals(A attribute, EntityID id, Object attributeValue) {
		E element = this.getElement(id);
		if (element != null && element.getAttributeValue(attribute).equals(attributeValue)) {
			return this.getEntityMap().remove(id);
		}
		return null;
	}
	public void addElement(V valueObjectOfElement) {
		this.addElement(this.getFactory().valueToEntity(valueObjectOfElement));
	}
	public void addElement(E element) {
		if (!this.contains(element.getID())) {
			this.getEntityMap().put(element.getID(), element);
		}
	}
	public boolean contains(EntityID id) {
		return this.getEntityMap().keySet().stream().anyMatch(k -> k.equals(id));
	}
	public Collection<E> getAllElements() {
		return this.getEntityMap().values();
	}
	public Collection<V> getAllElementsAsValueObjects() {
		Collection<V> result = new CopyOnWriteArrayList<V>();
		this.getEntityMap().values().stream().map(e -> this.toValueObject(e))
		.forEach(v -> result.add(v));
		return result;
	}
	public Collection<E> getAllElementsByAttributeValue(A attribute, Object attributeValue) {
		Collection<E> result = new CopyOnWriteArrayList<E>();
		this.getEntityMap().values().stream()
		.filter(e -> e.getAttributeValue(attribute).equals(attributeValue))
		.forEach(e -> result.add(e));
		return result;
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
	public EntityID getMinimalID(String id) {
		return this.idFac.createID(id);
	}
}
