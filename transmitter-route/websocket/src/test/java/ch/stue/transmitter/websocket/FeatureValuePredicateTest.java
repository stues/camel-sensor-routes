package ch.stue.transmitter.websocket;

import java.io.IOException;

import org.apache.commons.collections4.functors.ComparatorPredicate.Criterion;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import ch.stue.domain.Feature;
import ch.stue.domain.PointGeometry;
import ch.stue.transmitter.websocket.domain.FeatureValueJSONPredicate;
import ch.stue.transmitter.websocket.domain.JSONPredicate;

/**
 * Test Feature Value Predicates
 *
 * @author stue
 *
 */
@Test
public class FeatureValuePredicateTest extends AbstractJSONPredicateTests {

	@DataProvider(name = "equalsPredicate")
	public static Object[][] equalsPredicateValues() {
		return new Object[][] { { "name", "foo", true }, { "name", "bar", false } };
	}

	@Test(dataProvider = "equalsPredicate")
	public void testEqualValuePredicatePositive(String key, Object value, boolean result) {
		Feature<PointGeometry> feature = FeatureTestHelper.createFeature(key, value);
		FeatureValueJSONPredicate featurePredicate = FeatureTestHelper.createNameFooEqualJSONPredicate();
		Assert.assertEquals(result, featurePredicate.getPredicate().evaluate(feature));
	}

	@Test(dataProvider = "equalsPredicate")
	public void testEqualValuePredicateNegative(String key, Object value, boolean result) {
		Feature<PointGeometry> feature = FeatureTestHelper.createFeature(key, value);
		FeatureValueJSONPredicate falseFeaturePredicate = FeatureTestHelper.createNameBarEqualJSONPredicate();
		Assert.assertNotEquals(result, falseFeaturePredicate.getPredicate().evaluate(feature));
	}

	@DataProvider(name = "integerPredicate")
	public static Object[][] integerPredicateValues() {
		return new Object[][] { //
			{ Criterion.EQUAL, "number", 1, false }, { Criterion.EQUAL, "number", 5, true }, { Criterion.EQUAL, "number", 10, false }, //
			{ Criterion.LESS, "number", 1, true }, { Criterion.LESS, "number", 5, false }, { Criterion.LESS, "number", 10, false }, //
			{ Criterion.LESS_OR_EQUAL, "number", 1, true }, { Criterion.LESS_OR_EQUAL, "number", 5, true }, { Criterion.LESS_OR_EQUAL, "number", 10, false }, //
			{ Criterion.GREATER, "number", 1, false }, { Criterion.GREATER, "number", 5, false }, { Criterion.GREATER, "number", 10, true }, //
			{ Criterion.GREATER_OR_EQUAL, "number", 1, false }, { Criterion.GREATER_OR_EQUAL, "number", 5, true }, { Criterion.GREATER_OR_EQUAL, "number", 10, true }, //
		};
	}

	@Test(dataProvider = "integerPredicate")
	public void testIntegerValuePredicatePositive(Criterion criteria, String propertyName, Object value, boolean result) {
		Feature<PointGeometry> feature = FeatureTestHelper.createFeature(propertyName, value);
		FeatureValueJSONPredicate featureValuePredicate = FeatureTestHelper.createFeatureValueJSONPredicate(criteria, "number", 5);
		Assert.assertEquals(result, featureValuePredicate.getPredicate().evaluate(feature));
	}

	@DataProvider(name = "doublePredicate")
	public static Object[][] doublePredicateValues() {
		return new Object[][] { //
			{ Criterion.EQUAL, "number", 1.0d, false }, { Criterion.EQUAL, "number", 5.5d, true }, { Criterion.EQUAL, "number", 10.0d, false }, //
			{ Criterion.LESS, "number", 1.0d, true }, { Criterion.LESS, "number", 5.5d, false }, { Criterion.LESS, "number", 10.0d, false }, //
			{ Criterion.LESS_OR_EQUAL, "number", 1.0d, true }, { Criterion.LESS_OR_EQUAL, "number", 5.5d, true }, { Criterion.LESS_OR_EQUAL, "number", 10.0d, false }, //
			{ Criterion.GREATER, "number", 1.0d, false }, { Criterion.GREATER, "number", 5.5d, false }, { Criterion.GREATER, "number", 10.0d, true }, //
			{ Criterion.GREATER_OR_EQUAL, "number", 1.0d, false }, { Criterion.GREATER_OR_EQUAL, "number", 5.5d, true }, { Criterion.GREATER_OR_EQUAL, "number", 10.0d, true } //
		};
	}

