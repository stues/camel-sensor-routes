package ch.trackdata.sbs1route.converter.insertobservation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import net.opengis.gml.v_3_2_1.DirectPositionType;
import net.opengis.gml.v_3_2_1.FeaturePropertyType;
import net.opengis.gml.v_3_2_1.GeometryPropertyType;
import net.opengis.gml.v_3_2_1.MeasureType;
import net.opengis.gml.v_3_2_1.PointType;
import net.opengis.gml.v_3_2_1.ReferenceType;
import net.opengis.gml.v_3_2_1.TimeInstantPropertyType;
import net.opengis.gml.v_3_2_1.TimeInstantType;
import net.opengis.gml.v_3_2_1.TimePositionType;
import net.opengis.om.v_2_0_0.OMObservationType;
import net.opengis.om.v_2_0_0.OMProcessPropertyType;
import net.opengis.om.v_2_0_0.TimeObjectPropertyType;
import net.opengis.sos.v_2_0_0.InsertObservationType;
import net.opengis.sos.v_2_0_0.InsertObservationType.Observation;

import org.apache.commons.collections4.CollectionUtils;

import ch.trackdata.sbs1route.message.AbstractGeometry;
import ch.trackdata.sbs1route.message.GeoJSONFeature;

/**
 * This Helper class provides a Method {@link #getInsertObservation} to transform a given {@link geoJSONFeature}
 * into a {@link JAXBElement} which contains a {@link InsertObservationType}
 * 
 * @author stue
 *
 */
public class InserObservationJAXBHelper {

	private static final DateFormat GML_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	private static final net.opengis.om.v_2_0_0.ObjectFactory OM_OBJECT_FACTORY = new net.opengis.om.v_2_0_0.ObjectFactory();
	private static final net.opengis.gml.v_3_2_1.ObjectFactory GML_OBJECT_FACTORY = new net.opengis.gml.v_3_2_1.ObjectFactory();
	private static final net.opengis.sos.v_2_0_0.ObjectFactory SOS_OBJECT_FACTORY = new net.opengis.sos.v_2_0_0.ObjectFactory();
	
	/**
	 * Creates a new {@link InsertObservationType} as {@link JAXBElement} for the given feature with the given configuration
	 * 
	 * @param configuration the configuration
	 * @param geoJSONFeature the feature
	 * 
	 * @return the {@link JAXBElement} which contains a {@link InsertObservationType}
	 */
	@SuppressWarnings("unchecked")
	public static JAXBElement<InsertObservationType> getInsertObservation(InsertObservationSOSV2Configuration configuration, GeoJSONFeature<?> geoJSONFeature) {

		List<Observation> observations = new LinkedList<Observation>();
		boolean firstEntry = true;
		if (CollectionUtils.isNotEmpty(configuration.getObservedProperties())) {
			for (ObservedPropertyConfiguration<?> observedPropertyConfiguration : configuration.getObservedProperties()) {
				
				if(observedPropertyConfiguration.isCreateNullValueMessages() ||
						observedPropertyConfiguration.getValue(geoJSONFeature) != null){
					
					if (observedPropertyConfiguration instanceof MeasurementPropertyConfiguration) {
						observations.add(getMeasurementObservation((MeasurementPropertyConfiguration) observedPropertyConfiguration, geoJSONFeature, firstEntry));
					} else if (observedPropertyConfiguration instanceof AbstractGeometryPropertyConfiguration) {
						observations.add(getGeometryObservation((AbstractGeometryPropertyConfiguration<? extends AbstractGeometry<?>>) observedPropertyConfiguration, geoJSONFeature, firstEntry));
					} else if (observedPropertyConfiguration instanceof TextPropertyConfiguration) {
						observations.add(getTextObservation((TextPropertyConfiguration) observedPropertyConfiguration, geoJSONFeature, firstEntry));
					}
					firstEntry = false;
				}
			}
		}

		InsertObservationType insertObservationType = SOS_OBJECT_FACTORY.createInsertObservationType();
		insertObservationType.setService(configuration.getService());
		insertObservationType.setVersion(configuration.getVersion());
		insertObservationType.setOffering(configuration.getOfferings());
		insertObservationType.setObservation(observations);
		JAXBElement<InsertObservationType> insertObservation = SOS_OBJECT_FACTORY.createInsertObservation(insertObservationType);

		return insertObservation;
	}

