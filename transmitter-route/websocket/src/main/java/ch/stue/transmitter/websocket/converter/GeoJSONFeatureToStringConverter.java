package ch.stue.transmitter.websocket.converter;

import org.apache.camel.Converter;
import org.apache.camel.RuntimeCamelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.stue.domain.GeoJSONFeature;
import ch.stue.transmitter.websocket.FilterWebsocketProcessor;

/**
 * Converts a given {@link GeoJSONFeature} into a JSON-String
 *
 * @author stue
 */
@Converter
public class GeoJSONFeatureToStringConverter {

	private static final Logger LOGGER = LoggerFactory.getLogger(FilterWebsocketProcessor.class);

	ObjectMapper mapper;

	/**
	 * Default Constructor
	 */
	public GeoJSONFeatureToStringConverter(){
		this.mapper = new ObjectMapper();
		this.mapper.setSerializationInclusion(Include.NON_NULL);
	}

	/**
	 * converts the {@link GeoJSONFeature} into a JSON-{@link String}
	 *
	 * @param geoJSONFeature the geoJSONFeature
	 * @return the JSON-{@link String}
	 */
	@Converter
	public String convert(GeoJSONFeature<?> geoJSONFeature) {
		try {
			return this.mapper.writeValueAsString(geoJSONFeature);
		} catch (JsonProcessingException e) {
			LOGGER.warn("An error occurred while writing geojsonFeature as string", e);
			throw new RuntimeCamelException("An error occurred while writing geojsonFeature as string", e);
		}
	}

}
