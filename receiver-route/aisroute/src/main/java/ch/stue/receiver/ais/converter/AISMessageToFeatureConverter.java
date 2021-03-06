package ch.stue.receiver.ais.converter;

import java.util.Date;
import java.util.Map;

import org.apache.camel.Converter;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.lang3.StringUtils;

import cern.colt.bitvector.BitVector;
import ch.stue.domain.GeoJSONFeature;
import ch.stue.domain.PointGeometry;
import ch.stue.receiver.ais.domain.AISPositionMessage;
import nl.esi.metis.aisparser.AISMessage;
import nl.esi.metis.aisparser.AISMessage05;
import nl.esi.metis.aisparser.AISMessagePositionReport;
import nl.esi.metis.aisparser.PositionInfo;
import nl.esi.metis.aisparser.UtilsDimensions30;
import nl.esi.metis.aisparser.UtilsEta;

/**
 * Converts a {@link AISMessage} to a {@link GeoJSONFeature}
 *
 * @author stue
 */
@Converter
public class AISMessageToFeatureConverter {

	/**
	 * Converts the given {@link AISMessage} into a {@link AISPositionMessage}
	 *
	 * @param aisMessage
	 *            the {@link AISMessage}
	 * @return the {@link GeoJSONFeature} object
	 */
	@Converter
	public AISPositionMessage convert(AISMessage aisMessage) {
		BidiMap<String, Object> properties = getProperties(aisMessage);

		PointGeometry pointGeometry;
		if (aisMessage instanceof PositionInfo) {
			PositionInfo positionInfo = (PositionInfo) aisMessage;
			pointGeometry = new PointGeometry(positionInfo.getLongitudeInDegrees(), positionInfo.getLatitudeInDegrees());
		} else {
			pointGeometry = null;
		}
		return new AISPositionMessage(pointGeometry, properties);
	}

	/**
	 * Extracts all the values from the sbs1Message into a String to Object Map
	 *
	 * @param sbs1Message
	 *            the message
	 * @return a map which contains property name to value entries
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static BidiMap<String, Object> getProperties(AISMessage message) {
		BidiMap<String, Object> propertiesMap = new DualHashBidiMap();
		if (message instanceof AISMessage) {
			AISMessage aisMessage = message;
			propertiesMap.put(AISPositionMessage.MESSAGE_ID_PROPERTY_NAME, aisMessage.getMessageID());
			propertiesMap.put(AISPositionMessage.REPEAT_INDICATOR_PROPERTY_NAME, aisMessage.getRepeatIndicator());
			propertiesMap.put(AISPositionMessage.USER_ID_PROPERTY_NAME, aisMessage.getUserID());
			propertiesMap.put(AISPositionMessage.USER_ID_STRING_PROPERTY_NAME, Integer.toString(aisMessage.getUserID()));
			propertiesMap.put(AISPositionMessage.TIME_STAMP_PROPERTY_NAME, new Date());
		}
		if (message instanceof PositionInfo) {
			PositionInfo positionInfo = (PositionInfo) message;
			propertiesMap.put(AISPositionMessage.POSITION_ACCURACY_PROPERTY_NAME, positionInfo.getPositionAccuracy());
			propertiesMap.put(AISPositionMessage.LONGITUDE_PROPERTY_NAME, positionInfo.getLongitudeInDegrees());
			propertiesMap.put(AISPositionMessage.LATITUDE_PROPERTY_NAME, positionInfo.getLatitudeInDegrees());
		}
		if (message instanceof AISMessagePositionReport) {
			AISMessagePositionReport aisMessagePositionReport = (AISMessagePositionReport) message;
			propertiesMap.put(AISPositionMessage.NAVIGATIONAL_STATUS_PROPERTY_NAME, aisMessagePositionReport.getNavigationalStatus());
			propertiesMap.put(AISPositionMessage.RATE_OF_TURN_PROPERTY_NAME, aisMessagePositionReport.getRateOfTurn());
			propertiesMap.put(AISPositionMessage.SPEED_OVER_GROUND_PROPERTY_NAME, aisMessagePositionReport.getSpeedOverGround() > 1022 ? null : aisMessagePositionReport.getSpeedOverGround()/10.0);
			propertiesMap.put(AISPositionMessage.COURSE_OVER_GROUND_PROPERTY_NAME, aisMessagePositionReport.getCourseOverGround() > 3600 ? null : aisMessagePositionReport.getCourseOverGround()/10.0);
			propertiesMap.put(AISPositionMessage.TRUE_HEADING_PROPERTY_NAME, aisMessagePositionReport.getTrueHeading() > 360 ? null : aisMessagePositionReport.getTrueHeading());
			propertiesMap.put(AISPositionMessage.TIME_STAMP_PROPERTY_NAME, aisMessagePositionReport.getTimeStamp() > 63 ? new Date(aisMessagePositionReport.getTimeStamp()) : new Date());

			propertiesMap.put(AISPositionMessage.SPECIAL_MANOEUVRE_PROPERTY_NAME, aisMessagePositionReport.getSpecialManoeuvre() > 0 ? (aisMessagePositionReport.getSpecialManoeuvre() == 2) : null);
			propertiesMap.put(AISPositionMessage.SPARE_PROPERTY_NAME, aisMessagePositionReport.getSpare());
			propertiesMap.put(AISPositionMessage.RAIM_FLAG_PROPERTY_NAME, aisMessagePositionReport.getRaimFlag());
		}
		if(message instanceof AISMessage05){
			AISMessage05 aisMessage05 = (AISMessage05) message;
			propertiesMap.put(AISPositionMessage.AIS_VERSION_INDICATOR, aisMessage05.getAisVersionIndicator());
			propertiesMap.put(AISPositionMessage.IMO_NUMBER, aisMessage05.getImoNumber());
			propertiesMap.put(AISPositionMessage.CALL_SIGN, aisMessage05.getCallSign());
			propertiesMap.put(AISPositionMessage.NAME, aisMessage05.getName());
			propertiesMap.put(AISPositionMessage.SHIP_AND_CARGO_TYPE, aisMessage05.getTypeOfShipAndCargoType());

			BitVector dimension = aisMessage05.getDimension();
			propertiesMap.put(AISPositionMessage.DIMENSION_LENGTH, UtilsDimensions30.getBow(dimension) + UtilsDimensions30.getStern(dimension));
			propertiesMap.put(AISPositionMessage.DIMENSION_WIDTH, UtilsDimensions30.getPort(dimension) + UtilsDimensions30.getStarboard(dimension));

			propertiesMap.put(AISPositionMessage.ELECTRONIC_POSITION_FIXING_DEVICE_TYPE, aisMessage05.getTypeOfElectronicPositionFixingDevice());
			propertiesMap.put(AISPositionMessage.ETA, UtilsEta.convertToTime(aisMessage05.getEta(), new Date().getTime()));
			propertiesMap.put(AISPositionMessage.MAX_PRESENT_STATIC_DRAUGHT, aisMessage05.getMaximumPresentStaticDraught());
			propertiesMap.put(AISPositionMessage.DESTINATION, StringUtils.isEmpty(aisMessage05.getDestination()) ? null : aisMessage05.getDestination());
			propertiesMap.put(AISPositionMessage.DTE, aisMessage05.getDte());
			propertiesMap.put(AISPositionMessage.SPARE, aisMessage05.getSpare());
		}
		return propertiesMap.size() > 0 ? propertiesMap : new DualHashBidiMap<String, Object>((Map) new BeanMap(message));
	}
}