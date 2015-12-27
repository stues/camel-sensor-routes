package ch.stue.transmitter.websocket.domain;

import java.io.IOException;
import java.util.Arrays;

import org.apache.camel.RuntimeCamelException;
import org.apache.commons.collections4.functors.ComparatorPredicate.Criterion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class JSONTest {

	public static void main(String args[]) throws IOException {
		FeatureValueJSONPredicate intJSONPred = new FeatureValueJSONPredicate();
		intJSONPred.setCriterion(Criterion.EQUAL);
		intJSONPred.setPropertyName("callSign");
		intJSONPred.setValue("SWR342");
		AndJSONPredicate multiPred = new AndJSONPredicate();
		multiPred.setAnd(Arrays.asList(intJSONPred, new NotJSONPredicate(intJSONPred)));

		ObjectMapper mapper = new ObjectMapper();
		/*
		 * Register unique field names to TestObject types
		 */
		JSONPredicateDeserializer deserializer = new JSONPredicateDeserializer();


		// Add and register the UniquePropertyPolymorphicDeserializer to the Jackson module
		SimpleModule module = new SimpleModule("JSONPredicateDeserializer");
		module.addDeserializer(JSONPredicate.class, deserializer);
		mapper.registerModule(module);

		try {
			String value = mapper.writeValueAsString(multiPred);
			System.out.println(value);
			JSONPredicate predicateDeserialized = mapper.readValue(value, JSONPredicate.class);
			System.out.println(mapper.writeValueAsString(predicateDeserialized));
			System.out.println(predicateDeserialized.getPredicate());

		} catch (JsonProcessingException e) {
			throw new RuntimeCamelException("An error occurred while writing geojsonFeature as string", e);
		}
	}
}
