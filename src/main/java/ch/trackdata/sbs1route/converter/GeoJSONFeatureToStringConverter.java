package ch.trackdata.sbs1route.converter;

import org.apache.camel.Converter;
import org.apache.camel.RuntimeCamelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.trackdata.sbs1route.message.GeoJSONFeature;
import ch.trackdata.sbs1route.websocket.FilterWebsocketProcessor;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Converts a given {@link GeoJSONFeature} into a JSON-String
 * 
 * @author stue
 */
@Converter
public class GeoJSONFeatureToStringConverter {

	private static final Logger LOGGER = LoggerFactory.getLogger(FilterWebsocketProcessor.class);
	
	ObjectMapper mapper;
	
	public GeoJSONFeatureToStringConverter(){
		mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
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
			return mapper.writeValueAsString(geoJSONFeature);
		} catch (JsonProcessingException e) {
			LOGGER.warn("An error occurred while writing geojsonFeature as string", e);
			throw new RuntimeCamelException("An error occurred while writing geojsonFeature as string", e);
		}
	}

}
