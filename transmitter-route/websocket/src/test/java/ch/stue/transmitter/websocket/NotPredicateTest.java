package ch.stue.transmitter.websocket;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ch.stue.domain.Feature;
import ch.stue.domain.PointGeometry;
import ch.stue.transmitter.websocket.domain.FeatureValueJSONPredicate;
import ch.stue.transmitter.websocket.domain.NotJSONPredicate;

/**
 * Test NOT Predicate
 *
 * @author stue
 *
 */
@Test
public class NotPredicateTest {

	@DataProvider(name = "notPredicate")
	public static Object[][] notPredicateValues() {
		return new Object[][] { { "name", "foo", false }, { "name", "bar", true } };
	}

	@Test(dataProvider = "notPredicate")
	public void testNotPredicate(String key, Object value, boolean result) {
		Feature<PointGeometry> feature = FeatureTestHelper.createFeature(key, value);
		FeatureValueJSONPredicate featurePredicate = FeatureTestHelper.createNameFooEqualJSONPredicate();
		NotJSONPredicate not = extracted(featurePredicate);
		Assert.assertEquals(result, not.getPredicate().evaluate(feature));
	}

	/**
	 * Creates a new NotJsonPredicate
	 * @param featurePredicate the predicate to invert
	 * @return the created NotJSONPredicate
	 */
	private NotJSONPredicate extracted(FeatureValueJSONPredicate featurePredicate) {
		return new NotJSONPredicate(featurePredicate);
	}
}