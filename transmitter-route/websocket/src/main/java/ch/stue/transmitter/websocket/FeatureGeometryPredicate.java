package ch.stue.transmitter.websocket;

import org.apache.commons.collections4.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.stue.transmitter.websocket.GeometryConverterHelper;
import ch.stue.transmitter.websocket.domain.SpatialPredicate;
import ch.stue.domain.Feature;
import ch.stue.domain.Geometry;

/**
 * This predicate filters by boundingBox.
 *
 * The given {@link TrackPositionMessages} are within the configured BoundingBox
 *
 * If boundingBox is null every TrackPosition will be rejected
 *
 * @author stue
 */
public class FeatureGeometryPredicate implements Predicate<Feature<Geometry<?>>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(FeatureGeometryPredicate.class);

	private SpatialPredicate spatialPredicate;

	private com.vividsolutions.jts.geom.Geometry geometry;

	/**
	 * Default Constructor
	 */
	public FeatureGeometryPredicate() {
		// Nothing to do!
	}

	/**
	 * Constructor with geometry
	 *
	 * @param geometry
	 *            the geometry
	 */
	public FeatureGeometryPredicate(com.vividsolutions.jts.geom.Geometry geometry) {
		this.geometry = geometry;
	}

	/**
	 * Constructor with geometry
	 *
	 * @param geometry
	 *            the geometry
	 * @param spatialPredicate
	 *            the spatial predicate
	 */
	public FeatureGeometryPredicate(com.vividsolutions.jts.geom.Geometry geometry, SpatialPredicate spatialPredicate) {
		this.geometry = geometry;
		this.spatialPredicate = spatialPredicate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean evaluate(Feature<Geometry<?>> trackPositionMessage) {
		if (this.spatialPredicate != null && this.geometry != null) {
			com.vividsolutions.jts.geom.Geometry featureGeometry = GeometryConverterHelper.getGeometry(trackPositionMessage.getGeometry());
			if (featureGeometry != null) {
				return this.spatialPredicate.evaluate(featureGeometry, this.geometry);
			}
		}
		return false;
	}

	/**
	 * @return the geometry
	 */
	public com.vividsolutions.jts.geom.Geometry getGeometry() {
		return this.geometry;
	}

	/**
	 * @param geometry
	 *            the geometry to set
	 */
	public void setGeometry(com.vividsolutions.jts.geom.Geometry geometry) {
		LOGGER.trace("geometry set");
		this.geometry = geometry;
	}

	/**
	 * @return the spatial predicate
	 */
	public SpatialPredicate getSpatialPredicate() {
		return this.spatialPredicate;
	}

	/**
	 * @param spatialPredicate the spatial predicate to set
	 */
	public void setSpatialPredicate(SpatialPredicate spatialPredicate) {
		LOGGER.trace("spatial predicate set: {}", spatialPredicate);
		this.spatialPredicate = spatialPredicate;
	}
}
