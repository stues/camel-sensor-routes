package ch.trackdata.demo;

import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.trackdata.demo.message.Sbs1Message;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonProcessor implements Processor {	
	private static final Logger JSON = LoggerFactory.getLogger("JSON");
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonProcessor.class);
	
	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		List messages = exchange.getIn().getBody(List.class);
		Map msgMap = (Map) messages.get(0);
		Sbs1Message sbs1Message = (Sbs1Message) msgMap.values().iterator().next(); 
		LOGGER.info("Convert message: {}", sbs1Message);
		
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(sbs1Message);
		LOGGER.info("Converted to json: {}", json);
		JSON.info("{}", json);
		exchange.getOut().setBody(json);
	}

}
