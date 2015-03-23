package ch.trackdata.sbs1route.message;

import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A Geometry which represents a Point Geometry
 * 
 * @author stue
 * 
 */
public class PointGeometry extends AbstractGeometry<Double[]> {

	private static final long serialVersionUID = 1L;
	
	public static final String POINT_TYPE_STRING = "Point";

	/**
	 * Constructor with longitude and latitude
	 * 
	 * @param longitude
	 *            the longitude
	 * @param latitude
	 *            the latitude
	 */
	public PointGeometry(Double longitude, Double latitude) {
		setCoordinates(new Double[]{longitude, latitude});
	}
	
	/**
	 * Constructor to instantiate from JSON
	 * @param coordinates the coordinates as Double Array
	 */
	@JsonCreator
	public PointGeometry(@JsonProperty(COORDINATES_PROPERTY_NAME) Double[] coordinates){
		setCoordinates(coordinates);
	}
	
	/**
	 * @param geometry instantiates this geometry with the given geometry
	 */
	public PointGeometry(AbstractGeometry<Double[]> geometry){
		setCoordinates(geometry.getCoordinates());
	}
	
	/**
	 * Update the Point with the given coordinates
	 * 
	 * @param longitude
	 *            the longitude
	 * @param latitude
	 *            the latitude
	 */
	public void setCoordinates(Double longitude, Double latitude) {
		Double[] coordinates = getCoordinates();
		if (coordinates == null) {
			coordinates = new Double[2];
		}
		coordinates[0] = longitude;
		coordinates[1] = latitude;
		setCoordinates(coordinates);
	}

	/**
	 * Sets the given coordinates as point coordinates
	 * @param coordinates the coordinates needs to be a double array of length 2
	 */
	public void setCoordinates(Double[] coordinates){
		Assert.isTrue(coordinates != null && coordinates.length == 2);
		super.setCoordinates(coordinates);
	}
	
	/**
	 * @return the longitude if set otherwise null
	 */
	@JsonIgnore
	public Double getLongitude(){
		Double[] pointCoordinates = getCoordinates();
		if(pointCoordinates != null){
			return pointCoordinates[0];
		}
		return null;
	}
	
	/**
	 * @return the latitude if set otherwise null
	 */
	@JsonIgnore
	public Double getLatitude(){
		Double[] pointCoordinates = getCoordinates();
		if(pointCoordinates != null){
			return pointCoordinates[1];
		}
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@JsonIgnore
	public String getType() {
		return POINT_TYPE_STRING;
	}
}