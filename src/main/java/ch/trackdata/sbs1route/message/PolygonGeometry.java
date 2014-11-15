package ch.trackdata.sbs1route.message;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A Geometry which represents a Polygon Geometry
 * 
 * @author stue
 * 
 */
public class PolygonGeometry extends AbstractGeometry<List<List<Double[]>>> {

	public static final String POLYGON_TYPE_STRING = "Polygon";

	/**
	 * Constructor with coordinates
	 * 
	 * @param coordinates
	 *            the coordinates
	 */
	@JsonCreator
	public PolygonGeometry(@JsonProperty(COORDINATES_PROPERTY_NAME) List<List<Double[]>> coordinates) {
		setCoordinates(coordinates);
	}

	/**
	 * @param geometry
	 *            instantiates this geometry with the given geometry
	 */
	public PolygonGeometry(AbstractGeometry<List<List<Double[]>>> geometry) {
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
	public void setCoordinates(List<List<Double[]>> newCoordinates) {
		List<List<Double[]>> coordinates = getCoordinates();
		if (getCoordinates() == null) {
			coordinates = new ArrayList<List<Double[]>>();
		} else {
			coordinates.clear();
		}
		coordinates.addAll(newCoordinates);
		super.setCoordinates(coordinates);
	}

	/**
	 * @return the shell (first element of the coordinates list) or null if
	 *         no coordinates set
	 */
	public List<Double[]> getShell() {
		List<List<Double[]>> coordinates = getCoordinates();
		if (coordinates != null && coordinates.size() >= 1) {
			return coordinates.get(0);
		}
		return null;
	}

	/**
	 * @return the holes or null if
	 *         no coordinates set or if no holes set
	 */
	public List<List<Double[]>> getHoles() {
		List<List<Double[]>> coordinates = getCoordinates();
		if (coordinates != null && coordinates.size() > 1) {
			return coordinates.subList(1, coordinates.size());
		}
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getType() {
		return POLYGON_TYPE_STRING;
	}

}