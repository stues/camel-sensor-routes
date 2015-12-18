package ch.stue.domain.transmitter.websocket;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import ch.stue.domain.PointGeometry;
import ch.stue.domain.PolygonGeometry;

/**
 * Helper Class for transforming GeoJSON Data to JTS Geometries
 *
 * @author stue
 */
public final class GeometryConverterHelper {

	private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

	/**
	 * Private Constructor so class can not be instantiated
	 */
	private GeometryConverterHelper() {
		// Private constructor
	}

	/**
	 * Returns a JTS {@link Geometry} for the given {@link ch.stue.domain.Geometry}
	 *
	 * @param polygon
	 *            the {@link PolygonGeometry}
	 * @return if given {@link PolygonGeometry} contains polygon coordinates returns a
	 *         {@link Polygon} with these coordinates as polygon otherwise null
	 */
	public static Geometry getGeometry(ch.stue.domain.Geometry<?> geometry){
		if(geometry instanceof PointGeometry){
			return getPointGeometry((PointGeometry) geometry);
		}
		else if(geometry instanceof PolygonGeometry){
			return getPolygonGeometry((PolygonGeometry) geometry);
		}
		throw new IllegalArgumentException(String.format("The given '%s' geometry type is not known", geometry));
	}

	/**
	 * Returns a JTS {@link Point} for the given {@link PointGeometry}
	 *
	 * @param point
	 *            the {@link PointGeometry}
	 * @return if given {@link PointGemetry} contains two coordinates, a
	 *         {@link Point} with these two points will be returned otherwise
	 *         null
	 */
	public static Point getPointGeometry(PointGeometry point) {
		if(point != null){
			Double[] coordinates = point.getCoordinates();
			return GEOMETRY_FACTORY.createPoint(getCoordinate(coordinates));
		}
		else{
			return null;
		}
	}

	/**
	 * Returns a JTS {@link Geometry} for the given {@link PolygonGeometry}
	 *
	 * @param polygon
	 *            the {@link PolygonGeometry}
	 * @return if given {@link PolygonGeometry} contains polygon coordinates returns a
	 *         {@link Polygon} with these coordinates as polygon otherwise null
	 */
	public static Polygon getPolygonGeometry(PolygonGeometry polygon) {
		List<Double[]> shellDoubleValues = polygon.getShell();
		LinearRing shell = getLinearRing(shellDoubleValues);

		List<List<Double[]>> holesDoubleValues = polygon.getHoles();
		if (CollectionUtils.isNotEmpty(holesDoubleValues)) {
			int holesSize = holesDoubleValues.size();
			LinearRing[] holes = new LinearRing[holesSize];
			for(int i = 0; i< holesSize; i++){
				holes[i] = getLinearRing(holesDoubleValues.get(i));
			}
			return GEOMETRY_FACTORY.createPolygon(shell, holes);
		} else {
			return GEOMETRY_FACTORY.createPolygon(shell, null);
		}
	}

	/**
	 * Returns the coordinate for the given Double array
	 *
	 * @param coordinates
	 *            the double array which should have two values
	 * @return a {@link Coordinate} instance
	 */
	private static Coordinate getCoordinate(Double[] coordinates) {
		if (CollectionUtils.size(coordinates) == 2) {
			return new Coordinate(coordinates[0], coordinates[1]);
		}
		return null;
	}

	/**
	 * Returns a Linear Ring for the given List of Double array
	 * @param polygonDoubleValues the List of Double array
	 * @return a {@link LinearRing} instance
	 */
	private static LinearRing getLinearRing(List<Double[]> polygonDoubleValues) {
		if (CollectionUtils.isNotEmpty(polygonDoubleValues)) {
			int polygonSize = polygonDoubleValues.size();
			Coordinate[] coordinates = new Coordinate[polygonSize];
			for (int i = 0; i < polygonSize; i++) {
				coordinates[i] = getCoordinate(polygonDoubleValues.get(i));
			}
			return GEOMETRY_FACTORY.createLinearRing(coordinates);
		}
		return null;
	}
}
