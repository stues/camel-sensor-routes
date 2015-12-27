package ch.stue.transmitter.websocket;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ch.stue.domain.Feature;
import ch.stue.domain.PointGeometry;
import ch.stue.transmitter.websocket.domain.AndJSONPredicate;
import ch.stue.transmitter.websocket.domain.JSONPredicate;

@Test
public class AndPredicateTest  {

	@DataProvider(name = "andPredicate")
	public static Object[][] andPredicateValues() {
		return new Object[][] { //
			{ "name", "foo", FeatureTestHelper.createNameFooEqualJSONPredicate(), FeatureTestHelper.createNameFooEqualJSONPredicate(), true }, // foo, foo
			{ "name", "foo", FeatureTestHelper.createNameBarEqualJSONPredicate(), FeatureTestHelper.createNameFooEqualJSONPredicate(), false }, // foo, bar
			{ "name", "foo", FeatureTestHelper.createNameFooEqualJSONPredicate(), FeatureTestHelper.createNameBarEqualJSONPredicate(), false }, // bar, foo
			{ "name", "foo", FeatureTestHelper.createNameBarEqualJSONPredicate(), FeatureTestHelper.createNameBarEqualJSONPredicate(), false } // bar, bar
		};
	}

	@Test(dataProvider = "andPredicate")
	public void testAndPredicate(String propertyName, Object value, JSONPredicate valuePredicate1, JSONPredicate valuePredicate2, boolean result) {
		Feature<PointGeometry> feature = FeatureTestHelper.createFeature(propertyName, value);
		AndJSONPredicate andPredicate = new AndJSONPredicate(valuePredicate1, valuePredicate2);
		Assert.assertEquals(result, andPredicate.getPredicate().evaluate(feature));
	}
}