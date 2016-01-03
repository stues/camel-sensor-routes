package ch.stue.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * The Geometry Interface
 *
 * @author stue
 *
 * @param <C> the stored values of the Geometry
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
public interface Geometry<C> extends Serializable {

	/**
	 * @return the coordinates
	 */
	public abstract C getCoordinates();

	/**
	 * @param coordinates
	 *            the coordinates to set
	 */
	public abstract void setCoordinates(C coordinates);

	/**
	 * @return the type
	 */
	public abstract String getType();

}