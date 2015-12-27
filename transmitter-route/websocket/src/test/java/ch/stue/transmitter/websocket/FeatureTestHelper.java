package ch.stue.transmitter.websocket;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.collections4.functors.ComparatorPredicate.Criterion;

import ch.stue.domain.Feature;
import ch.stue.domain.GeoJSONFeature;
import ch.stue.domain.PointGeometry;
import ch.stue.transmitter.websocket.domain.FeatureValueJSONPredicate;

/**
 * Helper class for tests
 *
 * @author stue
 */
public class FeatureTestHelper {

	/**
	 * @return a predicate with criterion equals, property name and value bar
	 */
	protected static FeatureValueJSONPredicate createNameBarEqualJSONPredicate() {
		return createFeatureValueJSONPredicate(Criterion.EQUAL, "name", "bar");
	}

	/**
	 * @return a predicate with criterion equals, property name and value foo
	 */
	protected static FeatureValueJSONPredicate createNameFooEqualJSONPredicate() {
		return createFeatureValueJSONPredicate(Criterion.EQUAL, "name", "foo");
	}

	/**
	 * Returns a feature value predicate for the given values
	 *
	 * @param criteria
	 *            the criteria
	 * @param propertyName
	 *            the property
	 * @param value
	 *            the value
	 * @return the created JSONPredicate
	 */
	public static FeatureValueJSONPredicate createFeatureValueJSONPredicate(Criterion criteria, String propertyName, Object value) {
		return new FeatureValueJSONPredicate(criteria, propertyName, value);
	}

	/**
	 * Returns a new feature for the given properties
	 *
	 * @param longitude
	 *            the longitude
	 * @param latitude
	 *            the latitude
	 * @return the created feature
	 */
	public static Feature<PointGeometry> createFeature(String key, Object value) {
		return createFeature(null, null, Collections.<String, Object> singletonMap(key, value));
	}

	/**
	 * Returns a new feature for the given properties
	 *
	 * @param longitude
	 *            the longitude
	 * @param latitude
	 *            the latitude
	 * @return the created feature
	 */
	public static Feature<PointGeometry> createFeature(Double longitude, Double latitude, String key, Object value) {
		return createFeature(longitude, latitude, Collections.<String, Object> singletonMap(key, value));
	}

	/**
	 * Returns a new feature for the given properties
	 *
	 * @param longitude
	 *            the longitude
	 * @param latitude
	 *            the latitude
	 * @return the created feature
	 */
	public static Feature<PointGeometry> createFeature(Double longitude, Double latitude) {
		return createFeature(longitude, latitude, Collections.<String, Object> emptyMap());
	}

	/**
	 * Returns a new feature for the given properties
	 *
	 * @param longitude
	 *            the longitude
	 * @param latitude
	 *            the latitude
	 * @param properties
	 *            the properties to add
	 * @return the created feature
	 */
	public static Feature<PointGeometry> createFeature(Double longitude, Double latitude, Map<String, Object> properties) {
		PointGeometry pointGeometry = new PointGeometry(longitude, latitude);
		GeoJSONFeature<PointGeometry> feature = new GeoJSONFeature<PointGeometry>(pointGeometry);
		feature.setProperties(properties);
		return feature;
	}

	public FeatureTestHelper() {
		super();
	}

}