package ch.stue.domain;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This Class Provides members to describe a GeoJSON-Feature the geometry type
 * must be of the Type {@link AbstractGeometry} the properties Map may contain
 * different types of Values (String, Integer, Double, ...)
 *
 * @author stue
 *
 */
public class GeoJSONFeature<T extends Geometry<?>> implements Feature<T>, Serializable {

	private static final long serialVersionUID = 1L;

	private static final String GEOMETRY_PROPERTY_NAME = "geometry";
	private static final String PROPERTIES_PROPERTY_NAME = "properties";

	private static final String FEATURE_TYPE_STRING = "Feature";

	private String type;

	private Geometry<?> geometry;

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
	@JsonCreator
	public GeoJSONFeature(
			@JsonProperty(GEOMETRY_PROPERTY_NAME) Geometry<?> geometry,
			@JsonProperty(PROPERTIES_PROPERTY_NAME) Map<String, Object> properties) {
		this.geometry = geometry;
		this.properties = properties;
		this.type = FEATURE_TYPE_STRING;
	}

	/**
	 * @return the Geometry
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T getGeometry() {
		return (T) this.geometry;
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
	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public <O> O getProperty(String propertyName, Class<O> clazz) {
		if (MapUtils.isNotEmpty(this.properties) && this.properties.containsKey(propertyName)) {
			return clazz.cast(this.properties.get(propertyName));
		}
		return null;
	}

	/**
	 * @return the map which contains all the key value pairs
	 */
	@Override
	public Map<String, Object> getProperties() {
		return this.properties;
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
