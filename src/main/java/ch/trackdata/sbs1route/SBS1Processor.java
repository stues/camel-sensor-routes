package ch.trackdata.sbs1route;

import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.trackdata.sbs1route.message.SBS1Message;

/**
 * Extracts the {@link SBS1Message} from the exchanged List
 */
public class SBS1Processor implements Processor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SBS1Processor.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void process(Exchange exchange) throws Exception {
		List<Map<String, Object>> messages = exchange.getIn().getBody(List.class);
		Map<String, Object> msgMap = (Map<String, Object>) messages.get(0);
		SBS1Message sbs1Message = (SBS1Message) msgMap.values().iterator().next();
		LOGGER.info("Convert message: {}", sbs1Message);
		exchange.getOut().setBody(sbs1Message);
	}

}
