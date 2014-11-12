package ch.trackdata.sbs1route.websocket;

import java.util.Collection;
import java.util.Collections;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.trackdata.sbs1route.message.SBS1Message;
import ch.trackdata.sbs1route.message.TrackPositionMessage;

/**
 * Extracts the {@link SBS1Message} from the exchanged List
 */
public class TrackPositionMessageFilter implements Predicate<Object> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TrackPositionMessageFilter.class);

	private Collection<Predicate<TrackPositionMessage>> predicates;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean evaluate(Object object) {
		if (object instanceof TrackPositionMessage
				&& CollectionUtils.isNotEmpty(predicates)) {
			TrackPositionMessage trackPositionMessage = (TrackPositionMessage) object;
			for (Predicate<TrackPositionMessage> predicate : predicates) {
				if (!predicate.evaluate(trackPositionMessage)) {
					return false;
				}
			}
		}
		return true;
	}

	public Collection<Predicate<TrackPositionMessage>> getPredicates() {
		return predicates;
	}

	public void setPredicates(
			Collection<Predicate<TrackPositionMessage>> predicates) {
		LOGGER.trace("Set Predicates");
		this.predicates = predicates;
	}

	public void setPredicate(Predicate<TrackPositionMessage> predicate) {
		LOGGER.trace("Single Predicate set");
		this.predicates = Collections.singleton(predicate);
	}
}
