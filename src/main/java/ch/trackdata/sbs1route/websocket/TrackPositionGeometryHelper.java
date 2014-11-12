package ch.trackdata.sbs1route.websocket;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import ch.trackdata.sbs1route.message.AbstractGeometry;
import ch.trackdata.sbs1route.message.PointGeometry;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * Helper Class for transforming GeoJSON Data to JTS Geometries
 * 
 * @author stue
 */
public final class TrackPositionGeometryHelper {
	private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

	/**
	 * Private Constructor so class can not be instantiated
	 */
	private TrackPositionGeometryHelper() {
		// Private constructor
	}

	/**
	 * Returns a JTS {@link Point} for the given {@link PointGeometry}
	 * 
	 * @param point
	 *            the {@link PointGeometry}
	 * @return if given {@link PointGemetry} contains two coordinates, a
	 *         {@link Point} with these two points will be returned otherwise null
	 */
	public static Point getGeometry(AbstractGeometry<Double> point) {
		List<Double> coordinates = point.getCoordinates();
		if (CollectionUtils.size(coordinates) == 2) {
			return GEOMETRY_FACTORY.createPoint(new Coordinate(coordinates.get(0), coordinates.get(1)));
		}
		return null;
	}
}
