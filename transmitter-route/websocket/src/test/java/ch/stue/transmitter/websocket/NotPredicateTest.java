package ch.stue.transmitter.websocket;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import ch.stue.domain.Feature;
import ch.stue.domain.PointGeometry;
import ch.stue.transmitter.websocket.domain.FeatureValueJSONPredicate;
import ch.stue.transmitter.websocket.domain.JSONPredicate;
import ch.stue.transmitter.websocket.domain.NotJSONPredicate;

/**
 * Test NOT Predicate
 *
 * @author stue
 *
 */
@Test
public class NotPredicateTest extends AbstractJSONPredicateTests {

	@DataProvider(name = "notPredicateDataProvider")
	public static Object[][] notPredicateValues() {
		return new Object[][] { { "name", "foo", false }, { "name", "bar", true } };
	}

	@Test(dataProvider = "notPredicateDataProvider")
	public void testNotPredicate(String key, Object value, boolean result) {
		Feature<PointGeometry> feature = FeatureTestHelper.createFeature(key, value);

		NotJSONPredicate not = createNotNameFooEqualJSONPredicate();
		Assert.assertEquals(not.getPredicate().evaluate(feature), result);
	}

	@DataProvider(name = "jsonDataProvider")
	public static Object[][] serializeValues() {
		return new Object[][] { //
			{ createEmptyNotJSONPredicate(), "{\"not\":null}" }, //
			{ createNotNameFooEqualJSONPredicate(), "{\"not\":{\"criteria\":\"EQUAL\",\"property\":\"name\",\"value\":\"foo\"}}" } };
	}

	@Test(dataProvider = "jsonDataProvider")
	private void testSerialize(NotJSONPredicate not, String result) throws JsonProcessingException {
		Assert.assertEquals(this.jsonPredicateMapper.writeValueAsString(not), result);
	}

	@Test(dataProvider = "jsonDataProvider")
	private void testDeserialize(NotJSONPredicate not, String jsonString) throws IOException {
		NotJSONPredicate notPredicate = this.jsonPredicateMapper.readValue(jsonString, NotJSONPredicate.class);
		Assert.assertEquals(notPredicate, not);
	}

	@DataProvider(name = "serializeDeserializeDataProvider")
	public static Object[][] serializeDeserializeValues() {
		return new Object[][] { //
			{ createEmptyNotJSONPredicate(), null }, //
			{ createNotNameFooEqualJSONPredicate(), FeatureTestHelper.createNameFooEqualJSONPredicate() } };
	}

	@Test(dataProvider = "serializeDeserializeDataProvider")
	private void testSerializeDeserialize(NotJSONPredicate not, JSONPredicate jsonPredicate) throws IOException {
		String deserializedString = this.jsonPredicateMapper.writeValueAsString(not);
		NotJSONPredicate notPredicate = this.jsonPredicateMapper.readValue(deserializedString, NotJSONPredicate.class);
		Assert.assertEquals(notPredicate.getJSONPredicate(), jsonPredicate);
	}

	/**
	 * Creates a new NotJsonPredicate
	 *
	 * @return the created NotJSONPredicate
	 */
	protected static NotJSONPredicate createEmptyNotJSONPredicate() {
		return new NotJSONPredicate();
	}

	/**
	 * Creates a new NotJsonPredicate with a inner value predicate:
	 * - property "name"
	 * - value "foo"
	 * - criteria "EQUALS"
	 *
	 * @return the created NotJSONPredicate
	 */
	protected static NotJSONPredicate createNotNameFooEqualJSONPredicate() {
		FeatureValueJSONPredicate featurePredicate = FeatureTestHelper.createNameFooEqualJSONPredicate();
		return new NotJSONPredicate(featurePredicate);
	}
}