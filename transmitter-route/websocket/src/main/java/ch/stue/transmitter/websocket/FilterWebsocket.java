package ch.stue.transmitter.websocket;

import org.apache.camel.Message;
import org.apache.camel.component.websocket.DefaultWebsocket;
import org.apache.camel.component.websocket.NodeSynchronization;
import org.apache.camel.component.websocket.WebsocketConsumer;
import org.apache.commons.collections4.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extends the {@link DefaultWebsocket} with the ability to filter messages
 * 
 * @author stue
 */
public class FilterWebsocket extends DefaultWebsocket implements Predicate<Message> {
	
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(FilterWebsocket.class);

	private Predicate<Message> filter;
	
	/**
	 * Default constructor
	 */
	public FilterWebsocket(NodeSynchronization sync, WebsocketConsumer consumer){
		super(sync, consumer);
	}
	
	/**
	 * @return the filter
	 */
	public Predicate<Message> getFilter() {
		return filter;
	}

	/**
	 * @param filter the filter to set
	 */
	public void setFilter(Predicate<Message> filter) {
		LOGGER.trace("filter predicate set");
		this.filter = filter;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean evaluate(Message object) {
		if(filter == null || filter.evaluate(object)){
			return true;
		}
		return false;
	}
}