	/**
	 * Creates a new TextObservation for the given PropertyConfiguration
	 * 
	 * @param propertyConfiguration the property configuration
	 * @param feature the GeoJSON feature
	 * @return the generated Geometry Observation
	 */
	private static Observation getTextObservation(TextPropertyConfiguration propertyConfiguration, GeoJSONFeature<?> feature, boolean firstEntry) {
		OMObservationType omObservation = new OMObservationType();
		createObservationNoResult(propertyConfiguration.getObservationName(), propertyConfiguration.getProcedure(), propertyConfiguration.getObservedProperty(), feature,
				propertyConfiguration.getFeatureOfInterestPrefix(), propertyConfiguration.getFeatureIdentPropertyName(), propertyConfiguration.getFeatureTitlePropertyName(),
				firstEntry, omObservation);

		String value = propertyConfiguration.getValue(feature);
		if (value != null) {
			omObservation.setResult(value);
		}

		Observation observation = SOS_OBJECT_FACTORY.createInsertObservationTypeObservation();
		observation.setOMObservation(omObservation);
		return observation;
	}
	
	/**
	 * Creates a new MeasurementObservation for the given PropertyConfiguration
	 * 
	 * @param propertyConfiguration the property configuration
	 * @param feature the GeoJSON feature
	 * @return the generated Geometry Observation
	 */
	private static Observation getMeasurementObservation(MeasurementPropertyConfiguration propertyConfiguration, GeoJSONFeature<?> feature, boolean firstEntry) {
		OMObservationType omObservation = new OMObservationType();
		createObservationNoResult(propertyConfiguration.getObservationName(), propertyConfiguration.getProcedure(), propertyConfiguration.getObservedProperty(), feature,
				propertyConfiguration.getFeatureOfInterestPrefix(), propertyConfiguration.getFeatureIdentPropertyName(), propertyConfiguration.getFeatureTitlePropertyName(),
				firstEntry, omObservation);

		MeasureType measureType = GML_OBJECT_FACTORY.createMeasureType();
		measureType.setUom(propertyConfiguration.getMeasureUom());
		Number value = propertyConfiguration.getValue(feature);
		if (value != null) {
			measureType.setValue(value.doubleValue());
			omObservation.setResult(measureType);
		}

		Observation observation = SOS_OBJECT_FACTORY.createInsertObservationTypeObservation();
		observation.setOMObservation(omObservation);
		return observation;
	}

	/**
	 * Creates a new GeometryObservation for the given PropertyConfiguration
	 * 
	 * @param propertyConfiguration the property configuration
	 * @param feature the GeoJSON feature
	 * @return the generated Geometry Observation
	 */
	private static Observation getGeometryObservation(AbstractGeometryPropertyConfiguration<? extends AbstractGeometry<?>> propertyConfiguration, GeoJSONFeature<?> feature, boolean firstEntry) {
		OMObservationType omObservation = new OMObservationType();
		createObservationNoResult(propertyConfiguration.getObservationName(), 
				propertyConfiguration.getProcedure(), 
				propertyConfiguration.getObservedProperty(), 
				feature,
				propertyConfiguration.getFeatureOfInterestPrefix(), 
				propertyConfiguration.getFeatureIdentPropertyName(), 
				propertyConfiguration.getFeatureTitlePropertyName(),
				firstEntry, 
				omObservation);

		if (propertyConfiguration instanceof PointGeometryPropertyConfiguration) {
			PointGeometryPropertyConfiguration pointPropertyConfiguration = (PointGeometryPropertyConfiguration) propertyConfiguration;
			DirectPositionType positionType = GML_OBJECT_FACTORY.createDirectPositionType();
			positionType.setSrsName(pointPropertyConfiguration.getSrsName());
			positionType.setValue(Arrays.asList((Double[])feature.getGeometry().getCoordinates()));

			PointType pointType = GML_OBJECT_FACTORY.createPointType();
			pointType.setId(pointPropertyConfiguration.getPointId());
			pointType.setPos(positionType);

			GeometryPropertyType geometryPropertyType = GML_OBJECT_FACTORY.createGeometryPropertyType();
			geometryPropertyType.setAbstractGeometry(GML_OBJECT_FACTORY.createPoint(pointType));
			omObservation.setResult(geometryPropertyType);

		}

		Observation observation = SOS_OBJECT_FACTORY.createInsertObservationTypeObservation();
		observation.setOMObservation(omObservation);
		return observation;
	}

