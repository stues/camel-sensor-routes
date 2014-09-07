package ch.trackdata.sbs1route.converter;

import javax.xml.bind.JAXBElement;

import net.opengis.sos.v_2_0_0.InsertObservationType;

import org.apache.camel.Converter;

import ch.trackdata.sbs1route.converter.insertobservation.InserObservationJAXBHelper;
import ch.trackdata.sbs1route.converter.insertobservation.InsertObservationSOSV2Configuration;
import ch.trackdata.sbs1route.message.GeoJSONFeature;
import ch.trackdata.sbs1route.message.SBS1Message;

/**
 * Converts a {@link SBS1Message} to a {@link GeoJSONFeature}
 * 
 * @author stue
 */

@Converter
public class SBS1ToGeoJSONConverter {

	private InsertObservationSOSV2Configuration configuration;
	
	public SBS1ToGeoJSONConverter(){
		configuration = InserObservationJAXBHelper.getInsertObservationConfiguration();
	}
	
	@Converter
	public JAXBElement<InsertObservationType> convert(GeoJSONFeature feature) {
		JAXBElement<InsertObservationType> insertObservation = InserObservationJAXBHelper.getInsertObservation(configuration, feature);
		return insertObservation;
	}
}
