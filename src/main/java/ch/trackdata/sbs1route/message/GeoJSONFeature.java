package ch.trackdata.sbs1route.message;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GeoJSONFeature {

	private static final String FEATURE_TYPE_STRING = "Feature";

	@JsonProperty("type")
	private String type;
	
	@JsonProperty("geometry")
	private AbstractGeometry<?> geometry;
	
	@JsonProperty("properties")
	private Map<String, Object> properties;
	
	public GeoJSONFeature(AbstractGeometry<?> geometry){
		this(geometry, null);
	}

	public GeoJSONFeature(AbstractGeometry<?> geometry, Map<String, Object> properties){
		this.geometry = geometry;
		this.properties = properties;
		this.type = FEATURE_TYPE_STRING;
	}
	
	public AbstractGeometry<?> getGeometry() {
		return geometry;
	}

	public void setGeometry(AbstractGeometry<?> geometry) {
		this.geometry = geometry;
	}
	
	public String getType() {
		return type;
	}
	
	public Map<String, Object> getProperties() {
		return properties;
	}

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
