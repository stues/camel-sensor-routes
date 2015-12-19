package ch.stue.transmitter.sos.converter.insertobservation;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;

/**
 * Default Insert observation sos v2.0 configuration
 * @author stue
 */
public class DefaultInsertObservationSOSV2Configuration implements InsertObservationSOSV2Configuration, InitializingBean {

	private static final String SERVICE_NAME = "SOS";
	private static final String SOS_VERSION = "2.0.0";

	public static final String PROCEDURE_DEFAULT = "procedure";
	public static final String OBSERVED_PROPERTY_PREFIX_DEFAULT = "observedPropertyPrefix";
	public static final String FEATURE_OF_INTEREST_PREFIX_DEFAULT = "featureOfInterestPrefix";
	public static final String FEATURE_IDENT_PROPERTY_NAME_DEFAULT = "featureIdentPropertyName";
	public static final String FEATURE_TITLE_PROPERTY_NAME_DEFAULT = "featureTitlePropertyName";
	public static final String PHENOMENON_TIME_PROPERTY_DEFAULT = "phenomenonTimePropertyName";
	public static final String CREATE_NULL_VALUE_MESSAGES_DEFAULT = "createNullValueMessages";

	private List<? extends ObservedPropertyConfiguration<?>> observedProperties;
	private List<String> offerings;
	private Map<String, Object> defaultValues;

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
		return this.observedProperties;
	}

	@Override
	public List<String> getOfferings() {
		return this.offerings;
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

	/**
	 * @return the defaultValues
	 */
	public Map<String, Object> getDefaultValues() {
		return this.defaultValues;
	}

	/**
	 * @param defaultValues the defaultValues to set
	 */
	public void setDefaultValues(Map<String, Object> defaultValues) {
		this.defaultValues = defaultValues;
	}

	/**
	 * Does update the observed properties with the default values
	 */
	protected void updateObservedProperties(){
		List<? extends ObservedPropertyConfiguration<?>> currentObservedProperties = getObservedProperties();
		if(CollectionUtils.isNotEmpty(currentObservedProperties)){
			for(ObservedPropertyConfiguration<?> currentObservedProperty : currentObservedProperties){
				currentObservedProperty.setDefaultValues(getDefaultValues());
			}
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		updateObservedProperties();
	}
}