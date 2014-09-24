package ch.trackdata.sbs1route.message;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.time.DateUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TrackPositionMessage extends GeoJSONFeature {

	public static String HEX_PROPERTY_NAME = "hexIdent";
	public static String CALLSIGN_PROPERTY_NAME = "callsign";
	public static String ALTITUDE_PROPERTY_NAME = "altitude";
	public static String TRACK_PROPERTY_NAME = "track";
	public static String GROUND_SPEED_NAME = "groundSpeed";
	public static String IS_ON_GROUND_PROPERTY_NAME = "isOnGround";

	private static String DATE_TIME_MESSAGE_GENERATED_NAME = "messageGenerated";

	private Date messageReceived;

	public TrackPositionMessage() {
		super(null, new HashMap<String, Object>());
	}

	@JsonIgnore
	public String getHex() {
		return getProperty(String.class, HEX_PROPERTY_NAME);
	}

	public void setHex(String hex) {
		getProperties().put(HEX_PROPERTY_NAME, hex);
	}

	@JsonIgnore
	public String getCallSign() {
		return getProperty(String.class, CALLSIGN_PROPERTY_NAME);
	}

	public void setCallSign(String callSign) {
		getProperties().put(CALLSIGN_PROPERTY_NAME, callSign);
	}

	@JsonIgnore
	public Integer getTrack() {
		return getProperty(Integer.class, TRACK_PROPERTY_NAME);
	}

	public void setTrack(Integer track) {
		getProperties().put(TRACK_PROPERTY_NAME, track);
	}

	@JsonIgnore
	public Integer getAltitude() {
		return getProperty(Integer.class, ALTITUDE_PROPERTY_NAME);
	}

	public void setAltitude(Integer altitude) {
		getProperties().put(ALTITUDE_PROPERTY_NAME, altitude);
	}

	@JsonIgnore
	public Integer getGroundSpeed() {
		return getProperty(Integer.class, GROUND_SPEED_NAME);
	}

	public void setGroundSpeed(Integer groundSpeed) {
		getProperties().put(GROUND_SPEED_NAME, groundSpeed);
	}

	@JsonIgnore
	public Boolean isOnGround() {
		Integer isOnGround = getProperty(Integer.class, IS_ON_GROUND_PROPERTY_NAME);
		return isOnGround != null ? 
				isOnGround != 0 ? true : false : false;
	}

	public void setIsOnGround(Boolean isOnGround) {
		setIsOnGround(isOnGround ? -1 : 0);
	}

	public void setIsOnGround(Integer isOnGround) {
		getProperties().put(IS_ON_GROUND_PROPERTY_NAME, isOnGround);
	}

	public void setGeometry(Double longitude, Double latitude) {
		if(getGeometry() == null){
			setGeometry(new PointGeometry(longitude, latitude));
		}
		else{
			((PointGeometry) getGeometry()).setCoordinates(longitude, latitude);
		}
	}

	public void setMessageReceived(Date messageReceived) {
		this.messageReceived = messageReceived;
	}

	@JsonIgnore
	public Date getMessageReceived() {
		return messageReceived;
	}

	public void setMessageGenerated(String dateString, String timeString) {
		try {
			Date date = DateUtils.parseDate(dateString + " " + timeString, "yyyy/MM/dd HH:mm:ss.S");
			getProperties().put(DATE_TIME_MESSAGE_GENERATED_NAME, date);
		} catch (ParseException e) {
			// No Valid date,do Nothing
		}
	}

	@JsonIgnore
	public Date getMessageGenerated() {
		return getProperty(Date.class, DATE_TIME_MESSAGE_GENERATED_NAME);
	}

	@SuppressWarnings("unchecked")
	private <T> T getProperty(Class<T> type, String propertyName) {
		if (getProperties().containsKey(propertyName)) {
			return (T) getProperties().get(propertyName);
		} else {
			return null;
		}
	}
}
