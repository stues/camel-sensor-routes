package ch.trackdata.sbs1route.websocket;

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
                sendMessage(in, websocket);
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
     * @param in the message
     * @return send to all
     */
    protected boolean sendToAllSet(Message in) {
        // header may be null; have to be careful here (and fallback to use sendToAll option configured from endpoint)
        Boolean value = in.getHeader(WebsocketConstants.SEND_TO_ALL, sendToAll, Boolean.class);
        return value == null ? false : value;
    }

    /**
     * Sends the message to all Websocket
     * @param store the websocket store
     * @param in the message to send
     * @param exchange the exchange
     * @throws Exception if message conversion to string fails
     */
    protected void doSendToAll(WebsocketStore store, Message in, Exchange exchange) throws Exception {
        log.debug("Sending to all {}", in);
        Collection<DefaultWebsocket> websockets = store.getAll();
        Exception exception = null;
        for (DefaultWebsocket websocket : websockets) {
            try {
	        	sendMessage(in, websocket);
            } catch (Exception e) {
                if (exception == null) {
                    exception = new CamelExchangeException("Failed to deliver message to one or more recipients.", exchange, e);
                }
            }
        }
        if (exception != null) {
            throw exception;
        }
    }

    /**
     * Checks what type of Websocket the given websocket is, and handles the message according
     * @param in the message
     * @param websocket the websocket
     * @throws Exception if message conversion to string fails
     */
	private void sendMessage(Message in, DefaultWebsocket websocket) throws Exception {
		if(websocket instanceof FilterWebsocket){
			filterSendMessage((FilterWebsocket)websocket, in);
		}
		else{
			String message = in.getMandatoryBody(String.class);
			defaultSendMessage(websocket, message);
		}
	}

	/**
	 * Sends the message to a {@link FilterWebsocket}
	 * @param websocket the websocket
	 * @param message the message to send
	 * @throws Exception if message conversion to string fails
	 */
    protected void filterSendMessage(FilterWebsocket websocket, Message message) throws Exception {
        // in case there is web socket and socket connection is open - send message
        if (websocket.getConnection().isOpen()) {
            log.trace("Sending to websocket {} -> {}", websocket.getConnectionKey(), message);
            websocket.sendMessageToConnection(message);
        }
    }
    
    /**
	 * Sends the message to a {@link DefaultWebsocket}
	 * @param websocket the websocket
	 * @param message the string message to send
	 * @throws IOException is thrown if sending fails
	 */
    protected void defaultSendMessage(final DefaultWebsocket websocket, final String message) throws IOException {
        // in case there is web socket and socket connection is open - send message
        if (websocket.getConnection().isOpen()) {
            log.trace("Sending to websocket {} -> {}", websocket.getConnectionKey(), message);
            websocket.getConnection().sendMessage(message);
        }
    }
}