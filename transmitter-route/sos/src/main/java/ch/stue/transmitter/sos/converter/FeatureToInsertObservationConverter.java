package ch.stue.transmitter.sos.converter;

import javax.xml.bind.JAXBElement;

import org.apache.camel.BeanInject;
import org.apache.camel.Converter;
import org.apache.camel.RuntimeCamelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.stue.domain.Feature;
import ch.stue.transmitter.sos.converter.insertobservation.InsertObservationJAXBHelper;
import ch.stue.transmitter.sos.converter.insertobservation.InsertObservationSOSV2Configuration;
import net.opengis.sos.v_2_0_0.InsertObservationType;

/**
 * Converts a {@link Feature} to a {@link JAXBElement} which represents a
 * Insert Observation
 *
 * @author stue
 */
@Converter
public class FeatureToInsertObservationConverter {

	private static final Logger LOGGER = LoggerFactory.getLogger(FeatureToInsertObservationConverter.class);

	@BeanInject("sosConfiguration")
	private SOSConfiguration sosConfiguration;

	/**
	 * converts the {@link Feature} into a {@link JAXBElement}
	 *
	 * @param feature
	 * @return
	 */
	@Converter(allowNull = true)
	public JAXBElement<InsertObservationType> convert(Feature<?> feature) {
		InsertObservationSOSV2Configuration configuration = this.sosConfiguration.getConfiguration(feature);
		if(configuration != null){
			return InsertObservationJAXBHelper.getInsertObservation(configuration, feature);
		}
		else{
			String warnMessage = String.format("No SOS configuration has been found for the Feature: %s", feature);
			LOGGER.warn(warnMessage);
			throw new RuntimeCamelException(warnMessage);
		}
	}
}
