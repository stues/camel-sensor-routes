package ch.stue.transmitter.websocket;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ch.stue.domain.Feature;
import ch.stue.domain.PointGeometry;
import ch.stue.transmitter.websocket.domain.FeatureIsNullJSONPredicate;

/**
 * Test Is Null Predicate
 *
 * @author stue
 *
 */
@Test
public class IsNullPredicateTest {

	@DataProvider(name = "notPredicate")
	public static Object[][] notPredicateValues() {
		return new Object[][] { { "name", "foo", "firstName", true }, { "name", "foo", "name", false } };
	}

	@Test(dataProvider = "notPredicate")
	public void testNotPredicate(String propertyName, Object value, String predicatePropertyName, boolean result) {
		Feature<PointGeometry> feature = FeatureTestHelper.createFeature(propertyName, value);
		FeatureIsNullJSONPredicate featureIsNullJSONPredicate = createIsNullPredicate(predicatePropertyName);
		Assert.assertEquals(result, featureIsNullJSONPredicate.getPredicate().evaluate(feature));
	}

	/**
	 * Creates a new is null predicate
	 * @param propertyName the property to check for null
	 * @return the created feature is null predicate
	 */
	private FeatureIsNullJSONPredicate createIsNullPredicate(String propertyName) {
		return new FeatureIsNullJSONPredicate(propertyName);
	}
}