package ch.trackdata.sbs1route.websocket;

import java.util.Collection;
import java.util.Collections;

import org.apache.camel.InvalidPayloadException;
import org.apache.camel.Message;
import org.apache.camel.RuntimeCamelException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.trackdata.sbs1route.message.GeoJSONFeature;
import ch.trackdata.sbs1route.message.PointGeometry;

/**
 * Predicate Filter which iterates over all configured predicates. It accepts
 * the given object only if all predicates accept the object. If no predicate is set it will be accepted
 * 
 * @author stue
 */
public class TrackPositionMessageFilter implements Predicate<Message> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrackPositionMessageFilter.class);

	private Collection<Predicate<GeoJSONFeature<PointGeometry>>> predicates;

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean evaluate(Message message) {
		GeoJSONFeature<PointGeometry> object;
		try {
			object = message.getMandatoryBody(GeoJSONFeature.class);
			if (object instanceof GeoJSONFeature && CollectionUtils.isNotEmpty(predicates)) {
				for (Predicate<GeoJSONFeature<PointGeometry>> predicate : predicates) {
					if (!predicate.evaluate(object)) {
						return false;
					}
				}
			}
			return true;
		} catch (InvalidPayloadException e) {
			LOGGER.warn("An error occurred while converting message to a track position message: " + message, e);
			throw new RuntimeCamelException("An error occurred while converting message to a track position message: " + message,e);
		}
	}

	/**
	 * @return the predicates
	 */
	public Collection<Predicate<GeoJSONFeature<PointGeometry>>> getPredicates() {
		return predicates;
	}

	/**
	 * @param predicates a collection of the predicates to set
	 */
	public void setPredicates(Collection<Predicate<GeoJSONFeature<PointGeometry>>> predicates) {
		LOGGER.trace("Set Predicates");
		this.predicates = predicates;
	}

	/**
	 * @param predicate a single predicate to set
	 */
	public void setPredicate(Predicate<GeoJSONFeature<PointGeometry>> predicate) {
		LOGGER.trace("Single Predicate set");
		this.predicates = Collections.singleton(predicate);
	}
}
