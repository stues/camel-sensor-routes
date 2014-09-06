package ch.trackdata.sbs1route.converter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

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
import ch.trackdata.sbs1route.message.GeoJSONFeature;
import ch.trackdata.sbs1route.message.PointGeometry;
import ch.trackdata.sbs1route.message.TrackPositionMessage;

public class GeoTKSensoradapter {
	
	private static final String FEATURE_OF_INTEREST_HREF_PREFIX = "http://stue.ch/sensorobservation/foi/aircraft/";

	private static final String SERVICE_NAME = "SOS";

	private static final String SOS_VERSION = "2.0.0";

	private static final DateFormat GML_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	
	private static final net.opengis.om.v_2_0_0.ObjectFactory OM_OBJECT_FACTORY = new net.opengis.om.v_2_0_0.ObjectFactory();
	private static final net.opengis.gml.v_3_2_1.ObjectFactory GML_OBJECT_FACTORY = new net.opengis.gml.v_3_2_1.ObjectFactory();
	private static final net.opengis.sos.v_2_0_0.ObjectFactory SOS_OBJECT_FACTORY = new net.opengis.sos.v_2_0_0.ObjectFactory();
	
	public static void main(String[] args) {
		TrackPositionMessage trackPositionMessage = new TrackPositionMessage();
		trackPositionMessage.setCallSign("SWR935");
		trackPositionMessage.setGroundSpeed(350);
		trackPositionMessage.setAltitude(40000);
		trackPositionMessage.setTrack(25);
		trackPositionMessage.setGeometry(7.5, 48.0);
		
		JAXBElement<InsertObservationType> insertObservation = getInsertObservation(
				"http://stue.ch/sensorobservation/offering/ads-b",
				"http://stue.ch/sensorobservation/procedure/flighttracking",
				trackPositionMessage);

		printJAXBElement(insertObservation);
	}

