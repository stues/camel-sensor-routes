package ch.trackdata.sbs1route.converter;

import org.apache.camel.Converter;
import org.apache.camel.RuntimeCamelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.trackdata.sbs1route.message.GeoJSONFeature;
import ch.trackdata.sbs1route.message.PolygonGeometry;
import ch.trackdata.sbs1route.websocket.WebsocketProcessor;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Converts a {@link String} which contains a JSON Object into a {@link PolygonGeometry}
 * 
 * @author stue
 */

@Converter
public class StringToGeoJSONConverter {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketProcessor.class);

	
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
			message = message.replace("\\", "");
			GeoJSONFeature<?> geoJSONFeature = mapper.readValue(message, GeoJSONFeature.class);
			return geoJSONFeature;
		} catch(Exception e) {
			LOGGER.warn("An error occurred while mapping the given string: " + message, e);
			throw new RuntimeCamelException("An error occurred while mapping the given string: " + message, e);
		}
	}
}
