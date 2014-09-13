package ch.trackdata.sbs1route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import ch.trackdata.sbs1route.message.GeoJSONFeature;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Converts a given {@link GeoJSONFeature} into a JSON-String
 * 
 * @author stue
 */
public class GeoJSONProcessor implements Processor {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void process(Exchange exchange) throws Exception {
		GeoJSONFeature geoJSONFeature = exchange.getIn().getBody(GeoJSONFeature.class);
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(geoJSONFeature);
		exchange.getOut().setBody(json);
	}

}
