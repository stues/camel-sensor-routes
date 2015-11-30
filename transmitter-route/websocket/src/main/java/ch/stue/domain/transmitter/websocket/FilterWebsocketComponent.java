package ch.stue.domain.transmitter.websocket;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.component.websocket.MemoryWebsocketStore;
import org.apache.camel.component.websocket.NodeSynchronization;
import org.apache.camel.component.websocket.WebsocketComponent;
import org.apache.camel.component.websocket.WebsocketComponentServlet;
import org.apache.camel.component.websocket.WebsocketConsumer;
import org.apache.camel.component.websocket.WebsocketEndpoint;
import org.apache.camel.component.websocket.WebsocketProducer;
import org.apache.camel.util.jsse.SSLContextParameters;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Derived to instantiate a custom {@link WebsocketEndpoint}
 * 
 * @author stue
 */
public class FilterWebsocketComponent extends WebsocketComponent {

	/**
	 * Whole method copied from superclass 
	 * to instantiate the custom Endpoint 
	 *
	 * {@inheritDoc}
	 */
    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
		SSLContextParameters sslContextParameters = (SSLContextParameters) resolveAndRemoveReferenceParameter(parameters, "sslContextParametersRef", SSLContextParameters.class);
		if (sslContextParameters == null) {
			sslContextParameters = (SSLContextParameters) resolveAndRemoveReferenceParameter(parameters, "sslContextParameters", SSLContextParameters.class);
		}
        Boolean enableJmx = getAndRemoveParameter(parameters, "enableJmx", Boolean.class);
        String staticResources = getAndRemoveParameter(parameters, "staticResources", String.class);
        MemoryWebsocketStore websocketStore = resolveAndRemoveReferenceParameter(parameters, "websocketStore", MemoryWebsocketStore.class);
        int port = extractPortNumber(remaining);
        String host = extractHostName(remaining);

        WebsocketEndpoint endpoint = createWebsocketEndpoint(websocketStore, uri, remaining, parameters);

		if (enableJmx != null)
			endpoint.setEnableJmx(enableJmx.booleanValue());
		else {
			endpoint.setEnableJmx(isEnableJmx());
		}

		if (sslContextParameters == null) {
			sslContextParameters = getSslContextParameters();
		}

		if (sslContextParameters != null) {
			endpoint.setSslContextParameters(sslContextParameters);
		}

		if (staticResources == null) {
			staticResources = getStaticResources();
		}

		if (staticResources != null) {
			endpoint.setStaticResources(staticResources);
		}

		endpoint.setSslContextParameters(sslContextParameters);
		endpoint.setPort(port);
		endpoint.setHost(host);

		setProperties(endpoint, parameters);
        return endpoint;
    }

    /**
     * Can be derived in subclasses to instantiate a custom WebsocketEndpoint
     * @param websocketStore 
     * @param uri the uri
     * @param remaining the path
     * @param parameters further parameters
     * @return the WebsocketEndpoint instance
     */
	protected WebsocketEndpoint createWebsocketEndpoint(MemoryWebsocketStore websocketStore, String uri, String remaining, Map<String, Object> parameters) {
		if(websocketStore != null){
			return new FilterWebsocketEndpoint(this, websocketStore, uri, remaining, parameters);
		}
		else{
			return new FilterWebsocketEndpoint(this, uri, remaining, parameters);
		}
	}
    
	/**
	 * Copied from superclass 
	 * @param remaining the remaining
	 * @return the port number
	 */
    protected int extractPortNumber(String remaining) {
        int index1 = remaining.indexOf(":");
        int index2 = remaining.indexOf("/");

        if ((index1 != -1) && (index2 != -1)) {
            String result = remaining.substring(index1 + 1, index2);
            return Integer.parseInt(result);
        } else {
            return port;
        }
    }
    
	/**
	 * Copied from superclass 
	 * @param remaining the remaining
	 * @return the host name
	 */
    protected String extractHostName(String remaining) {
        int index = remaining.indexOf(":");
        if (index != -1) {
            return remaining.substring(0, index);
        } else {
            return host;
        }
    }
	
    /**
     * Derived to make Method public
     * 
     * {@inheritDoc}
     */
    public WebsocketComponentServlet addServlet(NodeSynchronization sync, WebsocketConsumer consumer, String remaining) throws Exception {
    	return super.addServlet(sync, consumer, remaining);
    }

    /**
     * Derived to make Method public
     * 
     * {@inheritDoc}
     */
    public WebsocketComponentServlet addServlet(NodeSynchronization sync, WebsocketProducer producer, String remaining) throws Exception {
    	return super.addServlet(sync, producer, remaining);
    }
    
    /**
     * Derived to instantiate custom {@link FilterWebsocketComponentServlet}
     * 
     * {@inheritDoc}
     */
    protected WebsocketComponentServlet createServlet(NodeSynchronization sync, String pathSpec, Map<String, WebsocketComponentServlet> servlets, ServletContextHandler handler) {
        WebsocketComponentServlet servlet = new FilterWebsocketComponentServlet(sync);
        servlets.put(pathSpec, servlet);
        handler.addServlet(new ServletHolder(servlet), pathSpec);
        return servlet;
    }
}
