package ch.stue.transmitter.sos.converter.insertobservation;

import static ch.stue.transmitter.sos.converter.insertobservation.DefaultInsertObservationSOSV2Configuration.CREATE_NULL_VALUE_MESSAGES_DEFAULT;
import static ch.stue.transmitter.sos.converter.insertobservation.DefaultInsertObservationSOSV2Configuration.FEATURE_IDENT_PROPERTY_NAME_DEFAULT;
import static ch.stue.transmitter.sos.converter.insertobservation.DefaultInsertObservationSOSV2Configuration.FEATURE_OF_INTEREST_PREFIX_DEFAULT;
import static ch.stue.transmitter.sos.converter.insertobservation.DefaultInsertObservationSOSV2Configuration.FEATURE_TITLE_PROPERTY_NAME_DEFAULT;
import static ch.stue.transmitter.sos.converter.insertobservation.DefaultInsertObservationSOSV2Configuration.OBSERVED_PROPERTY_PREFIX_DEFAULT;
import static ch.stue.transmitter.sos.converter.insertobservation.DefaultInsertObservationSOSV2Configuration.PHENOMENON_TIME_PROPERTY_DEFAULT;
import static ch.stue.transmitter.sos.converter.insertobservation.DefaultInsertObservationSOSV2Configuration.PROCEDURE_DEFAULT;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Abstract class which almost implements all the Methods of
 * {@link ObservedPropertyConfiguration}
 *
 * @author stue
 *
 * @param <T>
 *            the Type of Object which will be added as result
 */
public abstract class AbstractPropertyConfiguration<T> implements ObservedPropertyConfiguration<T> {

	public static final String TYPE_PROPERTY_NAME = "type";

	private String observationName;

	private String procedure;

	private String observedProperty;

	private String observedPropertySuffix;

	private String featureOfInterestPrefix;

	private String featureIdentPropertyName;

	private String featureTitlePropertyName;

	private String phenomenonTimePropertyName;

	private Boolean createNullValueMessages;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getObservationName() {
		return this.observationName;
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
		return this.procedure;
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
		return this.observedProperty;
	}

	/**
	 * @param observedProperty
	 *            the observedProperty to set
	 */
	public void setObservedProperty(String observedProperty) {
		this.observedProperty = observedProperty;
	}

	/**
	 * @param observedPropertySuffix
	 *            the observedPropertySuffix to set
	 */
	public void setObservedPropertySuffix(String observedPropertySuffix) {
		this.observedPropertySuffix = observedPropertySuffix;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFeatureOfInterestPrefix() {
		return this.featureOfInterestPrefix;
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
		return this.featureIdentPropertyName;
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
	@Override
	public String getFeatureTitlePropertyName() {
		return this.featureTitlePropertyName;
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
	@Override
	public Boolean isCreateNullValueMessages() {
		return this.createNullValueMessages;
	}

	/**
	 * @param createNullValueMessages
	 *            the createNullValueMessages to set
	 */
	public void setCreateNullValueMessages(Boolean createNullValueMessages) {
		this.createNullValueMessages = createNullValueMessages;
	}

	/**
	 * @return the phenomenonTimePropertyName
	 */
	public String getPhenomenonTimePropertyName() {
		return this.phenomenonTimePropertyName;
	}

	/**
	 * @param phenomenonTimePropertyName
	 *            the phenomenonTimePropertyName to set
	 */
	public void setPhenomenonTimePropertyName(String phenomenonTimePropertyName) {
		this.phenomenonTimePropertyName = phenomenonTimePropertyName;
	}

	@Override
	public void setDefaultValues(Map<String, Object> defaultValues) {
		if (MapUtils.isNotEmpty(defaultValues)) {
			Iterator<Entry<String, Object>> iterator = defaultValues.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> entry = iterator.next();
				switch (entry.getKey()) {
				case PROCEDURE_DEFAULT:
					applyProcedureDefaultValue(entry.getValue());
					break;
				case OBSERVED_PROPERTY_PREFIX_DEFAULT:
					applyObservedPropertyDefaultValue(entry.getValue());
					break;
				case FEATURE_OF_INTEREST_PREFIX_DEFAULT:
					applyFeatureOfInterestPrefixDefaultValue(entry.getValue());
					break;
				case FEATURE_IDENT_PROPERTY_NAME_DEFAULT:
					applyFeatureIdentPropertyNameDefaultValue(entry.getValue());
					break;
				case FEATURE_TITLE_PROPERTY_NAME_DEFAULT:
					applyFeatureTitlePropertyNameDefaultValue(entry.getValue());
					break;
				case PHENOMENON_TIME_PROPERTY_DEFAULT:
					applyPhenomenonTimePropertyNameDefaultValue(entry.getValue());
					break;
				case CREATE_NULL_VALUE_MESSAGES_DEFAULT:
					applyCreateNullValueMessagesDefaultValue(entry.getValue());
					break;
				default:
					// Nothing to do!
				}
			}
		}
	}

	/**
	 * @param value
	 *            set procedure value
	 */
	protected void applyProcedureDefaultValue(Object value) {
		if (value instanceof String && StringUtils.isEmpty(getProcedure())) {
			setProcedure((String) value);
		}
	}

	/**
	 * @param value
	 *            set the observed property value
	 */
	protected void applyObservedPropertyDefaultValue(Object value) {
		if (value instanceof String && StringUtils.isEmpty(getObservedProperty())) {
			String prefix = (String) value;
			String suffix = this.observedPropertySuffix != null ? this.observedPropertySuffix : StringUtils.EMPTY;
			boolean endsWithSlash = prefix.endsWith("/");
			boolean startsWithSlash = suffix.startsWith("/");
			String separator = !endsWithSlash && !startsWithSlash ? "/" : StringUtils.EMPTY;
			prefix = endsWithSlash && startsWithSlash ? prefix.substring(prefix.length()-2) : prefix;
			setObservedProperty(String.format("%s%s%s", prefix, separator, suffix));
		}
	}

	/**
	 * @param value
	 *            set featureOfInterestPrefix value
	 */
	protected void applyFeatureOfInterestPrefixDefaultValue(Object value) {
		if (value instanceof String && StringUtils.isEmpty(getFeatureOfInterestPrefix())) {
			setFeatureOfInterestPrefix((String) value);
		}
	}

	/**
	 * @param value
	 *            set featureIdentPropertyName value
	 */
	protected void applyFeatureIdentPropertyNameDefaultValue(Object value) {
		if (value instanceof String && StringUtils.isEmpty(getFeatureIdentPropertyName())) {
			setFeatureIdentPropertyName((String) value);
		}
	}

	/**
	 * @param value
	 *            set featureTitlePropertyName value
	 */
	protected void applyFeatureTitlePropertyNameDefaultValue(Object value) {
		if (value instanceof String && StringUtils.isEmpty(getFeatureTitlePropertyName())) {
			setFeatureTitlePropertyName((String) value);
		}
	}

	/**
	 * @param value
	 *            set phenomenonTimePropertyName value
	 */
	protected void applyPhenomenonTimePropertyNameDefaultValue(Object value) {
		if (value instanceof String && StringUtils.isEmpty(getPhenomenonTimePropertyName())) {
			setPhenomenonTimePropertyName((String) value);
		}
	}

	/**
	 * @param value
	 *            set createNullValueMessages value
	 */
	protected void applyCreateNullValueMessagesDefaultValue(Object value) {
		if (value != null //
				&& (boolean.class.equals(value.getClass()) || value instanceof Boolean)
				&& isCreateNullValueMessages() == null) {
			setCreateNullValueMessages((boolean) value);
		}
	}
}
