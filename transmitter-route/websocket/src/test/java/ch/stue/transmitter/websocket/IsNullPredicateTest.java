package ch.stue.transmitter.websocket;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import ch.stue.domain.Feature;
import ch.stue.domain.PointGeometry;
import ch.stue.transmitter.websocket.domain.FeatureValueIsNullJSONPredicate;

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
		FeatureValueIsNullJSONPredicate featureValueIsNullJSONPredicate = createIsNullPredicate(predicatePropertyName);
		Assert.assertEquals(result, featureValueIsNullJSONPredicate.getPredicate().evaluate(feature));
	}

	@DataProvider(name = "jsonDataProvider")
	public static Object[][] serializeValues() {
		return new Object[][] { //
			{ createIsNullPredicate(null), "{\"isNull\":null}" }, //
			{ createIsNullPredicate("name"), "{\"isNull\":\"name\"}" } };
	}

	@Test(dataProvider = "jsonDataProvider")
	private void testSerialize(FeatureValueIsNullJSONPredicate isNull, String result) throws JsonProcessingException {
		Assert.assertEquals(this.jsonPredicateMapper.writeValueAsString(isNull), result);
	}

	@Test(dataProvider = "jsonDataProvider")
	private void testDeserialize(FeatureValueIsNullJSONPredicate isNull, String jsonString) throws IOException {
		FeatureValueIsNullJSONPredicate isNullPredicate = this.jsonPredicateMapper.readValue(jsonString, FeatureValueIsNullJSONPredicate.class);
		Assert.assertEquals(isNullPredicate, isNull);
	}

	@DataProvider(name = "serializeDeserializeDataProvider")
	public static Object[][] serializeDeserializeValues() {
		return new Object[][] { //
			{ createIsNullPredicate(null), null }, //
			{ createIsNullPredicate("name"), "name" } };
	}

	@Test(dataProvider = "serializeDeserializeDataProvider")
	private void testSerializeDeserialize(FeatureValueIsNullJSONPredicate isNull, String nullPropertyName) throws IOException {
		String deserializedString = this.jsonPredicateMapper.writeValueAsString(isNull);
		FeatureValueIsNullJSONPredicate featureValueIsNullJSONPredicate = this.jsonPredicateMapper.readValue(deserializedString, FeatureValueIsNullJSONPredicate.class);
		Assert.assertEquals(featureValueIsNullJSONPredicate.getNullPropertyName(), nullPropertyName);
	}

	/**
	 * Creates a new is null predicate
	 * @param propertyName the property to check for null
	 * @return the created feature is null predicate
	 */
	private static FeatureValueIsNullJSONPredicate createIsNullPredicate(String propertyName) {
		return new FeatureValueIsNullJSONPredicate(propertyName);
	}
}