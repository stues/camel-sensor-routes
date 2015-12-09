package ch.stue.transmitter.sos.converter.insertobservation;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Abstract class which almost implements all the Methods of
 * {@link ObservedPropertyConfiguration}
 * 
 * @author stue
 * 
 * @param <T>
 *            the Type of Object which will be added as result
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = AbstractPropertyConfiguration.TYPE_PROPERTY_NAME)
@JsonSubTypes({ 
	@JsonSubTypes.Type(value = PointGeometryPropertyConfiguration.class, name = PointGeometryPropertyConfiguration.POINT_GEOMETRY_TYPE_STRING),
	@JsonSubTypes.Type(value = MeasurementPropertyConfiguration.class, name = MeasurementPropertyConfiguration.MEASUREMENT_TYPE_STRING),
	@JsonSubTypes.Type(value = TextPropertyConfiguration.class, name = TextPropertyConfiguration.TEXT_TYPE_STRING)})
public abstract class AbstractPropertyConfiguration<T> implements ObservedPropertyConfiguration<T> {

	public static final String TYPE_PROPERTY_NAME = "type";

	private String observationName;

	private String procedure;

	private String observedProperty;

	private String featureOfInterestPrefix;

	private String featureIdentPropertyName;

	private String featureTitlePropertyName;

	private String phenomenonTimePropertyName;

	private boolean createNullValueMessages;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getObservationName() {
		return observationName;
	}

	/**
	 * @param observationName
	 *            the observationName to set
	 */
	public void setObservationName(String observationName) {
		this.observationName = observationName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getProcedure() {
		return procedure;
	}

	/**
	 * @param procedure
	 *            the procedure to set
	 */
	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getObservedProperty() {
		return observedProperty;
	}

	/**
	 * @param observedProperty
	 *            the observedProperty to set
	 */
	public void setObservedProperty(String observedProperty) {
		this.observedProperty = observedProperty;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFeatureOfInterestPrefix() {
		return featureOfInterestPrefix;
	}

	/**
	 * @param featureOfInterestPrefix
	 *            the featureOfInterestPrefix to set
	 */
	public void setFeatureOfInterestPrefix(String featureOfInterestPrefix) {
		this.featureOfInterestPrefix = featureOfInterestPrefix;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFeatureIdentPropertyName() {
		return featureIdentPropertyName;
	}

	/**
	 * @param featureIdentPropertyName
	 *            the featureIdentPropertyName to set
	 */
	public void setFeatureIdentPropertyName(String featureIdentPropertyName) {
		this.featureIdentPropertyName = featureIdentPropertyName;
	}

	/**
	 * @return the featureTitlePropertyName
	 */
	public String getFeatureTitlePropertyName() {
		return featureTitlePropertyName;
	}

	/**
	 * @param featureTitlePropertyName
	 *            the featureTitlePropertyName to set
	 */
	public void setFeatureTitlePropertyName(String featureTitlePropertyName) {
		this.featureTitlePropertyName = featureTitlePropertyName;
	}

	/**
	 * @return the createNullValueMessages
	 */
	public boolean isCreateNullValueMessages() {
		return createNullValueMessages;
	}

	/**
	 * @param createNullValueMessages
	 *            the createNullValueMessages to set
	 */
	public void setCreateNullValueMessages(boolean createNullValueMessages) {
		this.createNullValueMessages = createNullValueMessages;
	}

	/**
	 * @return the phenomenonTimePropertyName
	 */
	public String getPhenomenonTimePropertyName() {
		return phenomenonTimePropertyName;
	}

	/**
	 * @param phenomenonTimePropertyName
	 *            the phenomenonTimePropertyName to set
	 */
	public void setPhenomenonTimePropertyName(String phenomenonTimePropertyName) {
		this.phenomenonTimePropertyName = phenomenonTimePropertyName;
	}
}
