package ch.stue.domain.transmitter.websocket;

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

	private final NodeSynchronization sync;
	private final WebsocketStore memoryStore;
	private final FilterWebsocketComponent component;

	private final String remaining;

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
		this.sync = new DefaultNodeSynchronization(this.memoryStore);
		this.component = component;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WebsocketComponent getComponent() {
		ObjectHelper.notNull(this.component, "component");
		return super.getComponent();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Consumer createConsumer(Processor processor) throws Exception {
		ObjectHelper.notNull(this.component, "component");
		WebsocketConsumer consumer = new WebsocketConsumer(this, processor);
		configureConsumer(consumer);
		return consumer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Producer createProducer() throws Exception {
		return new FilterWebsocketProducer(this, this.memoryStore);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void connect(WebsocketConsumer consumer) throws Exception {
		this.component.connect(consumer);
		this.component.addServlet(this.sync, consumer, this.remaining);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void disconnect(WebsocketConsumer consumer) throws Exception {
		this.component.disconnect(consumer);
		// Servlet should be removed
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void connect(WebsocketProducer producer) throws Exception {
		this.component.connect(producer);
		this.component.addServlet(this.sync, producer, this.remaining);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void disconnect(WebsocketProducer producer) throws Exception {
		this.component.disconnect(producer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doStart() throws Exception {
		ServiceHelper.startService(this.memoryStore);
		super.doStart();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doStop() throws Exception {
		ServiceHelper.stopService(this.memoryStore);
		super.doStop();
	}
}
