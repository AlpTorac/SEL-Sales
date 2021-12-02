package model.entity.id;

public abstract class EntityID implements Comparable<EntityID> {
	private final String id;
	
	protected EntityID(String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return this.id;
	}
	
	public boolean serialisedIDequals(String id) {
		return this.toString().equals(id);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof EntityID)) {
			return false;
		}
		return this.compareTo((EntityID) o) == 0 ? true : false;
//		String thisIDContent = this.toString();
//		String otherIDContent = otherID.toString();
//		boolean result = thisIDContent.equals(otherIDContent);
//		return result;
	}
	
	@Override
	public int compareTo(EntityID otherID) {
		return this.toString().compareTo(otherID.toString());
	}
}
