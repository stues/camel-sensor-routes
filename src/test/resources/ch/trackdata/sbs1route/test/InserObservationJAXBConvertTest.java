package ch.trackdata.sbs1route.test;

import java.io.StringWriter;
import java.util.Arrays;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import net.opengis.sos.v_2_0_0.InsertObservationType;
import ch.trackdata.sbs1route.converter.insertobservation.InsertObservationSOSV2Configuration;
import ch.trackdata.sbs1route.converter.insertobservation.MeasurementPropertyConfiguration;
import ch.trackdata.sbs1route.converter.insertobservation.PointGeometryPropertyConfiguration;
import ch.trackdata.sbs1route.message.TrackPositionMessage;

public class InserObservationJAXBConvertTest {

	private static final String FEATURE_OF_INTEREST_HREF_PREFIX = "http://stue.ch/sensorobservation/foi/aircraft/";
	
	@SuppressWarnings("unchecked")
	public static InsertObservationSOSV2Configuration getInsertObservationConfiguration() {
		MeasurementPropertyConfiguration speedPropertyConfiguration = new MeasurementPropertyConfiguration();
		speedPropertyConfiguration.setObservationName("speedObservation");
		speedPropertyConfiguration.setProcedure("http://stue.ch/sensorobservation/procedure/flighttracking");
		speedPropertyConfiguration.setObservedProperty("http://stue.ch/sensorobservation/observableProperty/speed");
		speedPropertyConfiguration.setFeatureOfInterestPrefix(FEATURE_OF_INTEREST_HREF_PREFIX);
		speedPropertyConfiguration.setFeatureIdentPropertyName(TrackPositionMessage.CALLSIGN_PROPERTY_NAME);
		speedPropertyConfiguration.setMeasureUom("urn:x-ogc:def:property:OGC::Speed");
		speedPropertyConfiguration.setFeatureMeasurementPropertyName(TrackPositionMessage.GROUND_SPEED_NAME);
		speedPropertyConfiguration.setFeatureMeasurementPropertyClazz(Integer.class);

		MeasurementPropertyConfiguration altitudePropertyConfiguration = new MeasurementPropertyConfiguration();
		altitudePropertyConfiguration.setObservationName("altitudeObservation");
		altitudePropertyConfiguration.setProcedure("http://stue.ch/sensorobservation/procedure/flighttracking");
		altitudePropertyConfiguration.setObservedProperty("http://stue.ch/sensorobservation/observableProperty/altitude");
		altitudePropertyConfiguration.setFeatureOfInterestPrefix(FEATURE_OF_INTEREST_HREF_PREFIX);
		altitudePropertyConfiguration.setFeatureIdentPropertyName(TrackPositionMessage.CALLSIGN_PROPERTY_NAME);
		altitudePropertyConfiguration.setMeasureUom("urn:x-ogc:def:property:OGC::Altitude");
		altitudePropertyConfiguration.setFeatureMeasurementPropertyName(TrackPositionMessage.ALTITUDE_PROPERTY_NAME);
		altitudePropertyConfiguration.setFeatureMeasurementPropertyClazz(Integer.class);

		MeasurementPropertyConfiguration headingPropertyConfiguration = new MeasurementPropertyConfiguration();
		headingPropertyConfiguration.setObservationName("headingObservation");
		headingPropertyConfiguration.setProcedure("http://stue.ch/sensorobservation/procedure/flighttracking");
		headingPropertyConfiguration.setObservedProperty("http://stue.ch/sensorobservation/observableProperty/heading");
		headingPropertyConfiguration.setFeatureOfInterestPrefix(FEATURE_OF_INTEREST_HREF_PREFIX);
		headingPropertyConfiguration.setFeatureIdentPropertyName(TrackPositionMessage.CALLSIGN_PROPERTY_NAME);
		headingPropertyConfiguration.setMeasureUom("urn:x-ogc:def:property:OGC::Heading");
		headingPropertyConfiguration.setFeatureMeasurementPropertyName(TrackPositionMessage.TRACK_PROPERTY_NAME);
		headingPropertyConfiguration.setFeatureMeasurementPropertyClazz(Integer.class);

		PointGeometryPropertyConfiguration positionPropertyConfiguration = new PointGeometryPropertyConfiguration();
		positionPropertyConfiguration.setObservationName("positionObservation");
		positionPropertyConfiguration.setProcedure("http://stue.ch/sensorobservation/procedure/flighttracking");
		positionPropertyConfiguration.setObservedProperty("http://stue.ch/sensorobservation/observableProperty/position");
		positionPropertyConfiguration.setFeatureOfInterestPrefix(FEATURE_OF_INTEREST_HREF_PREFIX);
		positionPropertyConfiguration.setFeatureIdentPropertyName(TrackPositionMessage.CALLSIGN_PROPERTY_NAME);
		positionPropertyConfiguration.setUseFeatureGeometry(true);
		positionPropertyConfiguration.setSrsName("http://www.opengis.net/def/crs/EPSG/0/4326");
		positionPropertyConfiguration.setPointId("aircraftPosition");

		InsertObservationSOSV2Configuration configuration = new InsertObservationSOSV2Configuration();
		configuration.setOfferings(Arrays.asList("http://stue.ch/sensorobservation/offering/ads-b"));
		configuration.setObservedProperties(Arrays.asList(speedPropertyConfiguration, altitudePropertyConfiguration, headingPropertyConfiguration, positionPropertyConfiguration));
		return configuration;
	}
	
	public static void main(String[] args) {
		TrackPositionMessage trackPositionMessage = new TrackPositionMessage();
		trackPositionMessage.setCallSign("SWR935");
		trackPositionMessage.setGroundSpeed(350);
		trackPositionMessage.setAltitude(40000);
		trackPositionMessage.setTrack(25);
		trackPositionMessage.setGeometry(7.5, 48.0);
		
		InsertObservationSOSV2Configuration configuration = getInsertObservationConfiguration();
		
		JAXBElement<InsertObservationType> insertObservation = getInsertObservation(configuration,
				trackPositionMessage);
		JAXBElement<InsertObservationType> insertObservation = SOS_OBJECT_FACTORY.createInsertObservation(insertObservationType);
		printJAXBElement(insertObservation);
	}
	
	private static void printJAXBElement(
			JAXBElement<InsertObservationType> insertObservation) {
		StringWriter writer = new StringWriter();
		try {

			JAXBContext context = JAXBContext
					.newInstance("net.opengis.sos.v_2_0_0:net.opengis.swe.v_2_0_0:net.opengis.filter.v_2_0_0");
			Marshaller m = context.createMarshaller();
			m.marshal(insertObservation, writer);

			// output string to console
			String theXML = writer.toString();
			System.out.println(theXML);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
