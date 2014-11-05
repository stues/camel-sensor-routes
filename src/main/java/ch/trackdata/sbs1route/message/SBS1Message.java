package ch.trackdata.sbs1route.message;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * SBS1-Object with all provided fields
 */
@CsvRecord(separator = ",")
public class SBS1Message {

	@DataField(pos = 1)
	private String messageType;

	@DataField(pos = 2)
	private Integer transmissionType;

	@DataField(pos = 3)
	private Integer sessionId;

	@DataField(pos = 4)
	private Integer aircraftId;

	@DataField(pos = 5)
	private String hexIdent;

	@DataField(pos = 6)
	private Integer flightId;

	@DataField(pos = 7)
	private String dateMessageGenerated;

	@DataField(pos = 8)
	private String timeMessageGenerated;

	@DataField(pos = 9)
	private String dateMessageLogged;

	@DataField(pos = 10)
	private String timeMessageLogged;

	@DataField(pos = 11)
	private String callsign;

	@DataField(pos = 12)
	private Integer altitude;

	@DataField(pos = 13)
	private Integer groundSpeed;

	@DataField(pos = 14)
	private Integer heading;

	@DataField(pos = 15)
	private Double latitude;

	@DataField(pos = 16)
	private Double longitude;

	@DataField(pos = 17)
	private String verticalRate;

	@DataField(pos = 18)
	private String squawk;

	@DataField(pos = 19)
	private String alert;

	@DataField(pos = 20)
	private String emergency;

	@DataField(pos = 21)
	private Integer spi;

	@DataField(pos = 22)
	private Integer isOnGround;

	/**
	 * @return the messageType
	 */
	public String getMessageType() {
		return messageType;
	}

	/**
	 * @param messageType
	 *            the messageType to set
	 */
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	/**
	 * @return the transmissionType
	 */
	public Integer getTransmissionType() {
		return transmissionType;
	}

	/**
	 * @param transmissionType
	 *            the transmissionType to set
	 */
	public void setTransmissionType(Integer transmissionType) {
		this.transmissionType = transmissionType;
	}

	/**
	 * @return the sessionId
	 */
	public Integer getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId
	 *            the sessionId to set
	 */
	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the aircraftId
	 */
	public Integer getAircraftId() {
		return aircraftId;
	}

	/**
	 * @param aircraftId
	 *            the aircraftId to set
	 */
	public void setAircraftId(Integer aircraftId) {
		this.aircraftId = aircraftId;
	}

	/**
	 * @return the hexIdent
	 */
	public String getHexIdent() {
		return hexIdent;
	}

	/**
	 * @param hexIdent
	 *            the hexIdent to set
	 */
	public void setHexIdent(String hexIdent) {
		this.hexIdent = hexIdent;
	}

	/**
	 * @return the flightId
	 */
	public Integer getFlightId() {
		return flightId;
	}

	/**
	 * @param flightId
	 *            the flightId to set
	 */
	public void setFlightId(Integer flightId) {
		this.flightId = flightId;
	}

	/**
	 * @return the dateMessageGenerated
	 */
	public String getDateMessageGenerated() {
		return dateMessageGenerated;
	}

	/**
	 * @param dateMessageGenerated
	 *            the dateMessageGenerated to set
	 */
	public void setDateMessageGenerated(String dateMessageGenerated) {
		this.dateMessageGenerated = dateMessageGenerated;
	}

	/**
	 * @return the timeMessageGenerated
	 */
	public String getTimeMessageGenerated() {
		return timeMessageGenerated;
	}

	/**
	 * @param timeMessageGenerated
	 *            the timeMessageGenerated to set
	 */
	public void setTimeMessageGenerated(String timeMessageGenerated) {
		this.timeMessageGenerated = timeMessageGenerated;
	}

	/**
	 * @return the dateMessageLogged
	 */
	public String getDateMessageLogged() {
		return dateMessageLogged;
	}

	/**
	 * @param dateMessageLogged
	 *            the dateMessageLogged to set
	 */
	public void setDateMessageLogged(String dateMessageLogged) {
		this.dateMessageLogged = dateMessageLogged;
	}

	/**
	 * @return the timeMessageLogged
	 */
	public String getTimeMessageLogged() {
		return timeMessageLogged;
	}

	/**
	 * @param timeMessageLogged
	 *            the timeMessageLogged to set
	 */
	public void setTimeMessageLogged(String timeMessageLogged) {
		this.timeMessageLogged = timeMessageLogged;
	}

	/**
	 * @return the callsign
	 */
	public String getCallsign() {
		return callsign;
	}

	/**
	 * @param callsign
	 *            the callsign to set
	 */
	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}

	/**
	 * @return the altitude
	 */
	public Integer getAltitude() {
		return altitude;
	}

	/**
	 * @param altitude
	 *            the altitude to set
	 */
	public void setAltitude(Integer altitude) {
		this.altitude = altitude;
	}

	/**
	 * @return the groundSpeed
	 */
	public Integer getGroundSpeed() {
		return groundSpeed;
	}

	/**
	 * @param groundSpeed
	 *            the groundSpeed to set
	 */
	public void setGroundSpeed(Integer groundSpeed) {
		this.groundSpeed = groundSpeed;
	}

	/**
	 * @return the heading
	 */
	public Integer getHeading() {
		return heading;
	}

	/**
	 * @param heading
	 *            the heading to set
	 */
	public void setHeading(Integer heading) {
		this.heading = heading;
	}

	/**
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the verticalRate
	 */
	public String getVerticalRate() {
		return verticalRate;
	}

	/**
	 * @param verticalRate
	 *            the verticalRate to set
	 */
	public void setVerticalRate(String verticalRate) {
		this.verticalRate = verticalRate;
	}

	/**
	 * @return the squawk
	 */
	public String getSquawk() {
		return squawk;
	}

	/**
	 * @param squawk
	 *            the squawk to set
	 */
	public void setSquawk(String squawk) {
		this.squawk = squawk;
	}

	/**
	 * @return the alert
	 */
	public String getAlert() {
		return alert;
	}

	/**
	 * @param alert
	 *            the alert to set
	 */
	public void setAlert(String alert) {
		this.alert = alert;
	}

	/**
	 * @return the emergency
	 */
	public String getEmergency() {
		return emergency;
	}

	/**
	 * @param emergency
	 *            the emergency to set
	 */
	public void setEmergency(String emergency) {
		this.emergency = emergency;
	}

	/**
	 * @return the spi
	 */
	public Integer getSpi() {
		return spi;
	}

	/**
	 * @param spi
	 *            the spi to set
	 */
	public void setSpi(Integer spi) {
		this.spi = spi;
	}

	/**
	 * @return the isOnGround
	 */
	public Integer getIsOnGround() {
		return isOnGround;
	}

	/**
	 * @param isOnGround
	 *            the isOnGround to set
	 */
	public void setIsOnGround(Integer isOnGround) {
		this.isOnGround = isOnGround;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
