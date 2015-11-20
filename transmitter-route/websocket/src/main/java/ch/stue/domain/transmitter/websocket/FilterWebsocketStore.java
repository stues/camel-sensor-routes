package ch.stue.domain.transmitter.websocket;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.camel.component.websocket.DefaultWebsocket;
import org.apache.camel.component.websocket.MemoryWebsocketStore;
import org.apache.commons.collections4.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extends the {@link MemoryWebsocketStore} with the ability to store a message filter per 
 * websocket key
 * 
 * @author stue
 */
public class FilterWebsocketStore extends MemoryWebsocketStore {
	
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(FilterWebsocketStore.class);

	private ConcurrentMap<String, Predicate<Object>> filterMap;
	
	/**
	 * Default constructor
	 */
	public FilterWebsocketStore(){
		LOGGER.debug("instantiating: ", FilterWebsocketStore.class.getSimpleName());
		filterMap = new ConcurrentHashMap<String, Predicate<Object>>();
	}
	
	/**
	 * Returns the Filter for the given connection key
	 * @param key the key
	 * @return the Predicate for the given connection key
	 */
	public Predicate<Object> getFilter(String key){
		return filterMap.get(key);
	}
	
	/**
	 * Returns the Predicate for the given websocket
	 * @param ws the websocket
	 * @return the Predicate for the given websocket
	 */
	public Predicate<Object> getFilter(DefaultWebsocket ws){
		return getFilter(ws.getConnectionKey());
	}
	
	/**
	 * Sets the filter for the given websocket conenction key
	 * @param key the connection key
	 * @param predicate the Filter Predicate
	 */
	public void setFilter(String key, Predicate<Object> predicate){
		filterMap.put(key, predicate);
	}
	
	/**
	 * Sets the filter for the given Websocket
	 * @param ws the websocket
	 * @param predicate the Filter Predicate
	 */
	public void setFilter(DefaultWebsocket ws, Predicate<Object> predicate){
		setFilter(ws.getConnectionKey(), predicate);
	}
	
	/**
	 * Adds the given Websocket with the given Predicate to filter messages
	 * @param ws the websocket
	 * @param predicate the predicate for the given Websocket
	 */
	public void add(DefaultWebsocket ws, Predicate<Object> predicate){
		super.add(ws);
		setFilter(ws, predicate);
	}
	
	/**
	 * Returns whether the given {@link DefaultWebsocket} has a filter set or not
	 * @param ws the websocket
	 * @return true if websocket has a filter otherwise false
	 */
	public boolean isFilterSet(DefaultWebsocket ws){
		return isFilterSet(ws.getConnectionKey());
	}
	
	/**
	 * 
	 * @param connectionKey
	 * @return
	 */
	private boolean isFilterSet(String connectionKey) {	
		return filterMap.containsKey(connectionKey);
	}

	/**
     * {@inheritDoc}
     */
    public void remove(DefaultWebsocket ws){
    	filterMap.remove(ws.getConnectionKey());
    	super.remove(ws);
    }

    /**
     * {@inheritDoc}
     */
    public void remove(String key){
    	filterMap.remove(key);
    	super.remove(key);
    }
	
    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() throws Exception {
        filterMap.clear();
        super.stop();
    }
}
