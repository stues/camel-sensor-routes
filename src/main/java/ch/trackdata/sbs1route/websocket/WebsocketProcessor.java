package ch.trackdata.sbs1route.websocket;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.websocket.DefaultWebsocket;
import org.apache.camel.component.websocket.WebsocketConstants;
import org.apache.camel.component.websocket.WebsocketStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.trackdata.sbs1route.message.AbstractGeometry;
import ch.trackdata.sbs1route.message.GeoJSONFeature;
import ch.trackdata.sbs1route.message.PolygonGeometry;
import ch.trackdata.sbs1route.message.SBS1Message;

import com.vividsolutions.jts.geom.Polygon;

/**
 * Extracts the {@link SBS1Message} from the exchanged List
 */
public class WebsocketProcessor implements Processor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketProcessor.class);

	private WebsocketStore websocketStore;
		
	/**
	 * @return the websocketStore
	 */
	public WebsocketStore getWebsocketStore() {
		return websocketStore;
	}

	/**
	 * @param websocketStore the websocketStore to set
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
		GeoJSONFeature<?> message = in.getMandatoryBody(GeoJSONFeature.class);
		
		AbstractGeometry<?> geometry = message.getGeometry();
		
		if(geometry instanceof PolygonGeometry){
			Polygon polygon = GeometryConverterHelper.getGeometry((PolygonGeometry)geometry);
			if(polygon != null){
				String connectionKey = (String)in.getHeader(WebsocketConstants.CONNECTION_KEY);
				DefaultWebsocket websocket = websocketStore.get(connectionKey);
				if(websocket instanceof FilterWebsocket){
					
					TrackPositionGeometryFilter geometryFilter = new TrackPositionGeometryFilter(polygon);
					
					TrackPositionMessageFilter messageFilter = new TrackPositionMessageFilter();
					messageFilter.setPredicate(geometryFilter);
					
					((FilterWebsocket) websocket).setFilter(messageFilter);
				}
				else{
					LOGGER.trace("websocket NOT in store");
				}
			}
		}
				
	}

}
