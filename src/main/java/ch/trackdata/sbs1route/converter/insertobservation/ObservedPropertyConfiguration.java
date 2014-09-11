package ch.trackdata.sbs1route.converter.insertobservation;

import ch.trackdata.sbs1route.message.GeoJSONFeature;

public interface ObservedPropertyConfiguration<T> {

	/**
	 * @return the observationName
	 */
	public abstract String getObservationName();

	/**
	 * @return the procedure
	 */
	public abstract String getProcedure();

	/**
	 * @return the observedProperty
	 */
	public abstract String getObservedProperty();

	/**
	 * @return the featureOfInterestPrefix
	 */
	public abstract String getFeatureOfInterestPrefix();

	/**
	 * @return the featureIdentPropertyName
	 */
	public abstract String getFeatureIdentPropertyName();

	/**
	 *  @return the featureTitlePropertyName
	 */
	public abstract String getFeatureTitlePropertyName();
	
	/**
	 * @return the observed value
	 */
	public abstract T getValue(GeoJSONFeature feature);
	
}