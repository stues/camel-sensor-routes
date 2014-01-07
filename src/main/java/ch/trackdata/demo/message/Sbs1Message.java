package ch.trackdata.demo.message;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

@CsvRecord( separator = "," )
public class Sbs1Message {
	
	@DataField(pos = 1)
	@JsonIgnore 
	private String messageType;
	
	@DataField(pos = 2)
	private int transmissionType;
	
	@DataField(pos = 3)
	@JsonIgnore
	private int sessionId;

	@DataField(pos = 4)
	private int aircraftId;
	
	@DataField(pos = 5)
	private String hexIdent;
	
	@DataField(pos = 6)
	private int flightId;
	
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
	private String altitude;
	
	@DataField(pos = 13)
	private String groundSpeed;
	
	@DataField(pos = 14)
	private String track;
	
	@DataField(pos = 15)
	private String latitude;
	
	@DataField(pos = 16)
	private String longitude;
	
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
	 * @param messageType the messageType to set
	 */
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	/**
	 * @return the transmissionType
	 */
	public int getTransmissionType() {
		return transmissionType;
	}

	/**
	 * @param transmissionType the transmissionType to set
	 */
	public void setTransmissionType(int transmissionType) {
		this.transmissionType = transmissionType;
	}

	/**
	 * @return the sessionId
	 */
	public int getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the aircraftId
	 */
	public int getAircraftId() {
		return aircraftId;
	}

	/**
	 * @param aircraftId the aircraftId to set
	 */
	public void setAircraftId(int aircraftId) {
		this.aircraftId = aircraftId;
	}

	/**
	 * @return the hexIdent
	 */
	public String getHexIdent() {
		return hexIdent;
	}

	/**
	 * @param hexIdent the hexIdent to set
	 */
	public void setHexIdent(String hexIdent) {
		this.hexIdent = hexIdent;
	}

	/**
	 * @return the flightId
	 */
	public int getFlightId() {
		return flightId;
	}

	/**
	 * @param flightId the flightId to set
	 */
	public void setFlightId(int flightId) {
		this.flightId = flightId;
	}

	/**
	 * @return the dateMessageGenerated
	 */
	public String getDateMessageGenerated() {
		return dateMessageGenerated;
	}

	/**
	 * @param dateMessageGenerated the dateMessageGenerated to set
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
	 * @param timeMessageGenerated the timeMessageGenerated to set
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
	 * @param dateMessageLogged the dateMessageLogged to set
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
	 * @param timeMessageLogged the timeMessageLogged to set
	 */
	public void setTimeMessageLogged(String timeMessageLogged) {
		this.timeMessageLogged = timeMessageLogged;
	}

	public String getCallsign() {
		return callsign;
	}

	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}

	public String getAltitude() {
		return altitude;
	}

	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}

	public String getGroundSpeed() {
		return groundSpeed;
	}

	public void setGroundSpeed(String groundSpeed) {
		this.groundSpeed = groundSpeed;
	}

	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getVerticalRate() {
		return verticalRate;
	}

	public void setVerticalRate(String verticalRate) {
		this.verticalRate = verticalRate;
	}

	public String getSquawk() {
		return squawk;
	}

	public void setSquawk(String squawk) {
		this.squawk = squawk;
	}

	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

	public String getEmergency() {
		return emergency;
	}

	public void setEmergency(String emergency) {
		this.emergency = emergency;
	}

	public Integer getSpi() {
		return spi;
	}

	public void setSpi(Integer spi) {
		this.spi = spi;
	}

	public Integer getIsOnGround() {
		return isOnGround;
	}

	public void setIsOnGround(Integer isOnGround) {
		this.isOnGround = isOnGround;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
