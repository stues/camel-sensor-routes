package ch.stue.transmitter.sos.converter;

import java.util.HashMap;
import java.util.Map;

import ch.stue.domain.Feature;
import ch.stue.transmitter.sos.converter.insertobservation.InsertObservationSOSV2Configuration;

/**
 * Default Observation configuration
 *
 * @author stue
 */
public class SOSConfiguration {

	private Map<Class<? extends Feature<?>>, InsertObservationSOSV2Configuration> configurationMap;

	/**
	 * Default Constructor
	 */
	public SOSConfiguration() {
		this.configurationMap = new HashMap<Class<? extends Feature<?>>, InsertObservationSOSV2Configuration>();
	}

	/**
	 * @return the configurationMap
	 */
	public Map<Class<? extends Feature<?>>, InsertObservationSOSV2Configuration> getConfigurationMap() {
		return this.configurationMap;
	}

	/**
	 * @param configurationMap
	 *            the configurationMap to set
	 */
	public void setConfigurationMap(Map<Class<? extends Feature<?>>, InsertObservationSOSV2Configuration> configurationMap) {
		this.configurationMap = configurationMap;
	}

	/**
	 * Adds a insertObservationSOSV2Configuration
	 * if featureType is already configured it will be overwritten
	 * @param insertObservationSOSV2Configuration the insertObservationSOSV2Configuration
	 */
	public void addConfiguration(InsertObservationSOSV2Configuration insertObservationSOSV2Configuration){
		this.configurationMap.put(insertObservationSOSV2Configuration.getFeatureType(), insertObservationSOSV2Configuration);
	}

	/**
	 * Returns the configuration for the given feature
	 *
	 * @param feature
	 *            the feature
	 * @return the configuration for the given feature or null if no
	 *         configuration has been found for the given feature
	 */
	@SuppressWarnings("rawtypes")
	public InsertObservationSOSV2Configuration getConfiguration(Feature<?> feature) {
		Class<? extends Feature> featureClazz = feature != null ? feature.getClass() : null;
		if (this.configurationMap.containsKey(featureClazz)) {
			return this.configurationMap.get(featureClazz);
		}
		return null;
	}

}