	@Test(dataProvider = "doublePredicate")
	public void testDoubleValuePredicatePositive(Criterion criteria, String propertyName, Object value, boolean result) {
		Feature<PointGeometry> feature = FeatureTestHelper.createFeature(propertyName, value);
		FeatureValueJSONPredicate featureValuePredicate = FeatureTestHelper.createFeatureValueJSONPredicate(criteria, "number", 5.5d);
		Assert.assertEquals(result, featureValuePredicate.getPredicate().evaluate(feature));
	}

	@DataProvider(name = "longPredicate")
	public static Object[][] longPredicateValues() {
		return new Object[][] { //
			{ Criterion.EQUAL, "number", 1l, false }, { Criterion.EQUAL, "number", 5l, true }, { Criterion.EQUAL, "number", 10l, false }, //
			{ Criterion.LESS, "number", 1l, true }, { Criterion.LESS, "number", 5l, false }, { Criterion.LESS, "number", 10l, false }, //
			{ Criterion.LESS_OR_EQUAL, "number", 1l, true }, { Criterion.LESS_OR_EQUAL, "number", 5l, true }, { Criterion.LESS_OR_EQUAL, "number", 10l, false }, //
			{ Criterion.GREATER, "number", 1l, false }, { Criterion.GREATER, "number", 5l, false }, { Criterion.GREATER, "number", 10l, true }, //
			{ Criterion.GREATER_OR_EQUAL, "number", 1l, false }, { Criterion.GREATER_OR_EQUAL, "number", 5l, true }, { Criterion.GREATER_OR_EQUAL, "number", 10l, true } //
		};
	}

	@Test(dataProvider = "longPredicate")
	public void testLongValuePredicatePositive(Criterion criteria, String propertyName, Object value, boolean result) {
		Feature<PointGeometry> feature = FeatureTestHelper.createFeature(propertyName, value);
		FeatureValueJSONPredicate featureValuePredicate = FeatureTestHelper.createFeatureValueJSONPredicate(criteria, "number", 5l);
		Assert.assertEquals(result, featureValuePredicate.getPredicate().evaluate(feature));
	}

	@DataProvider(name = "floatPredicate")
	public static Object[][] floatPredicateValues() {
		return new Object[][] { //
			{ Criterion.EQUAL, "number", 1.0f, false }, { Criterion.EQUAL, "number", 5.5f, true }, { Criterion.EQUAL, "number", 10.0f, false }, //
			{ Criterion.LESS, "number", 1.0f, true }, { Criterion.LESS, "number", 5.5f, false }, { Criterion.LESS, "number", 10.0f, false }, //
			{ Criterion.LESS_OR_EQUAL, "number", 1.0f, true }, { Criterion.LESS_OR_EQUAL, "number", 5.5f, true }, { Criterion.LESS_OR_EQUAL, "number", 10.0f, false }, //
			{ Criterion.GREATER, "number", 1.0f, false }, { Criterion.GREATER, "number", 5.5f, false }, { Criterion.GREATER, "number", 10.0f, true }, //
			{ Criterion.GREATER_OR_EQUAL, "number", 1.0f, false }, { Criterion.GREATER_OR_EQUAL, "number", 5.5f, true }, { Criterion.GREATER_OR_EQUAL, "number", 10.0f, true } //
		};
	}

