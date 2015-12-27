package ch.stue.transmitter.websocket;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ch.stue.domain.Feature;
import ch.stue.domain.PointGeometry;
import ch.stue.transmitter.websocket.domain.JSONPredicate;
import ch.stue.transmitter.websocket.domain.OrJSONPredicate;

/**
 * Test OR Predicate
 *
 * @author stue
 *
 */
@Test
public class OrPredicateTest  {

	@DataProvider(name = "orPredicate")
	public static Object[][] orPredicateValues() {
		return new Object[][] { //
			{ "name", "foo", FeatureTestHelper.createNameFooEqualJSONPredicate(), FeatureTestHelper.createNameFooEqualJSONPredicate(), true }, // foo, foo
			{ "name", "foo", FeatureTestHelper.createNameBarEqualJSONPredicate(), FeatureTestHelper.createNameFooEqualJSONPredicate(), true }, // foo, bar
			{ "name", "foo", FeatureTestHelper.createNameFooEqualJSONPredicate(), FeatureTestHelper.createNameBarEqualJSONPredicate(), true }, // bar, foo
			{ "name", "foo", FeatureTestHelper.createNameBarEqualJSONPredicate(), FeatureTestHelper.createNameBarEqualJSONPredicate(), false } // bar, bar
		};
	}

	@Test(dataProvider = "orPredicate")
	public void testOrPredicate(String propertyName, Object value, JSONPredicate valuePredicate1, JSONPredicate valuePredicate2, boolean result) {
		Feature<PointGeometry> feature = FeatureTestHelper.createFeature(propertyName, value);
		OrJSONPredicate andPredicate = new OrJSONPredicate(valuePredicate1, valuePredicate2);
		Assert.assertEquals(result, andPredicate.getPredicate().evaluate(feature));
	}
}