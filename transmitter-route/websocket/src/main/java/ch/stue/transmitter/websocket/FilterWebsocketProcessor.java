package ch.stue.transmitter.websocket;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.websocket.DefaultWebsocket;
import org.apache.camel.component.websocket.WebsocketConstants;
import org.apache.camel.component.websocket.WebsocketStore;
import org.apache.commons.collections4.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.stue.domain.Feature;
import ch.stue.transmitter.websocket.domain.JSONPredicate;

/**
 * Extracts the {@link Feature} from the exchanged List
 */
public class FilterWebsocketProcessor implements Processor {

	private static final Logger LOGGER = LoggerFactory.getLogger(FilterWebsocketProcessor.class);

	private WebsocketStore websocketStore;

	/**
	 * @return the websocketStore
	 */
	public WebsocketStore getWebsocketStore() {
		return this.websocketStore;
	}

	/**
	 * @param websocketStore
	 *            the websocketStore to set
	 */
	public void setWebsocketStore(WebsocketStore websocketStore) {
		this.websocketStore = websocketStore;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void process(Exchange exchange) throws Exception {
		Message in = exchange.getIn();
		JSONPredicate jsonPredicate = in.getMandatoryBody(JSONPredicate.class);

		if (jsonPredicate != null) {
			String connectionKey = (String) in.getHeader(WebsocketConstants.CONNECTION_KEY);
			DefaultWebsocket websocket = this.websocketStore.get(connectionKey);

			if (websocket instanceof FilterWebsocket) {
				FilterWebsocket filterWebsocket = (FilterWebsocket) websocket;

				FeatureMessageFilter messageFilter;
				if (filterWebsocket.getFilter() instanceof FeatureMessageFilter) {
					messageFilter = (FeatureMessageFilter) filterWebsocket.getFilter();
				} else {
					messageFilter = new FeatureMessageFilter();
					filterWebsocket.setFilter(messageFilter);
				}

				Predicate<Object> predicate = jsonPredicate.getPredicate();
				messageFilter.setPredicate(predicate);
			}
		}
		else{
			LOGGER.info("Empty predicate passed");
		}

	}

}