	/**
	 * Fills the given omObservation instance with elements generated from the given parameters
	 * no results are added to the omObservation
	 * 
	 * @param observationName the name of this observation
	 * @param procedure the procedure
	 * @param observedProperty the observed property
	 * @param feature the GeoJSON feature
	 * @param featureOfInterestPrefix the prefix of the feature of interest
	 * @param featureIdentPropertyName the property name of the identifier of the feature of interest
	 * @param featureTitlePropertyName the property name of the title of the feature of interest
	 * @param firstEntry whether this is the first entry or not (important for references)
	 * @param omObservation the object to fill
	 */
	private static void createObservationNoResult(String observationName, String procedure, String observedProperty, GeoJSONFeature<?> feature, String featureOfInterestPrefix,
			String featureIdentPropertyName, String featureTitlePropertyName, boolean firstEntry, OMObservationType omObservation) {
		omObservation.setId(observationName);
		if (firstEntry) {
			omObservation.setPhenomenonTime(getCurrentTimeObjectProperty("phenomenTime"));
		} else {
			omObservation.setPhenomenonTime(getPhenomenTimeObjectPropertyHref("#phenomenTime"));
		}
		omObservation.setResultTime(getResultTimeObjectPropertyHref("#phenomenTime"));
		omObservation.setProcedure(getProcedure(procedure));
		omObservation.setObservedProperty(getObservedPropertyType(observedProperty));
		omObservation.setFeatureOfInterest(getFeatureOfInterestType(feature, featureOfInterestPrefix, featureIdentPropertyName, featureTitlePropertyName));
	}

	/**
	 * Returns the OM process procedure for the given procedure
	 * @param procedure the procedure string
	 * @return the generated {@link OMProcessPropertyType}
	 */
	private static OMProcessPropertyType getProcedure(String procedure) {
		OMProcessPropertyType procedureType = OM_OBJECT_FACTORY.createOMProcessPropertyType();
		procedureType.setHref(procedure);
		return procedureType;
	}

	/**
	 * Returns the feature of interest for the given Parameters
	 * 
	 * @param feature the feature
	 * @param featureOfInterestPrefix the prefix of the feature of interest
	 * @param featureIdentPropertyName the property name of the identifier of the feature of interest
	 * @param featureTitlePropertyName the property name of the title of the feature of interest
	 * @return the created {@link FeaturePropertyType} instance
	 */
	private static FeaturePropertyType getFeatureOfInterestType(GeoJSONFeature<?> feature, 
			String featureOfInterestPrefix, 
			String featureIdentPropertyName, 
			String featureTitlePropertyName) {
		FeaturePropertyType featurePropertyType = GML_OBJECT_FACTORY.createFeaturePropertyType();
		String identifier = feature.getProperty(featureIdentPropertyName, String.class);
		featurePropertyType.setHref(featureOfInterestPrefix + identifier);

		String title = feature.getProperty(featureTitlePropertyName, String.class);
		featurePropertyType.setTitle(title);
		return featurePropertyType;
	}

	private static ReferenceType getObservedPropertyType(String observedProperty) {
		ReferenceType observedPropertyReferenceType = GML_OBJECT_FACTORY.createReferenceType();
		observedPropertyReferenceType.setHref(observedProperty);
		return observedPropertyReferenceType;
	}

	/**
	 * Returns a TimeObjectPropertyType for the given gmlId
	 * 
	 * @param gmlId the gmlId
	 * @return the created {@link TimeObjectPropertyType} instance
	 */
	private static TimeObjectPropertyType getCurrentTimeObjectProperty(String gmlId) {
		TimePositionType timePosition = GML_OBJECT_FACTORY.createTimePositionType();
		timePosition.setValue(Arrays.asList(GML_DATE_FORMAT.format(new Date())));

		TimeInstantType timeInstantType = GML_OBJECT_FACTORY.createTimeInstantType();
		timeInstantType.setTimePosition(timePosition);
		timeInstantType.setId(gmlId);

		TimeObjectPropertyType timeObjectProperty = OM_OBJECT_FACTORY.createTimeObjectPropertyType();
		timeObjectProperty.setAbstractTimeObject(GML_OBJECT_FACTORY.createTimeInstant(timeInstantType));

		return timeObjectProperty;
	}

	/**
	 * Returns a pheonmentTypeObject which references the given hrefId
	 * 
	 * @param hrefId the hrefId string
	 * @return the created {@link TimeObjectPropertyType}
	 */
	private static TimeObjectPropertyType getPhenomenTimeObjectPropertyHref(String hrefId) {
		TimeObjectPropertyType timeObjectProperty = OM_OBJECT_FACTORY.createTimeObjectPropertyType();
		timeObjectProperty.setHref(hrefId);
		return timeObjectProperty;
	}

	/**
	 * Returns a resultTimeObject which references the given hrefId
	 * 
	 * @param hrefId the hrefId string
	 * @return the created {@link TimeInstantPropertyType} instance
	 */
	private static TimeInstantPropertyType getResultTimeObjectPropertyHref(String hrefId) {
		TimeInstantPropertyType timeObjectProperty = GML_OBJECT_FACTORY.createTimeInstantPropertyType();
		timeObjectProperty.setHref(hrefId);
		return timeObjectProperty;
	}
}
