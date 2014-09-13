package ch.trackdata.sbs1route.converter;

import javax.xml.bind.JAXBElement;

import net.opengis.sos.v_2_0_0.InsertObservationType;

import org.apache.camel.BeanInject;
import org.apache.camel.Converter;

import ch.trackdata.sbs1route.converter.insertobservation.InserObservationJAXBHelper;
import ch.trackdata.sbs1route.converter.insertobservation.InsertObservationSOSV2Configuration;
import ch.trackdata.sbs1route.message.GeoJSONFeature;

/**
 * Converts a {@link GeoJSONFeature} to a {@link JAXBElement} which represents a
 * Insert Observation
 * 
 * @author stue
 */

@Converter
public class GeoJSONToInsertObservationConverter {

	@BeanInject("insertObservationConfiguration")
	private InsertObservationSOSV2Configuration configuration;

	/**
	 * converts the {@link GeoJSONFeature} into a {@link JAXBElement}
	 * 
	 * @param feature
	 * @return
	 */
	@Converter
	public JAXBElement<InsertObservationType> convert(GeoJSONFeature feature) {
		JAXBElement<InsertObservationType> insertObservation = InserObservationJAXBHelper.getInsertObservation(configuration, feature);
		return insertObservation;
	}
}
