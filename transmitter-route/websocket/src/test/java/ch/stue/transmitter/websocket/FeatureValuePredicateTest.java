package ch.stue.transmitter.websocket;

import org.apache.commons.collections4.functors.ComparatorPredicate.Criterion;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ch.stue.domain.Feature;
import ch.stue.domain.PointGeometry;
import ch.stue.transmitter.websocket.domain.FeatureValueJSONPredicate;

/**
 * Test Feature Value Predicates
 *
 * @author stue
 *
 */
@Test
public class FeatureValuePredicateTest {

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
}