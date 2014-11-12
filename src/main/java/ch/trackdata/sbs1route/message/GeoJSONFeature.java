package ch.trackdata.sbs1route.message;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * This Class Provides members to describe a GeoJSON-Feature the geometry type
 * must be of the Type {@link AbstractGeometry} the properties Map may contain
 * different types of Values (String, Integer, Double, ...)
 * 
 * @author stue
 * 
 */
public class GeoJSONFeature<T extends AbstractGeometry<?>> {

	private static final String FEATURE_TYPE_STRING = "Feature";

	private String type;

	private T geometry;

	private Map<String, Object> properties;

	/**
	 * Constructor with a given geometry
	 * 
	 * @param geometry
	 *            the geometry
	 */
	public GeoJSONFeature(T geometry) {
		this(geometry, null);
	}

	/**
	 * Constructor with a geometry and properties
	 * 
	 * @param geometry
	 *            the geometry
	 * @param properties
	 *            the properties
	 */
	public GeoJSONFeature(T geometry, Map<String, Object> properties) {
		this.geometry = geometry;
		this.properties = properties;
		this.type = FEATURE_TYPE_STRING;
	}

	/**
	 * @return the Geometry
	 */
	public T getGeometry() {
		return geometry;
	}

	/**
	 * @param geometry
	 *            the geometry to set
	 */
	public void setGeometry(T geometry) {
		this.geometry = geometry;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Returns the Property Casted as type of the given clazz
	 * 
	 * @param propertyName
	 *            the property to return
	 * @param clazz
	 *            the clazz
	 * @return the property casted as clazz
	 */
	public <O> O getProperty(String propertyName, Class<O> clazz) {
		if (MapUtils.isNotEmpty(properties) && properties.containsKey(propertyName)) {
			return clazz.cast(properties.get(propertyName));
		}
		return null;
	}

	/**
	 * @return the map which contains all the key value pairs
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
