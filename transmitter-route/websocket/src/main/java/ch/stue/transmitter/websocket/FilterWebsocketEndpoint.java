package ch.stue.transmitter.websocket;

import java.util.Map;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.component.websocket.DefaultNodeSynchronization;
import org.apache.camel.component.websocket.MemoryWebsocketStore;
import org.apache.camel.component.websocket.NodeSynchronization;
import org.apache.camel.component.websocket.WebsocketComponent;
import org.apache.camel.component.websocket.WebsocketConsumer;
import org.apache.camel.component.websocket.WebsocketEndpoint;
import org.apache.camel.component.websocket.WebsocketProducer;
import org.apache.camel.component.websocket.WebsocketStore;
import org.apache.camel.util.ObjectHelper;
import org.apache.camel.util.ServiceHelper;

/**
 * Extends {@link WebsocketEndpoint} to instantiate a custom {@link FilterWebsocketComponent}
 * as Component
 * 
 * @author stue
 */
public class FilterWebsocketEndpoint extends WebsocketEndpoint {

    private NodeSynchronization sync;
    private WebsocketStore memoryStore;
    private FilterWebsocketComponent component;

    private String remaining; 

    /**
     * Constructor without memory store only with required parameters
     * @param component the component
     * @param uri the URI
     * @param remaining the remaining string
     * @param parameters further parameters
     */
    public FilterWebsocketEndpoint(FilterWebsocketComponent component, String uri, String remaining, Map<String, Object> parameters) {
        this(component, new MemoryWebsocketStore(), uri, remaining, parameters);
    }

    /**
     * Constructor with memory store and required parameters
     * @param component the component
     * @param websocketStore the websocketStore
     * @param uri the URI
     * @param remaining the remaining string
     * @param parameters further parameters
     */
    public FilterWebsocketEndpoint(FilterWebsocketComponent component, MemoryWebsocketStore websocketStore, String uri, String remaining, Map<String, Object> parameters) {
    	super(component, uri, remaining, parameters);
        this.remaining = remaining;
        this.memoryStore = websocketStore;
        this.sync = new DefaultNodeSynchronization(memoryStore);
        this.component = component;
	}

	/**
     * {@inheritDoc}
     */
    @Override
    public WebsocketComponent getComponent() {
        ObjectHelper.notNull(component, "component");
        return (WebsocketComponent) super.getComponent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        ObjectHelper.notNull(component, "component");
        WebsocketConsumer consumer = new WebsocketConsumer(this, processor);
        configureConsumer(consumer);
        return consumer;
    }

    /**
     * {@inheritDoc}
     */
	@Override
	public Producer createProducer() throws Exception {
		return new FilterWebsocketProducer(this, memoryStore);
	}
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void connect(WebsocketConsumer consumer) throws Exception {
        component.connect(consumer);
        component.addServlet(sync, consumer, remaining);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect(WebsocketConsumer consumer) throws Exception {
        component.disconnect(consumer);
        // Servlet should be removed
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect(WebsocketProducer producer) throws Exception {
        component.connect(producer);
        component.addServlet(sync, producer, remaining);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect(WebsocketProducer producer) throws Exception {
        component.disconnect(producer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doStart() throws Exception {
        ServiceHelper.startService(memoryStore);
        super.doStart();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doStop() throws Exception {
        ServiceHelper.stopService(memoryStore);
        super.doStop();
    }
}
