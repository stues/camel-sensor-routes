package ch.trackdata.sbs1route.test;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import net.opengis.sos.v_2_0_0.InsertObservationType;
import ch.trackdata.sbs1route.converter.insertobservation.InsertObservationSOSV2Configuration;
import ch.trackdata.sbs1route.message.TrackPositionMessage;

public class InserObservationJAXBConvertTest {

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
