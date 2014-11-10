package ch.trackdata.sbs1route.websocket;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.websocket.DefaultWebsocket;
import org.apache.camel.component.websocket.WebsocketConstants;
import org.apache.camel.component.websocket.WebsocketStore;
import org.apache.commons.collections4.functors.FalsePredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.trackdata.sbs1route.message.SBS1Message;

/**
 * Extracts the {@link SBS1Message} from the exchanged List
 */
public class WebsocketProcessor implements Processor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketProcessor.class);

	private WebsocketStore websocketStore;
		

	/**
	 * @return the websocketStore
	 */
	public WebsocketStore getWebsocketStore() {
		return websocketStore;
	}

	/**
	 * @param websocketStore the websocketStore to set
	 */
	public void setWebsocketStore(WebsocketStore websocketStore) {
		this.websocketStore = websocketStore;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void process(Exchange exchange) throws Exception {
		
		Message in = exchange.getIn();
		String message = in.getMandatoryBody(String.class);
		
		String connectionKey = (String)in.getHeader(WebsocketConstants.CONNECTION_KEY);
		DefaultWebsocket websocket = websocketStore.get(connectionKey);
		if(websocket instanceof FilterWebsocket){
			((FilterWebsocket) websocket).setFilter(FalsePredicate.INSTANCE);
		}
		else{
			LOGGER.trace("websocket NOT in store");
		}
		
		exchange.getOut().setHeader(WebsocketConstants.CONNECTION_KEY, in.getHeader(WebsocketConstants.CONNECTION_KEY));
		exchange.getOut().setBody("OK");
	}

}
