package ch.trackdata.sbs1route.message;

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
}
