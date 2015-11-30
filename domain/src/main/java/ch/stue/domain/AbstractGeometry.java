package ch.stue.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * {@link AbstractGeometry} JSON Class provides coordinates and a type member
 * 
 * @author stue
 * 
 */
@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = AbstractGeometry.TYPE_PROPERTY_NAME)
@JsonSubTypes({
    @JsonSubTypes.Type(
    	value = PointGeometry.class,
        name = PointGeometry.POINT_TYPE_STRING),
    @JsonSubTypes.Type(
    	value = PolygonGeometry.class,
    	name = PolygonGeometry.POLYGON_TYPE_STRING)})
public abstract class AbstractGeometry<C> implements Geometry<C> {

	private static final long serialVersionUID = 1L;
	
	public static final String TYPE_PROPERTY_NAME = "type";
	public static final String COORDINATES_PROPERTY_NAME = "coordinates";

	@JsonProperty(COORDINATES_PROPERTY_NAME)
	private C coordinates;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public C getCoordinates() {
		return coordinates;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCoordinates(C coordinates) {
		this.coordinates = coordinates;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract String getType();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coordinates == null) ? 0 : coordinates.hashCode());
		result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AbstractGeometry<?> other = (AbstractGeometry<?>) obj;
		if (coordinates == null) {
			if (other.coordinates != null) {
				return false;
			}
		} else if (!coordinates.equals(other.coordinates)) {
			return false;
		}
		if (getType() == null) {
			if (other.getType() != null) {
				return false;
			}
		} else if (!getType().equals(other.getType())) {
			return false;
		}
		return true;
	}
}
