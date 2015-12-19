package ch.stue.transmitter.sos.converter.insertobservation;

import java.util.List;

import ch.stue.domain.Feature;

public interface InsertObservationSOSV2Configuration {

	/**
	 * @return the type of feature for which this configuration can be used
	 */
	public abstract Class<? extends Feature<?>> getFeatureType();

	/**
	 * @return the service
	 */
	public abstract String getService();

	/**
	 * @return the version
	 */
	public abstract String getVersion();

	/**
	 * @return the observedProperties
	 */
	public abstract List<? extends ObservedPropertyConfiguration<?>> getObservedProperties();

	/**
	 * @return the offerings
	 */
	public abstract List<String> getOfferings();

}