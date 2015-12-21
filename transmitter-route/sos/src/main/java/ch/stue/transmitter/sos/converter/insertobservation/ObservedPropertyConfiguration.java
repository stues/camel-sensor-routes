package ch.stue.transmitter.sos.converter.insertobservation;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ch.stue.domain.Feature;

/**
 * A interface which can be used to describe a Observation including a
 * ObservedProperty
 *
 * @author stue
 *
 * @param <T>
 *            the Type of Object which will be added as result
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = AbstractPropertyConfiguration.TYPE_PROPERTY_NAME)
@JsonSubTypes({ //
	@JsonSubTypes.Type(value = PointGeometryPropertyConfiguration.class, name = PointGeometryPropertyConfiguration.POINT_GEOMETRY_TYPE_STRING), //
	@JsonSubTypes.Type(value = MeasurementPropertyConfiguration.class, name = MeasurementPropertyConfiguration.MEASUREMENT_TYPE_STRING), //
	@JsonSubTypes.Type(value = TruthPropertyConfiguration.class, name = TruthPropertyConfiguration.TRUTH_TYPE_STRING), //
	@JsonSubTypes.Type(value = TextPropertyConfiguration.class, name = TextPropertyConfiguration.TEXT_TYPE_STRING) //
})
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
	 * @return the featureTitlePropertyName
	 */
	public abstract String getFeatureTitlePropertyName();

	/**
	 * @return the createNullValueMessages
	 */
	public abstract Boolean isCreateNullValueMessages();

	/**
	 * @return the observed value
	 */
	public abstract T getValue(Feature<?> feature);

	/**
	 * Does update this observed propertyConfiguration with default values the
	 * default values will be used for all properties which have not been
	 * defined
	 *
	 * @param defaultValues
	 *            the default values to set
	 */
	public abstract void setDefaultValues(Map<String, Object> defaultValues);
}