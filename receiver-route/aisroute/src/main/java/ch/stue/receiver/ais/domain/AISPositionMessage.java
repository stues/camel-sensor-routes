package ch.stue.receiver.ais.domain;

import java.util.HashMap;

import ch.stue.domain.GeoJSONFeature;
import ch.stue.domain.PointGeometry;

/**
 * Extends the GeoJSONFeature with some utility methods to instantiate a
 * {@link AISPositionMessage}
 * 
 * It also provides some getter and setter methods for the Mode-S Attributes
 * 
 * @author stue
 */
public class AISPositionMessage extends GeoJSONFeature<PointGeometry> {

	private static final long serialVersionUID = 1L;
	
	/*
	 * AIS MESSAGE 1,2,3 ATTRIBUTES 
	 */
	public static String NAVIGATIONAL_STATUS_PROPERTY_NAME = "navigationalStatus";
	public static String RATE_OF_TURN_PROPERTY_NAME = "rateOfTurn";
	public static String SPEED_OVER_GROUND_PROPERTY_NAME = "speedOverGround";
	public static String COURSE_OVER_GROUND_PROPERTY_NAME = "courseOverGround";
	public static String TRUE_HEADING_PROPERTY_NAME = "trueHeading";
	public static String TIME_STAMP_PROPERTY_NAME = "timeStamp";
	public static String SPECIAL_MANOEUVRE_PROPERTY_NAME = "specialManoeuvre";
	public static String SPARE_PROPERTY_NAME = "spareProperty";
	public static String RAIM_FLAG_PROPERTY_NAME = "raimFlag";
	public static String POSITION_ACCURACY_PROPERTY_NAME = "positionAccuracy";
	public static String LONGITUDE_PROPERTY_NAME = "longitude";
	public static String LATITUDE_PROPERTY_NAME = "latitude";
	public static String MESSAGE_ID_PROPERTY_NAME = "messageId";
	public static String REPEAT_INDICATOR_PROPERTY_NAME = "repeatIndicator";
	public static String USER_ID_PROPERTY_NAME = "userId";
	
	/*
	 * AIS MESSAGE 5 ATTRIBUTES 
	 */
	public static String AIS_VERSION_INDICATOR = "aisVersionIndicator";
	public static String IMO_NUMBER = "imoNumber";
	public static String CALL_SIGN = "callSign";
	public static String NAME = "name";
	public static String SHIP_AND_CARGO_TYPE = "typeOfShipAndCargoType";
	public static String DIMENSION_LENGTH = "dimensionLength";
	public static String DIMENSION_WIDTH = "dimensionWidth";
	
	public static String ELECTRONIC_POSITION_FIXING_DEVICE_TYPE = "typeOfElectronicPositionFixingDevice";
	public static String ETA = "eta";
	public static String MAX_PRESENT_STATIC_DRAUGHT = "maximumPresentStaticDraught";
	public static String DESTINATION = "destination";
	public static String DTE = "dte";
	public static String SPARE = "spare";
	
	/**
	 * Default Constructor
	 */
	public AISPositionMessage() {
		super(null, new HashMap<String, Object>());
	}	
}