	@Test(dataProvider = "floatPredicate")
	public void testFloatValuePredicatePositive(Criterion criteria, String propertyName, Object value, boolean result) {
		Feature<PointGeometry> feature = FeatureTestHelper.createFeature(propertyName, value);
		FeatureValueJSONPredicate featureValuePredicate = FeatureTestHelper.createFeatureValueJSONPredicate(criteria, "number", 5.5f);
		Assert.assertEquals(result, featureValuePredicate.getPredicate().evaluate(feature));
	}

	@DataProvider(name = "jsonDataProvider")
	public static Object[][] serializeValues() {
		return new Object[][] { //
			{ createEmptyFeatureValueJSONPredicate(), "{\"criteria\":null,\"property\":null,\"value\":null}" }, //
			{ FeatureTestHelper.createNameFooEqualJSONPredicate(), "{\"criteria\":\"EQUAL\",\"property\":\"name\",\"value\":\"foo\"}" }, //
			{ FeatureTestHelper.createFeatureValueJSONPredicate(Criterion.EQUAL, "number", 1), "{\"criteria\":\"EQUAL\",\"property\":\"number\",\"value\":1}" }, //
			{ FeatureTestHelper.createFeatureValueJSONPredicate(Criterion.GREATER, "number", 2.5d), "{\"criteria\":\"GREATER\",\"property\":\"number\",\"value\":2.5}" }, //
			{ FeatureTestHelper.createFeatureValueJSONPredicate(Criterion.LESS, "number", 3), "{\"criteria\":\"LESS\",\"property\":\"number\",\"value\":3}" }, //
			{ FeatureTestHelper.createFeatureValueJSONPredicate(Criterion.GREATER_OR_EQUAL, "number", 4.5d), "{\"criteria\":\"GREATER_OR_EQUAL\",\"property\":\"number\",\"value\":4.5}" }, //
			{ FeatureTestHelper.createFeatureValueJSONPredicate(Criterion.LESS_OR_EQUAL, "number", 5), "{\"criteria\":\"LESS_OR_EQUAL\",\"property\":\"number\",\"value\":5}" }, //
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
			{ createEmptyFeatureValueJSONPredicate(), null, null, null }, //
			{ FeatureTestHelper.createNameFooEqualJSONPredicate(), Criterion.EQUAL, "name", "foo" }, //
			{ FeatureTestHelper.createFeatureValueJSONPredicate(Criterion.EQUAL, "number", 1), Criterion.EQUAL, "number", 1 }, //
			{ FeatureTestHelper.createFeatureValueJSONPredicate(Criterion.GREATER, "number", 2.5d), Criterion.GREATER, "number", 2.5d }, //
			{ FeatureTestHelper.createFeatureValueJSONPredicate(Criterion.LESS, "number", 3), Criterion.LESS, "number", 3 }, //
			{ FeatureTestHelper.createFeatureValueJSONPredicate(Criterion.GREATER_OR_EQUAL, "number", 4.5d), Criterion.GREATER_OR_EQUAL, "number", 4.5d }, //
			{ FeatureTestHelper.createFeatureValueJSONPredicate(Criterion.LESS_OR_EQUAL, "number", 5), Criterion.LESS_OR_EQUAL, "number", 5}, //
		};
	}

	@Test(dataProvider = "serializeDeserializeDataProvider")
	private void testSerializeDeserialize(FeatureValueJSONPredicate predicate, Criterion criteria, String propertyName, Object value) throws IOException {
		String deserializedString = this.jsonPredicateMapper.writeValueAsString(predicate);
		FeatureValueJSONPredicate featureValueJSONPredicate = this.jsonPredicateMapper.readValue(deserializedString, FeatureValueJSONPredicate.class);
		Assert.assertEquals(featureValueJSONPredicate.getCriterion(), criteria);
		Assert.assertEquals(featureValueJSONPredicate.getPropertyName(), propertyName);
		Assert.assertEquals(featureValueJSONPredicate.getValue(), value);
	}

	/**
	 * Creates a new empty FeatureValueJSONPredicate
	 *
	 * @return the created FeatureValueJSONPredicate
	 */
	protected static FeatureValueJSONPredicate createEmptyFeatureValueJSONPredicate() {
		return new FeatureValueJSONPredicate();
	}
}