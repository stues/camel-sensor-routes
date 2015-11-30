package ch.stue.domain.transmitter.websocket;

import java.io.IOException;
import java.util.Collection;

import org.apache.camel.CamelExchangeException;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.component.websocket.DefaultWebsocket;
import org.apache.camel.component.websocket.WebsocketConstants;
import org.apache.camel.component.websocket.WebsocketEndpoint;
import org.apache.camel.component.websocket.WebsocketProducer;
import org.apache.camel.component.websocket.WebsocketStore;
import org.apache.commons.collections4.CollectionUtils;

/**
 * Extends the {@link WebsocketProducer} with the ability to filter messages
 * 
 * @author stue
 * 
 */
public class FilterWebsocketProducer extends WebsocketProducer {

	private final WebsocketStore store;
	private final Boolean sendToAll;
	private final FilterWebsocketEndpoint endpoint;

	public FilterWebsocketProducer(FilterWebsocketEndpoint endpoint, WebsocketStore store) {
		super(endpoint, store);
		this.store = store;
		this.sendToAll = endpoint.getSendToAll();
		this.endpoint = endpoint;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void process(Exchange exchange) throws Exception {
		Message in = exchange.getIn();

		if (sendToAllSet(in)) {
			doSendToAll(store, in, exchange);
		} else {
			// look for connection key and get Websocket
			String connectionKey = in.getHeader(WebsocketConstants.CONNECTION_KEY, String.class);
			if (connectionKey != null) {
				DefaultWebsocket websocket = store.get(connectionKey);
				log.debug("Sending to connection key {} -> {}", connectionKey, in);
				String message = in.getMandatoryBody(String.class);
				sendMessage(message, websocket);
			} else {
				throw new IllegalArgumentException("Failed to send message to single connection; connetion key not set.");
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WebsocketEndpoint getEndpoint() {
		return endpoint;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start() throws Exception {
		super.start();
		endpoint.connect(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stop() throws Exception {
		endpoint.disconnect(this);
		super.stop();
	}

	/**
	 * returns whether the service shall send the given message to all
	 * 
	 * @param in
	 *            the message
	 * @return send to all
	 */
	protected boolean sendToAllSet(Message in) {
		// header may be null; have to be careful here (and fallback to use
		// sendToAll option configured from endpoint)
		Boolean value = in.getHeader(WebsocketConstants.SEND_TO_ALL, sendToAll, Boolean.class);
		return value == null ? false : value;
	}

	/**
	 * Sends the message to all Websocket
	 * 
	 * @param store
	 *            the websocket store
	 * @param in
	 *            the message to send
	 * @param exchange
	 *            the exchange
	 * @throws Exception
	 *             if message conversion to string fails
	 */
	protected void doSendToAll(WebsocketStore store, Message in, Exchange exchange) throws Exception {
		log.debug("Sending to all {}", in);

		Exception exception = null;

		Collection<DefaultWebsocket> websockets = store.getAll();
		if (CollectionUtils.isNotEmpty(websockets)) {
			String message = in.getMandatoryBody(String.class);
			for (DefaultWebsocket websocket : websockets) {
				try {
					if ((!(websocket instanceof FilterWebsocket)
							|| (websocket instanceof FilterWebsocket 
									&& ((FilterWebsocket) websocket).evaluate(in)))) {
						sendMessage(message, websocket);
					}
				} catch (Exception e) {
					if (exception == null) {
						exception = new CamelExchangeException("Failed to deliver message to one or more recipients.", exchange, e);
					}
				}
			}
		}
		if (exception != null) {
			throw exception;
		}
	}

	/**
	 * Checks whether the websocket conneciton is open, is so, the given message will be send
	 * 
	 * @param message
	 *            the message
	 * @param websocket
	 *            the websocket
	 * @throws IOException if message sending fails 
	 *             
	 */
	private void sendMessage(String message, DefaultWebsocket websocket) throws IOException {
		if (websocket.getConnection().isOpen()) {
			log.trace("Sending to websocket {} -> {}", websocket.getConnectionKey(), message);
			websocket.getConnection().sendMessage(message);
		}
	}
}