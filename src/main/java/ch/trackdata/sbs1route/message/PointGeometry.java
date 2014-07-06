package ch.trackdata.sbs1route.message;

import java.util.ArrayList;
import java.util.List;

/**
 * Point Geometry 
 * 
 * @author stue
 *
 */
public class PointGeometry extends AbstractGeometry<Double> {

	private static final String POINT_TYPE_STRING = "Point";

	public PointGeometry(Double longitude, Double latitude) {
		super(POINT_TYPE_STRING);
		setCoordinates(longitude, latitude);
	}

	public void setCoordinates(Double longitude, Double latitude) {
		List<Double> coordinates = getCoordinates();
		if(getCoordinates() == null){
			coordinates = new ArrayList<Double>();
		}
		else{
			coordinates.clear();
		}
		coordinates.add(longitude);
		coordinates.add(latitude);
		setCoordinates(coordinates);
	}

}