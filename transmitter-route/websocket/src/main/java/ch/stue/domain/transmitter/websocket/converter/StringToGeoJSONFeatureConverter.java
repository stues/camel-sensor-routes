package ch.stue.domain.transmitter.websocket.converter;

import org.apache.camel.Converter;
import org.apache.camel.RuntimeCamelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.stue.domain.GeoJSONFeature;
import ch.stue.domain.PolygonGeometry;
import ch.stue.domain.transmitter.websocket.FilterWebsocketProcessor;

/**
 * Converts a {@link String} which contains a JSON Object into a {@link PolygonGeometry}
 *
 * @author stue
 */

@Converter
public class StringToGeoJSONFeatureConverter {

	private static final Logger LOGGER = LoggerFactory.getLogger(FilterWebsocketProcessor.class);


	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * converts the {@link String} containing a JSON Object into a {@link PolygonGeometry}
	 *
	 * @param message the String to convert
	 * @return the {@link PolygonGeometry} object
	 */
	@Converter
	public static GeoJSONFeature<?> convertTo(String message) {
		try {
			GeoJSONFeature<?> geoJSONFeature = mapper.readValue(message, GeoJSONFeature.class);
			return geoJSONFeature;
		} catch(Exception e) {
			String warnString = String.format("An error occurred while mapping the given string: %s", message);
			LOGGER.warn(warnString, e);
			throw new RuntimeCamelException(warnString, e);
		}
	}
}
