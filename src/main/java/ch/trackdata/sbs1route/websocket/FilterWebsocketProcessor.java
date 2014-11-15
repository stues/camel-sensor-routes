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
public class FilterWebsocketProcessor implements Processor {

	private static final String ACTION_PROPERTY_NAME = "action";

	private static final String CLEAR_FILTER_ACTION_NAME = "clearFilter";
	private static final String SET_AREA_FILTER_ACTION_NAME = "setAreaFilter";

	private static final Logger LOGGER = LoggerFactory.getLogger(FilterWebsocketProcessor.class);

	private WebsocketStore websocketStore;

	/**
	 * @return the websocketStore
	 */
	public WebsocketStore getWebsocketStore() {
		return websocketStore;
	}

	/**
	 * @param websocketStore
	 *            the websocketStore to set
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
		GeoJSONFeature<?> geoJSONFeature = in.getMandatoryBody(GeoJSONFeature.class);

		String action = geoJSONFeature.getProperty(ACTION_PROPERTY_NAME, String.class);
		if (action != null) {
			AbstractGeometry<?> geometry = geoJSONFeature.getGeometry();

			String connectionKey = (String) in.getHeader(WebsocketConstants.CONNECTION_KEY);
			DefaultWebsocket websocket = websocketStore.get(connectionKey);

			if (websocket instanceof FilterWebsocket) {
				FilterWebsocket filterWebsocket = (FilterWebsocket) websocket;

				TrackPositionMessageFilter messageFilter;
				if (filterWebsocket.getFilter() instanceof TrackPositionMessageFilter) {
					messageFilter = (TrackPositionMessageFilter) filterWebsocket.getFilter();
				} else {
					messageFilter = new TrackPositionMessageFilter();
					filterWebsocket.setFilter(messageFilter);
				}

				if (SET_AREA_FILTER_ACTION_NAME.equals(action)) {
					if (geometry instanceof PolygonGeometry) {
						Polygon polygon = GeometryConverterHelper.getGeometry((PolygonGeometry) geometry);
						if (polygon != null) {

							TrackPositionGeometryFilter geometryFilter = new TrackPositionGeometryFilter(polygon);

							messageFilter.setPredicate(geometryFilter);

						} else {
							LOGGER.trace("polygon is null");
						}
					}
				} else if (CLEAR_FILTER_ACTION_NAME.equals(action)) {
					messageFilter.clearPredicates();
				}
			}
		}

	}

}
