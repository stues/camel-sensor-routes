package ch.stue.transmitter.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import ch.stue.domain.Feature;
import ch.stue.domain.Geometry;
import ch.stue.domain.PointGeometry;
import ch.stue.domain.PolygonGeometry;
import ch.stue.transmitter.websocket.domain.GeometryJSONPredicate;
import ch.stue.transmitter.websocket.domain.JSONPredicate;
import ch.stue.transmitter.websocket.domain.SpatialPredicate;

@Test
public class GeometryPredicateTest extends AbstractJSONPredicateTests {

	@DataProvider(name = "spatialPredicate")
	public static Object[][] integerPredicateValues() {
		return new Object[][] { //
			{ SpatialPredicate.EQUALS, create0to10Polygon(), true }, { SpatialPredicate.EQUALS, createLonMinus10Lat5Point(), false }, { SpatialPredicate.EQUALS, createLon10Lat5Point(), false }, //
			{ SpatialPredicate.DISJOINT, createLonMinus10Lat5Point(), true }, { SpatialPredicate.DISJOINT, create0to10Polygon(), false }, { SpatialPredicate.DISJOINT, createLon10Lat5Point(), false }, //
			{ SpatialPredicate.TOUCHES, createLon10Lat10Point(), true }, { SpatialPredicate.TOUCHES, createMinus5to0Polygon(), true }, { SpatialPredicate.TOUCHES, createLon5Lat5Point(), false }, //
			{ SpatialPredicate.CONTAINS, createMinus20to20Polygon(), true }, { SpatialPredicate.CONTAINS, createLon5Lat5Point(), false }, { SpatialPredicate.CONTAINS, create2p5to7p5Polygon(), false }, //
			{ SpatialPredicate.COVERS, createMinus20to20Polygon(), true }, { SpatialPredicate.COVERS, createLon5Lat5Point(), false }, { SpatialPredicate.COVERS, create0to10Polygon(), true }, //
			{ SpatialPredicate.INTERSECTS, createMinus20to20Polygon(), true }, { SpatialPredicate.INTERSECTS, createLon5Lat5Point(), true }, { SpatialPredicate.INTERSECTS, create0to10Polygon(), true }, { SpatialPredicate.INTERSECTS, createLonMinus10Lat5Point(), false }, //
			{ SpatialPredicate.WITHIN, createMinus20to20Polygon(), false }, { SpatialPredicate.WITHIN, createLon5Lat5Point(), true }, { SpatialPredicate.WITHIN, create0to10Polygon(), true }, { SpatialPredicate.WITHIN, create2p5to7p5Polygon(), true }, //
			{ SpatialPredicate.COVEREDBY, createMinus20to20Polygon(), false }, { SpatialPredicate.COVEREDBY, createLon5Lat5Point(), true }, { SpatialPredicate.COVEREDBY, create0to10Polygon(), true }, { SpatialPredicate.COVEREDBY, create2p5to7p5Polygon(), true }, //
		};
	}

	@Test(dataProvider = "spatialPredicate")
	public void testIntegerValuePredicate(SpatialPredicate spatialPredicate, Geometry<?> featureGeometry, boolean result) {
		Feature<?> feature = FeatureTestHelper.createFeature(featureGeometry);
		GeometryJSONPredicate featureValuePredicate = new GeometryJSONPredicate(spatialPredicate, create0to10Polygon());
		Assert.assertEquals(result, featureValuePredicate.getPredicate().evaluate(feature));
	}

