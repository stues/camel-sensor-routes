package ch.trackdata.sbs1route.websocket;

import javax.servlet.http.HttpServletRequest;

import org.apache.camel.component.websocket.MemoryWebsocketStore;
import org.apache.camel.component.websocket.NodeSynchronization;
import org.apache.camel.component.websocket.WebsocketComponentServlet;
import org.eclipse.jetty.websocket.WebSocket;

/**
 * Extends the {@link MemoryWebsocketStore} with the ability to store a message filter per 
 * websocket key
 * 
 * @author stue
 */
public class FilterWebsocketComponentServlet extends WebsocketComponentServlet {

	private static final long serialVersionUID = 1L;
	private NodeSynchronization sync;
	
	public FilterWebsocketComponentServlet(NodeSynchronization sync) {
		super(sync);
		this.sync = sync;
	}
	
    @Override
    public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
        return new FilterWebsocket(sync, getConsumer());
    }
	
}
