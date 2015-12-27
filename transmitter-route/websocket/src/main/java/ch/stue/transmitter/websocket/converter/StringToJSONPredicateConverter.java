package ch.stue.transmitter.websocket.converter;

import org.apache.camel.BeanInject;
import org.apache.camel.Converter;
import org.apache.camel.RuntimeCamelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.stue.domain.PolygonGeometry;
import ch.stue.transmitter.websocket.FilterWebsocketProcessor;
import ch.stue.transmitter.websocket.domain.JSONPredicate;

/**
 * Converts a {@link String} which contains a JSON Object into a {@link PolygonGeometry}
 *
 * @author stue
 */

@Converter
public class StringToJSONPredicateConverter {

	private static final Logger LOGGER = LoggerFactory.getLogger(FilterWebsocketProcessor.class);

	@BeanInject("jsonPredicateMapper")
	private ObjectMapper mapper;

	/**
	 * converts the {@link String} containing a JSON Object into a {@link JSONPredicate}
	 *
	 * @param message the String to convert
	 * @return the {@link JSONPredicate} object
	 */
	@Converter
	public JSONPredicate convertTo(String message) {
		try {
			JSONPredicate geoJSONFeature = this.mapper.readValue(message, JSONPredicate.class);
			return geoJSONFeature;
		} catch(Exception e) {
			String warnString = String.format("An error occurred while mapping the given string: %s", message);
			LOGGER.warn(warnString, e);
			throw new RuntimeCamelException(warnString, e);
		}
	}
}