	@DataProvider(name = "jsonDataProvider")
	public static Object[][] serializeValues() {
		return new Object[][] { //
			{ createEmptyEqualsGeometryJSONPredicate(), "{\"equals\":null}" }, //
			{ createLon10Lat5PointGeometryJsonPredicate(SpatialPredicate.EQUALS), "{\"equals\":{\"type\":\"Point\",\"coordinates\":[10.0,5.0]}}" }, //
			{ createLon10Lat5PointGeometryJsonPredicate(SpatialPredicate.DISJOINT), "{\"disjoint\":{\"type\":\"Point\",\"coordinates\":[10.0,5.0]}}" }, //
			{ createLon10Lat5PointGeometryJsonPredicate(SpatialPredicate.TOUCHES), "{\"touches\":{\"type\":\"Point\",\"coordinates\":[10.0,5.0]}}" }, //
			{ createLon10Lat5PointGeometryJsonPredicate(SpatialPredicate.CONTAINS), "{\"contains\":{\"type\":\"Point\",\"coordinates\":[10.0,5.0]}}" }, //
			{ createLon10Lat5PointGeometryJsonPredicate(SpatialPredicate.COVERS), "{\"covers\":{\"type\":\"Point\",\"coordinates\":[10.0,5.0]}}" }, //
			{ createLon10Lat5PointGeometryJsonPredicate(SpatialPredicate.INTERSECTS), "{\"intersects\":{\"type\":\"Point\",\"coordinates\":[10.0,5.0]}}" }, //
			{ createLon10Lat5PointGeometryJsonPredicate(SpatialPredicate.WITHIN), "{\"within\":{\"type\":\"Point\",\"coordinates\":[10.0,5.0]}}" }, //
			{ createLon10Lat5PointGeometryJsonPredicate(SpatialPredicate.COVEREDBY), "{\"coveredBy\":{\"type\":\"Point\",\"coordinates\":[10.0,5.0]}}" }, //
		};
	}

	@Test(dataProvider = "jsonDataProvider")
	private void testSerialize(JSONPredicate predicate, String result) throws JsonProcessingException {
		Assert.assertEquals(this.jsonPredicateMapper.writeValueAsString(predicate), result);
	}

	@Test(dataProvider = "jsonDataProvider")
	private void testDeserialize(JSONPredicate predicate, String jsonString) throws IOException {
		JSONPredicate deserializedPredicate = this.jsonPredicateMapper.readValue(jsonString, JSONPredicate.class);
		Assert.assertEquals(deserializedPredicate, predicate);
	}

	@DataProvider(name = "serializeDeserializeDataProvider")
	public static Object[][] serializeDeserializeValues() {
		return new Object[][] { //
			{ createEmptyEqualsGeometryJSONPredicate(), SpatialPredicate.EQUALS, null }, //
			{ createLon10Lat5PointGeometryJsonPredicate(SpatialPredicate.EQUALS), SpatialPredicate.EQUALS, createLon10Lat5Point() }, //
			{ createLon10Lat5PointGeometryJsonPredicate(SpatialPredicate.DISJOINT), SpatialPredicate.DISJOINT, createLon10Lat5Point() }, //
			{ createLon10Lat5PointGeometryJsonPredicate(SpatialPredicate.TOUCHES), SpatialPredicate.TOUCHES, createLon10Lat5Point() }, //
			{ createLon10Lat5PointGeometryJsonPredicate(SpatialPredicate.CONTAINS), SpatialPredicate.CONTAINS, createLon10Lat5Point() }, //
			{ createLon10Lat5PointGeometryJsonPredicate(SpatialPredicate.COVERS), SpatialPredicate.COVERS, createLon10Lat5Point() }, //
			{ createLon10Lat5PointGeometryJsonPredicate(SpatialPredicate.INTERSECTS), SpatialPredicate.INTERSECTS, createLon10Lat5Point() }, //
			{ createLon10Lat5PointGeometryJsonPredicate(SpatialPredicate.WITHIN), SpatialPredicate.WITHIN, createLon10Lat5Point() }, //
			{ createLon10Lat5PointGeometryJsonPredicate(SpatialPredicate.COVEREDBY), SpatialPredicate.COVEREDBY, createLon10Lat5Point() }, //
		};
	}

