package model.datamapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import model.entity.Entity;
import model.entity.IFactory;
import model.entity.Repository;
import model.entity.ValueObject;
import model.entity.id.EntityID;
import model.entity.id.EntityIDFactory;
import model.entity.id.MinimalIDFactory;
import model.util.IParser;
import model.util.ISerialiser;

public abstract class EntityDAO<A extends IAttribute, E extends Entity<A>, V extends ValueObject<A>, C extends Repository<A, E, V>> implements IParser, ISerialiser {
	private IFactory<A,E,V> fac;
	
	private EntityFormat entityFormat = new EntityFormat();
	private AttributeFormat attributeFormat = new AttributeFormat();
	private EntityIDFactory idFac = new MinimalIDFactory();
	
	private String address;
	private Map<A, AttributeDAO<A,E,V,C>> attributeToDAO;

	public EntityDAO(String address) {
		this.address = address;
		this.attributeToDAO = this.initDAOMap();
		this.fillDAOList();
		this.fac = this.initFactory();
	}

	protected abstract IFactory<A,E,V> initFactory();
	
	public AttributeDAO<A,E,V,C> getDAO(IAttribute oa) {
		return this.getDAOMap().get(oa);
	}
	
	public abstract AttributeDAO<A,E,V,C> getDAO(String serialisedDesc);
	
	protected Map<A, AttributeDAO<A,E,V,C>> getDAOMap() {
		return this.attributeToDAO;
	}

	/**
	 * Make sure that the map sorts the elements, so that the order of the maps stays the same.
	 * This way, the format of the serialised orders stays the same
	 */
	protected abstract Map<A, AttributeDAO<A,E,V,C>> initDAOMap();

	protected String getAddress() {
		return this.address;
	}

	protected abstract void fillDAOList();

	protected EntityID createMinimalID(String id) {
		return this.idFac.createID(id);
	}
	
	public EntityFormat getEntityFormat() {
		return this.entityFormat;
	}

	public AttributeFormat getAttributeFormat() {
		return this.attributeFormat;
	}
	
	@SuppressWarnings("unchecked")
	public String serialiseValueObjects(V... data) {
		String result = "";
		for (V d : data) {
			String serialisedAttributes = "";
			for (AttributeDAO<A,E,V,C> dao : this.getDAOMap().values()) {
				serialisedAttributes += dao.serialiseAttribute(d);
			}
			result += this.getEntityFormat().format(d.getID(), serialisedAttributes);
		}
		return result;
	}
	
	public Collection<V> parseValueObjects(String serialisedOrder) {
		if (serialisedOrder != null) {
			Collection<V> col = new ArrayList<V>();
			String[] serialisedOrders = this.getEntityFormat().getMatches(serialisedOrder).toArray(String[]::new);
			for (String so : serialisedOrders) {
				System.out.println("serialisedEntity: " + so);
				col.add(this.parseValueObject(so));
			}
			return col;
		}
		return null;
	}
	
	public V parseValueObject(String serialisedOrder) {
		if (serialisedOrder != null) {
			String[] fields = this.getEntityFormat().getFields(serialisedOrder);
			if (fields.length > 1) {
				String id = fields[0];
				String[] serialisedAttributes = this.getAttributeFormat().getMatches(fields[1]).toArray(String[]::new);
				V data = this.fac.constructMinimalValueObject(this.createMinimalID(id));
				for (String attr : serialisedAttributes) {
					if (attr != null) {
						String[] attributeFields = this.getAttributeFormat().getFields(attr);
						if (attributeFields.length > 1) {
							String attrDesc = attributeFields[0];
							String attrValue = attributeFields[1];
							System.out.println("desc: " + attrDesc);
							System.out.println("val: " + attrValue);
							this.getDAO(attrDesc).setAttributeAlgorithm(data, attrValue);
						}
					}
				}
				return data;
			}
		}
		return null;
	}

}