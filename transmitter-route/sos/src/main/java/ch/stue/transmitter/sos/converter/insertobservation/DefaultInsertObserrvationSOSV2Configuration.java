package ch.stue.transmitter.sos.converter.insertobservation;

import java.util.List;

/**
 * Default Insert observation sos v2.0 configuration
 * @author stue
 */
public class DefaultInsertObserrvationSOSV2Configuration implements InsertObservationSOSV2Configuration {

	private static final String SERVICE_NAME = "SOS";
	private static final String SOS_VERSION = "2.0.0";
	
	private List<? extends ObservedPropertyConfiguration<?>> observedProperties;
	private List<String> offerings;

	@Override
	public String getService() {
		return SERVICE_NAME;
	}

	@Override
	public String getVersion() {
		return SOS_VERSION;
	}

	@Override
	public List<? extends ObservedPropertyConfiguration<?>> getObservedProperties() {
		return observedProperties;
	}

	@Override
	public List<String> getOfferings() {
		return offerings;
	}

	/**
	 * @param observedProperties the observedProperties to set
	 */
	public void setObservedProperties(List<? extends ObservedPropertyConfiguration<?>> observedProperties) {
		this.observedProperties = observedProperties;
	}

	/**
	 * @param offerings the offerings to set
	 */
	public void setOfferings(List<String> offerings) {
		this.offerings = offerings;
	}
}