	private static void printJAXBElement(
			JAXBElement<InsertObservationType> insertObservation) {
		StringWriter writer = new StringWriter();
		try {
			
			JAXBContext context = JAXBContext.newInstance("net.opengis.sos.v_2_0_0:net.opengis.swe.v_2_0_0:net.opengis.filter.v_2_0_0");
			Marshaller m = context.createMarshaller();
			m.marshal(insertObservation, writer);

			// output string to console
			String theXML = writer.toString();
			System.out.println(theXML);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	private static JAXBElement<InsertObservationType> getInsertObservation(String offering,
			String procedure,
			GeoJSONFeature geoJSONFeature) {
		
		Observation speedObservation = getMeasurementObservation("speedObservation",
				procedure, "http://stue.ch/sensorobservation/observableProperty/speed", 
				"urn:x-ogc:def:property:OGC::Speed", 
				geoJSONFeature, 
				TrackPositionMessage.CALLSIGN_PROPERTY_NAME, 
				TrackPositionMessage.GROUND_SPEED_NAME,
				Integer.class,
				true);
		
		Observation altitudeObservation = getMeasurementObservation("altitudeObservation",
				procedure, 
				"http://stue.ch/sensorobservation/observableProperty/altitude", 
				"urn:x-ogc:def:property:OGC::Altitude", 
				geoJSONFeature, 
				TrackPositionMessage.CALLSIGN_PROPERTY_NAME, 
				TrackPositionMessage.ALTITUDE_PROPERTY_NAME, 
				Integer.class, 
				false);
		
		Observation headingObservation = getMeasurementObservation("headingObservation",
				procedure, 
				"http://stue.ch/sensorobservation/observableProperty/heading", 
				"urn:x-ogc:def:property:OGC::Heading", 
				geoJSONFeature, 
				TrackPositionMessage.CALLSIGN_PROPERTY_NAME, 
				TrackPositionMessage.TRACK_PROPERTY_NAME, 
				Integer.class, 
				false);
		
		Observation geometryObservation = getGeometryObservation("positionObservation",
				procedure, 
				"http://stue.ch/sensorobservation/observableProperty/position", 
				"urn:x-ogc:def:property:OGC::Position", 
				geoJSONFeature, 
				TrackPositionMessage.CALLSIGN_PROPERTY_NAME, 
				false);
		
		InsertObservationType insertObservationType = SOS_OBJECT_FACTORY.createInsertObservationType();
		insertObservationType.setService(SERVICE_NAME);
		insertObservationType.setVersion(SOS_VERSION);
		insertObservationType.setOffering((List<String>)Arrays.asList(offering));
		insertObservationType.setObservation((List<Observation>)Arrays.asList(speedObservation, altitudeObservation, headingObservation, geometryObservation));
		JAXBElement<InsertObservationType> insertObservation = SOS_OBJECT_FACTORY.createInsertObservation(insertObservationType);
		
		return insertObservation;
	}

	private static Observation getMeasurementObservation(String observationName, 
			String procedure,
			String observedProperty, String measureUom, GeoJSONFeature feature, String featureIdentPropertyName, String featureMeasurementPropertyName, Class<? extends Number> featureMeasurementPropertyClazz, boolean firstEntry) {
		OMObservationType omObservation = new OMObservationType();
		createObservationNoResult(observationName, procedure, observedProperty,
				feature, featureIdentPropertyName, firstEntry, omObservation);
		
		
		MeasureType measureType = GML_OBJECT_FACTORY.createMeasureType();
		measureType.setUom(measureUom);
		Number value = feature.getProperty(featureMeasurementPropertyName, featureMeasurementPropertyClazz);
		if(value != null){
			measureType.setValue(value.doubleValue());
			omObservation.setResult(measureType);
		}
		
		Observation observation = SOS_OBJECT_FACTORY.createInsertObservationTypeObservation();
		observation.setOMObservation(omObservation);
		return observation;
	}

	@SuppressWarnings("unchecked")
	private static Observation getGeometryObservation(String observationName, 
			String procedure,
			String observedProperty, String measureUom, GeoJSONFeature feature, String featureIdentPropertyName, boolean firstEntry) {
		OMObservationType omObservation = new OMObservationType();
		createObservationNoResult(observationName, procedure, observedProperty,
				feature, featureIdentPropertyName, firstEntry, omObservation);
		
		

		if(feature.getGeometry() instanceof PointGeometry){
			DirectPositionType positionType = GML_OBJECT_FACTORY.createDirectPositionType();
			positionType.setSrsName("http://www.opengis.net/def/crs/EPSG/0/4326");
			positionType.setValue((List<Double>)feature.getGeometry().getCoordinates());
			
			PointType pointType = GML_OBJECT_FACTORY.createPointType();
			pointType.setId("aircraftPosition");
			pointType.setPos(positionType);
			
			GeometryPropertyType geometryPropertyType = GML_OBJECT_FACTORY.createGeometryPropertyType();
			geometryPropertyType.setAbstractGeometry(GML_OBJECT_FACTORY.createPoint(pointType));
			omObservation.setResult(geometryPropertyType);
			
		}
		
		Observation observation = SOS_OBJECT_FACTORY.createInsertObservationTypeObservation();
		observation.setOMObservation(omObservation);
		return observation;
	}
	
	private static void createObservationNoResult(String observationName,
			String procedure, String observedProperty, GeoJSONFeature feature,
			String featureIdentPropertyName, boolean firstEntry,
			OMObservationType omObservation) {
		omObservation.setId(observationName);
		if(firstEntry){
			omObservation.setPhenomenonTime(getCurrentTimeObjectProperty("phenomenTime"));
		}
		else{
			omObservation.setPhenomenonTime(getPhenomenTimeObjectPropertyHref("#phenomenTime"));	
		}
		omObservation.setResultTime(getResultTimeObjectPropertyHref("#phenomenTime"));
		omObservation.setProcedure(getProcedure(procedure));
		omObservation.setObservedProperty(getObservedPropertyType(observedProperty));
		omObservation.setFeatureOfInterest(getFeatureOfInterestType(feature,featureIdentPropertyName));
	}

	private static OMProcessPropertyType getProcedure(String procedure) {
		OMProcessPropertyType procedureType = OM_OBJECT_FACTORY.createOMProcessPropertyType();
		procedureType.setHref(procedure);
		return procedureType;
	}

	private static FeaturePropertyType getFeatureOfInterestType(GeoJSONFeature feature, String propertyName) {
		FeaturePropertyType featurePropertyType = GML_OBJECT_FACTORY.createFeaturePropertyType();
		String identifier = feature.getProperty(propertyName, String.class);
		featurePropertyType.setHref(FEATURE_OF_INTEREST_HREF_PREFIX + identifier);
		featurePropertyType.setTitle(identifier);
		return featurePropertyType;
	}

	private static ReferenceType getObservedPropertyType(String observedProperty) {
		ReferenceType observedPropertyReferenceType = GML_OBJECT_FACTORY.createReferenceType();
		observedPropertyReferenceType.setHref(observedProperty);
		return observedPropertyReferenceType;
	}

	/**
	 * Returns a TimeObjectPropertyType
	 * @param gmlId
	 * @return
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
	 * Returns a TimeObjectPropertyType
	 * @param gmlId
	 * @return
	 */
	private static TimeObjectPropertyType getPhenomenTimeObjectPropertyHref(String hrefId) {
		TimeObjectPropertyType timeObjectProperty = OM_OBJECT_FACTORY.createTimeObjectPropertyType();
		timeObjectProperty.setHref(hrefId);
		return timeObjectProperty;
	}
	
	private static TimeInstantPropertyType getResultTimeObjectPropertyHref(String hrefId) {
		TimeInstantPropertyType timeObjectProperty = GML_OBJECT_FACTORY.createTimeInstantPropertyType();
		timeObjectProperty.setHref(hrefId);
		return timeObjectProperty;
	}
}
