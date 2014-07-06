package ch.trackdata.sbs1route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.trackdata.sbs1route.message.GeoJSONFeature;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GeoJSONProcessor implements Processor {	
	private static final Logger JSON_LOGGER = LoggerFactory.getLogger("JSON");
	private static final Logger LOGGER = LoggerFactory.getLogger(GeoJSONProcessor.class);
	
	@Override
	public void process(Exchange exchange) throws Exception {
		GeoJSONFeature geoJSONFeature = exchange.getIn().getBody(GeoJSONFeature.class);
		LOGGER.info("Convert message: {}", geoJSONFeature);
		
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(geoJSONFeature);
		LOGGER.info("Converted to json: {}", json);
		JSON_LOGGER.info("{}", json);
		exchange.getOut().setBody(json);
	}

}
