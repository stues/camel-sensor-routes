package ch.stue.transmitter.websocket;

import java.io.IOException;
import java.util.Arrays;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import ch.stue.domain.Feature;
import ch.stue.domain.PointGeometry;
import ch.stue.transmitter.websocket.domain.AndJSONPredicate;
import ch.stue.transmitter.websocket.domain.JSONPredicate;

@Test
public class AndPredicateTest extends AbstractJSONPredicateTests {

	@DataProvider(name = "andPredicate")
	public static Object[][] andPredicateValues() {
		return new Object[][] { //
			{ "name", "foo", FeatureTestHelper.createNameFooEqualJSONPredicate(), FeatureTestHelper.createNameFooEqualJSONPredicate(), true }, // foo,
			// foo
			{ "name", "foo", FeatureTestHelper.createNameBarEqualJSONPredicate(), FeatureTestHelper.createNameFooEqualJSONPredicate(), false }, // foo,
			// bar
			{ "name", "foo", FeatureTestHelper.createNameFooEqualJSONPredicate(), FeatureTestHelper.createNameBarEqualJSONPredicate(), false }, // bar,
			// foo
			{ "name", "foo", FeatureTestHelper.createNameBarEqualJSONPredicate(), FeatureTestHelper.createNameBarEqualJSONPredicate(), false } // bar,
			// bar
		};
	}

	@Test(dataProvider = "andPredicate")
	public void testAndPredicate(String propertyName, Object value, JSONPredicate valuePredicate1, JSONPredicate valuePredicate2, boolean result) {
		Feature<PointGeometry> feature = FeatureTestHelper.createFeature(propertyName, value);
		AndJSONPredicate andPredicate = createAndJSONPredicate(valuePredicate1, valuePredicate2);
		Assert.assertEquals(result, andPredicate.getPredicate().evaluate(feature));
	}

	@DataProvider(name = "jsonDataProvider")
	public static Object[][] serializeValues() {
		return new Object[][] { //
			{ createAndJSONPredicate(null, null), "{\"and\":[null,null]}" }, //
			{ createAndJSONPredicate(null, null, null), "{\"and\":[null,null,null]}" }, //
			{ createAndJSONPredicate(FeatureTestHelper.createNameBarEqualJSONPredicate(), FeatureTestHelper.createNameFooEqualJSONPredicate()), "{\"and\":[{\"criteria\":\"EQUAL\",\"property\":\"name\",\"value\":\"bar\"},{\"criteria\":\"EQUAL\",\"property\":\"name\",\"value\":\"foo\"}]}" }, //
			{ createAndJSONPredicate(FeatureTestHelper.createNameBarEqualJSONPredicate(), FeatureTestHelper.createNameFooEqualJSONPredicate(), FeatureTestHelper.createNameFooEqualJSONPredicate()),
				"{\"and\":[{\"criteria\":\"EQUAL\",\"property\":\"name\",\"value\":\"bar\"}"
						+ ",{\"criteria\":\"EQUAL\",\"property\":\"name\",\"value\":\"foo\"}"
						+ ",{\"criteria\":\"EQUAL\",\"property\":\"name\",\"value\":\"foo\"}]}" }, //
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
			{ createAndJSONPredicate(null, null), new JSONPredicate[]{null, null} }, //
			{ createAndJSONPredicate(null, null, null), new JSONPredicate[]{null, null, null} }, //
			{ createAndJSONPredicate( //
					FeatureTestHelper.createNameBarEqualJSONPredicate(), FeatureTestHelper.createNameFooEqualJSONPredicate()), //
				new JSONPredicate[]{FeatureTestHelper.createNameBarEqualJSONPredicate(),FeatureTestHelper.createNameFooEqualJSONPredicate()} }, //
			{ createAndJSONPredicate( //
					FeatureTestHelper.createNameBarEqualJSONPredicate(), FeatureTestHelper.createNameFooEqualJSONPredicate(), FeatureTestHelper.createNameFooEqualJSONPredicate()), //
					new JSONPredicate[]{FeatureTestHelper.createNameBarEqualJSONPredicate(),FeatureTestHelper.createNameFooEqualJSONPredicate(), FeatureTestHelper.createNameFooEqualJSONPredicate()} }, //
		};
	}

	@Test(dataProvider = "serializeDeserializeDataProvider")
	private void testSerializeDeserialize(AndJSONPredicate predicate, JSONPredicate[] combinedPredicates) throws IOException {
		String deserializedString = this.jsonPredicateMapper.writeValueAsString(predicate);
		AndJSONPredicate andJSONPredicate = this.jsonPredicateMapper.readValue(deserializedString, AndJSONPredicate.class);
		Assert.assertEquals(andJSONPredicate.getJSONPredicates(), Arrays.asList(combinedPredicates));
	}

	/**
	 * Creates a new and predicate for the given predicates
	 *
	 * @param predicates
	 *            the predicates
	 * @return the created predicate
	 */
	private static AndJSONPredicate createAndJSONPredicate(JSONPredicate... predicates) {
		return new AndJSONPredicate(predicates);
	}
}