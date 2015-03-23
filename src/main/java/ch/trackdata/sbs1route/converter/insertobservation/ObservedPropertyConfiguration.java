package ch.trackdata.sbs1route.converter.insertobservation;

import ch.trackdata.sbs1route.message.Feature;

/**
 * A interface which can be used to describe a Observation including a ObservedProperty
 * 
 * @author stue
 *
 * @param <T> the Type of Object which will be added as result
 */
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
	 * @return the createNullValueMessages
	 */
	public abstract boolean isCreateNullValueMessages();
	
	/**
	 * @return the observed value
	 */
	public abstract T getValue(Feature<?> feature);
	
}