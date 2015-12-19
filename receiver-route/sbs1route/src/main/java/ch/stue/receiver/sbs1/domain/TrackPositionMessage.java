package ch.stue.receiver.sbs1.domain;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.time.DateUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ch.stue.domain.GeoJSONFeature;
import ch.stue.domain.PointGeometry;

/**
 * Extends the GeoJSONFeature with some utility methods to instantiate a
 * {@link TrackPositionMessage}
 *
 * It also provides some getter and setter methods for the Mode-S Attributes
 *
 * @author stue
 */
public class TrackPositionMessage extends GeoJSONFeature<PointGeometry> {

	private static final long serialVersionUID = 1L;

	public static String HEX_PROPERTY_NAME = "hexIdent";
	public static String CALLSIGN_PROPERTY_NAME = "callsign";
	public static String ALTITUDE_PROPERTY_NAME = "altitude";
	public static String HEADING_PROPERTY_NAME = "heading";
	public static String GROUND_SPEED_NAME = "groundSpeed";
	public static String DATE_TIME_MESSAGE_GENERATED_NAME = "messageGenerated";

	private Date messageReceived;

	/**
	 * Default Constructor
	 */
	public TrackPositionMessage() {
		super(null, new HashMap<String, Object>());
	}

	/**
	 * @return the hex id as {@link String}
	 */
	@JsonIgnore
	public String getHex() {
		return getProperty(HEX_PROPERTY_NAME, String.class);
	}

	/**
	 * @param hex
	 *            the hex id to set
	 */
	public void setHex(String hex) {
		if(hex != null){
			getProperties().put(HEX_PROPERTY_NAME, hex);
		} else {
			getProperties().remove(HEX_PROPERTY_NAME);
		}
	}

	/**
	 * @return the call sign as {@link String}
	 */
	@JsonIgnore
	public String getCallSign() {
		return getProperty(CALLSIGN_PROPERTY_NAME, String.class);
	}

	/**
	 * @param callSign
	 *            the call sign to set
	 */
	public void setCallSign(String callSign) {
		if (callSign != null) {
			getProperties().put(CALLSIGN_PROPERTY_NAME, callSign);
		} else {
			getProperties().remove(CALLSIGN_PROPERTY_NAME);
		}
	}

	/**
	 * @return the heading as {@link Integer}
	 */
	@JsonIgnore
	public Integer getHeading() {
		return getProperty(HEADING_PROPERTY_NAME, Integer.class);
	}

	public void setHeading(Integer heading) {
		if (heading != null) {
			getProperties().put(HEADING_PROPERTY_NAME, heading);
		} else {
			getProperties().remove(HEADING_PROPERTY_NAME);
		}
	}

	/**
	 * @return the altitude as {@link Integer}
	 */
	@JsonIgnore
	public Integer getAltitude() {
		return getProperty(ALTITUDE_PROPERTY_NAME, Integer.class);
	}

	/**
	 * @param altitude
	 *            the altitude to set
	 */
	public void setAltitude(Integer altitude) {
		if (altitude != null) {
			getProperties().put(ALTITUDE_PROPERTY_NAME, altitude);
		} else {
			getProperties().remove(ALTITUDE_PROPERTY_NAME);
		}
	}

	/**
	 * @return the speed over ground as {@link Integer}
	 */
	@JsonIgnore
	public Integer getGroundSpeed() {
		return getProperty(GROUND_SPEED_NAME, Integer.class);
	}

	/**
	 * @param groundSpeed
	 *            the ground speed to set
	 */
	public void setGroundSpeed(Integer groundSpeed) {
		if (groundSpeed != null) {
			getProperties().put(GROUND_SPEED_NAME, groundSpeed);
		} else {
			getProperties().remove(GROUND_SPEED_NAME);
		}
	}

	/**
	 * Sets the geometry with longitude and latitude
	 *
	 * @param longitude
	 *            the longitude
	 * @param latitude
	 *            the latitude
	 */
	public void setGeometry(Double longitude, Double latitude) {
		if (getGeometry() == null) {
			setGeometry(new PointGeometry(longitude, latitude));
		} else {
			getGeometry().setCoordinates(longitude, latitude);
		}
	}

	/**
	 * @param messageReceived
	 *            sets the date when the message was received from the system
	 */
	public void setMessageReceived(Date messageReceived) {
		this.messageReceived = messageReceived;
	}

	/**
	 * @return the date when the message was received from the system
	 */
	@JsonIgnore
	public Date getMessageReceived() {
		return this.messageReceived;
	}

	/**
	 * Sets the message generated date as to strings
	 *
	 * @param dateString
	 *            the date string
	 * @param timeString
	 *            the time string
	 */
	public void setMessageGenerated(String dateString, String timeString) {
		try {
			if (dateString != null && timeString != null) {
				Date date = DateUtils.parseDate(String.format("%s %s",dateString, timeString), "yyyy/MM/dd HH:mm:ss.S");
				getProperties().put(DATE_TIME_MESSAGE_GENERATED_NAME, date);
			} else {
				getProperties().remove(DATE_TIME_MESSAGE_GENERATED_NAME);
			}
		} catch (ParseException e) {
			// No Valid date,do Nothing
		}
	}

	/**
	 * Sets the message generated date
	 *
	 * @param messageGenerated
	 *            the daten when the message has been generated
	 */
	public void setMessageGenerated(Date messageGgenerated) {
		if (messageGgenerated != null) {
			getProperties().put(DATE_TIME_MESSAGE_GENERATED_NAME, messageGgenerated);
		} else {
			getProperties().remove(DATE_TIME_MESSAGE_GENERATED_NAME);
		}
	}

	/**
	 * @return when the message was generated
	 */
	@JsonIgnore
	public Date getMessageGenerated() {
		return getProperty(DATE_TIME_MESSAGE_GENERATED_NAME, Date.class);
	}
}
