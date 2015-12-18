package ch.stue.transmitter.sos.converter.insertobservation;

import java.util.List;

public interface InsertObservationSOSV2Configuration {

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