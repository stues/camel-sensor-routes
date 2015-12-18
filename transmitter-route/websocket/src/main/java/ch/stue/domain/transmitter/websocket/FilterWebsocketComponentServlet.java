package ch.stue.domain.transmitter.websocket;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.apache.camel.component.websocket.NodeSynchronization;
import org.apache.camel.component.websocket.WebSocketFactory;
import org.apache.camel.component.websocket.WebsocketComponentServlet;
import org.eclipse.jetty.websocket.WebSocket;

/**
 * Extends the {@link WebsocketComponentServlet} to instantiate a {@link FilterWebsocket} within the
 * {@link #doWebSocketConnect(HttpServletRequest, String)} Method
 *
 * @author stue
 */
public class FilterWebsocketComponentServlet extends WebsocketComponentServlet {

	private static final long serialVersionUID = 1L;
	private final NodeSynchronization sync;

	/**
	 * Constructor with {@link NodeSynchronization}
	 * @param sync the node synchronization object
	 */
	public FilterWebsocketComponentServlet(NodeSynchronization sync) {
		super(sync, Collections.<String, WebSocketFactory>emptyMap());
		this.sync = sync;
	}

	/**
	 * Derived to instantiate custom {@link FilterWebsocket}
	 *
	 * {@inheritDoc}
	 */
	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
		return new FilterWebsocket(this.sync, getConsumer());
	}

}
