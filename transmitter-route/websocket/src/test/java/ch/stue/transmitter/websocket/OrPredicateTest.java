package ch.stue.transmitter.websocket;

import java.io.IOException;
import java.util.Arrays;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

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
public class OrPredicateTest extends AbstractJSONPredicateTests {

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
		OrJSONPredicate orPredicate = new OrJSONPredicate(valuePredicate1, valuePredicate2);
		Assert.assertEquals(result, orPredicate.getPredicate().evaluate(feature));
	}

	@DataProvider(name = "jsonDataProvider")
	public static Object[][] serializeValues() {
		return new Object[][] { //
			{ createOrJSONPredicate(null, null), "{\"or\":[null,null]}" }, //
			{ createOrJSONPredicate(null, null, null), "{\"or\":[null,null,null]}" }, //
			{ createOrJSONPredicate(FeatureTestHelper.createNameBarEqualJSONPredicate(), FeatureTestHelper.createNameFooEqualJSONPredicate()), "{\"or\":[{\"criteria\":\"EQUAL\",\"property\":\"name\",\"value\":\"bar\"},{\"criteria\":\"EQUAL\",\"property\":\"name\",\"value\":\"foo\"}]}" }, //
			{ createOrJSONPredicate(FeatureTestHelper.createNameBarEqualJSONPredicate(), FeatureTestHelper.createNameFooEqualJSONPredicate(), FeatureTestHelper.createNameFooEqualJSONPredicate()),
				"{\"or\":[{\"criteria\":\"EQUAL\",\"property\":\"name\",\"value\":\"bar\"}"
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
			{ createOrJSONPredicate(null, null), new JSONPredicate[]{null, null} }, //
			{ createOrJSONPredicate(null, null, null), new JSONPredicate[]{null, null, null} }, //
			{ createOrJSONPredicate( //
					FeatureTestHelper.createNameBarEqualJSONPredicate(), FeatureTestHelper.createNameFooEqualJSONPredicate()), //
				new JSONPredicate[]{FeatureTestHelper.createNameBarEqualJSONPredicate(),FeatureTestHelper.createNameFooEqualJSONPredicate()} }, //
			{ createOrJSONPredicate( //
					FeatureTestHelper.createNameBarEqualJSONPredicate(), FeatureTestHelper.createNameFooEqualJSONPredicate(), FeatureTestHelper.createNameFooEqualJSONPredicate()), //
					new JSONPredicate[]{FeatureTestHelper.createNameBarEqualJSONPredicate(),FeatureTestHelper.createNameFooEqualJSONPredicate(), FeatureTestHelper.createNameFooEqualJSONPredicate()} }, //
		};
	}

	@Test(dataProvider = "serializeDeserializeDataProvider")
	private void testSerializeDeserialize(OrJSONPredicate predicate, JSONPredicate[] combinedPredicates) throws IOException {
		String deserializedString = this.jsonPredicateMapper.writeValueAsString(predicate);
		OrJSONPredicate orJSONPredicate = this.jsonPredicateMapper.readValue(deserializedString, OrJSONPredicate.class);
		Assert.assertEquals(orJSONPredicate.getJSONPredicates(), Arrays.asList(combinedPredicates));
	}

	/**
	 * Creates a new or predicate for the given predicates
	 *
	 * @param predicates
	 *            the predicates
	 * @return the created predicate
	 */
	private static OrJSONPredicate createOrJSONPredicate(JSONPredicate... predicates) {
		return new OrJSONPredicate(predicates);
	}
}