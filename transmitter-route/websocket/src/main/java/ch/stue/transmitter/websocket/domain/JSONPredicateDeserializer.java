package ch.stue.transmitter.websocket.domain;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Special Deserializer to be able to deserialize a given JSONPredicate
 * dependent on the unique properties of a JSONPredicate a different class will
 * be used to deserialize
 */
public class JSONPredicateDeserializer extends StdDeserializer<JSONPredicate> {

	private static final long serialVersionUID = 1L;

	private final Map<String, Class<? extends JSONPredicate>> propertyMap;

	/**
	 * Default Constructor
	 */
	public JSONPredicateDeserializer() {
		super(JSONPredicate.class);
		this.propertyMap = new HashMap<String, Class<? extends JSONPredicate>>();
		this.propertyMap.put(FeatureValueJSONPredicate.PROPERTY, FeatureValueJSONPredicate.class);
		this.propertyMap.put(FeatureValueJSONPredicate.CRITERIA, FeatureValueJSONPredicate.class);
		this.propertyMap.put(FeatureValueJSONPredicate.VALUE, FeatureValueJSONPredicate.class);

		this.propertyMap.put(WithinGeometryJSONPredicate.WITHIN, GeometryJSONPredicate.class);

		this.propertyMap.put(AndJSONPredicate.AND, AndJSONPredicate.class);
		this.propertyMap.put(OrJSONPredicate.OR, OrJSONPredicate.class);
		this.propertyMap.put(NotJSONPredicate.NOT, NotJSONPredicate.class);
		this.propertyMap.put(FeatureValueIsNullJSONPredicate.IS_NULL, FeatureValueIsNullJSONPredicate.class);

		this.propertyMap.putAll(GeometryJSONPredicate.getJSONUniqueIdentifiers());
	}

	@Override
	public JSONPredicate deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {

		Class<? extends JSONPredicate> clazz = null;

		ObjectMapper mapper = (ObjectMapper) parser.getCodec();
		ObjectNode obj = (ObjectNode) mapper.readTree(parser);
		Iterator<Entry<String, JsonNode>> elementsIterator = obj.fields();

		while (elementsIterator.hasNext()) {
			Entry<String, JsonNode> element = elementsIterator.next();
			String name = element.getKey();
			if (this.propertyMap.containsKey(name)) {
				clazz = this.propertyMap.get(name);
				break;
			}
		}

		if (clazz == null) {
			throw context.mappingException("No property found which can be used to unique identify a JSONPredicate subclass");
		}

		return mapper.treeToValue(obj, clazz);
	}

}