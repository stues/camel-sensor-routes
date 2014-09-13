package ch.trackdata.sbs1route.message;

import java.util.ArrayList;
import java.util.List;

/**
 * A Geometry which represents a Point Geometry
 * 
 * @author stue
 * 
 */
public class PointGeometry extends AbstractGeometry<Double> {

	private static final String POINT_TYPE_STRING = "Point";

	/**
	 * Constructor with longitude and latitude
	 * 
	 * @param longitude
	 *            the longitude
	 * @param latitude
	 *            the latitude
	 */
	public PointGeometry(Double longitude, Double latitude) {
		super(POINT_TYPE_STRING);
		setCoordinates(longitude, latitude);
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
		List<Double> coordinates = getCoordinates();
		if (getCoordinates() == null) {
			coordinates = new ArrayList<Double>();
		} else {
			coordinates.clear();
		}
		coordinates.add(longitude);
		coordinates.add(latitude);
		setCoordinates(coordinates);
	}

}