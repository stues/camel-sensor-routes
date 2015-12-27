package ch.stue.transmitter.websocket;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

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
public class IsNullPredicateTest extends AbstractJSONPredicateTests {

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

	@DataProvider(name = "jsonDataProvider")
	public static Object[][] serializeValues() {
		return new Object[][] { //
			{ createIsNullPredicate(null), "{\"isNull\":null}" }, //
			{ createIsNullPredicate("name"), "{\"isNull\":\"name\"}" } };
	}

	@Test(dataProvider = "jsonDataProvider")
	private void testSerialize(FeatureIsNullJSONPredicate isNull, String result) throws JsonProcessingException {
		Assert.assertEquals(this.jsonPredicateMapper.writeValueAsString(isNull), result);
	}

	@Test(dataProvider = "jsonDataProvider")
	private void testDeserialize(FeatureIsNullJSONPredicate isNull, String jsonString) throws IOException {
		FeatureIsNullJSONPredicate isNullPredicate = this.jsonPredicateMapper.readValue(jsonString, FeatureIsNullJSONPredicate.class);
		Assert.assertEquals(isNullPredicate, isNull);
	}

	@DataProvider(name = "serializeDeserializeDataProvider")
	public static Object[][] serializeDeserializeValues() {
		return new Object[][] { //
			{ createIsNullPredicate(null), null }, //
			{ createIsNullPredicate("name"), "name" } };
	}

	@Test(dataProvider = "serializeDeserializeDataProvider")
	private void testSerializeDeserialize(FeatureIsNullJSONPredicate isNull, String nullPropertyName) throws IOException {
		String deserializedString = this.jsonPredicateMapper.writeValueAsString(isNull);
		FeatureIsNullJSONPredicate featureIsNullJSONPredicate = this.jsonPredicateMapper.readValue(deserializedString, FeatureIsNullJSONPredicate.class);
		Assert.assertEquals(featureIsNullJSONPredicate.getNullPropertyName(), nullPropertyName);
	}

	/**
	 * Creates a new is null predicate
	 * @param propertyName the property to check for null
	 * @return the created feature is null predicate
	 */
	private static FeatureIsNullJSONPredicate createIsNullPredicate(String propertyName) {
		return new FeatureIsNullJSONPredicate(propertyName);
	}
}