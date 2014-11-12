package ch.trackdata.sbs1route.websocket;

import org.apache.commons.collections4.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.trackdata.sbs1route.message.TrackPositionMessage;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

/**
 * This predicate filters by boundingBox.
 * 
 * The given {@link TrackPositionMessages} are within the configured BoundingBox
 * 
 * If boundingBox is null every TrackPosition will be accepted
 * 
 * @author stue
 */
public class TrackPositionGeometryFilter implements Predicate<TrackPositionMessage> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TrackPositionGeometryFilter.class);

	private Geometry boundingBox;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean evaluate(TrackPositionMessage trackPositionMessage) {
		if(boundingBox != null){
			Point position = TrackPositionGeometryHelper.getGeometry(trackPositionMessage.getGeometry());
			if(position != null && !boundingBox.contains(position)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @return the bounding box geometry
	 */
	public Geometry getBoundingBox() {
		return boundingBox;
	}

	/**
	 * @param boundingBox the boundingBox to set
	 */
	public void setBoundingBox(Geometry boundingBox) {
		LOGGER.trace("bounding box geometry set");
		this.boundingBox = boundingBox;
	}
}
