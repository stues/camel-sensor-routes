package ch.stue.transmitter.sos.converter.insertobservation;

import java.util.List;

/**
 * The Configuration can be used to Describe a InsertObservation
 * 
 * @author stue
 *
 */
public class InsertObservationSOSV2Configuration {
	

	private static final String SERVICE_NAME = "SOS";

	private static final String SOS_VERSION = "2.0.0";
		
	private List<? extends ObservedPropertyConfiguration<?>> observedProperties;
	
	private List<String> offerings;
		
	/**
	 * @return the service
	 */
	public String getService(){
		return SERVICE_NAME;
	}
	
	/**
	 * @return the version
	 */
	public String getVersion(){
		return SOS_VERSION;
	}
	
	/**
	 * @return the observedProperties
	 */
	public List<? extends ObservedPropertyConfiguration<?>> getObservedProperties() {
		return observedProperties;
	}

	/**
	 * @param observedProperties the observedProperties to set
	 */
	public void setObservedProperties(
			List<? extends ObservedPropertyConfiguration<?>> observedProperties) {
		this.observedProperties = observedProperties;
	}

	/**
	 * @return the offerings
	 */
	public List<String> getOfferings() {
		return offerings;
	}

	/**
	 * @param offerings the offerings to set
	 */
	public void setOfferings(List<String> offerings) {
		this.offerings = offerings;
	}
}