	@Test(dataProvider = "serializeDeserializeDataProvider")
	private void testSerializeDeserialize(GeometryJSONPredicate predicate, SpatialPredicate spatialPredicate, Geometry<?> geometry) throws IOException {
		String deserializedString = this.jsonPredicateMapper.writeValueAsString(predicate);
		GeometryJSONPredicate geometryJSONPredicate = this.jsonPredicateMapper.readValue(deserializedString, GeometryJSONPredicate.class);
		Assert.assertEquals(geometryJSONPredicate.getKey(), spatialPredicate);
		Assert.assertEquals(geometryJSONPredicate.getValue(), geometry);
	}

	/**
	 * Creates a new empty FeatureValueJSONPredicate
	 *
	 * @return the created FeatureValueJSONPredicate
	 */
	protected static GeometryJSONPredicate createEmptyEqualsGeometryJSONPredicate() {
		return new GeometryJSONPredicate();
	}

	protected static GeometryJSONPredicate createLon10Lat5WithinPointGeometryJsonPredicate() {
		return createLon10Lat5PointGeometryJsonPredicate(SpatialPredicate.WITHIN);
	}

	protected static GeometryJSONPredicate createLon10Lat5PointGeometryJsonPredicate(SpatialPredicate spatialPredicate) {
		return new GeometryJSONPredicate(spatialPredicate, createLon10Lat5Point());
	}

	protected static PointGeometry createLon10Lat5Point() {
		return new PointGeometry(10.0, 5.0);
	}

	protected static PointGeometry createLon5Lat5Point() {
		return new PointGeometry(5.0, 5.0);
	}

	protected static PointGeometry createLon10Lat10Point() {
		return new PointGeometry(10.0, 10.0);
	}

	protected static PointGeometry createLonMinus10Lat5Point() {
		return new PointGeometry(-10.0, 5.0);
	}

	protected static GeometryJSONPredicate createLon5Lat5PointGeometryJsonPredicate(SpatialPredicate spatialPredicate) {
		return new GeometryJSONPredicate(spatialPredicate, new PointGeometry(5.0, 5.0));
	}

	protected static PolygonGeometry createMinus20to20Polygon() {
		List<Double[]> points = new ArrayList<Double[]>();
		points.add(new Double[] { -20.0, -20.0 });
		points.add(new Double[] { 20.0, -20.0 });
		points.add(new Double[] { 20.0, 20.0 });
		points.add(new Double[] { -20.0, 20.0 });
		points.add(new Double[] { -20.0, -20.0 });
		return new PolygonGeometry(Collections.singletonList(points));
	}

	protected static PolygonGeometry create2p5to7p5Polygon() {
		List<Double[]> points = new ArrayList<Double[]>();
		points.add(new Double[] { 2.5, 2.5 });
		points.add(new Double[] { 7.5, 2.5 });
		points.add(new Double[] { 7.5, 7.5 });
		points.add(new Double[] { 2.5, 7.5 });
		points.add(new Double[] { 2.5, 2.5 });
		return new PolygonGeometry(Collections.singletonList(points));
	}

	protected static PolygonGeometry createMinus5to0Polygon() {
		List<Double[]> points = new ArrayList<Double[]>();
		points.add(new Double[] { 0.0, 0.0 });
		points.add(new Double[] { -5.0, 0.0 });
		points.add(new Double[] { -5.0, -5.0 });
		points.add(new Double[] { 0.0, -5.0 });
		points.add(new Double[] { 0.0, 0.0 });
		return new PolygonGeometry(Collections.singletonList(points));
	}

	protected static PolygonGeometry create0to10Polygon() {
		List<Double[]> points = new ArrayList<Double[]>();
		points.add(new Double[] { 0.0, 0.0 });
		points.add(new Double[] { 0.0, 10.0 });
		points.add(new Double[] { 10.0, 10.0 });
		points.add(new Double[] { 10.0, 0.0 });
		points.add(new Double[] { 0.0, 0.0 });
		return new PolygonGeometry(Collections.singletonList(points));
	}
}