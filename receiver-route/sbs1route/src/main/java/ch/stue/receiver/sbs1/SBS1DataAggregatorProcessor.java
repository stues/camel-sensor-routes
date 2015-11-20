package ch.stue.receiver.sbs1;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import ch.stue.receiver.sbs1.domain.SBS1Message;
import ch.stue.receiver.sbs1.domain.TrackPositionMessage;

/**
 * Aggregates a given SBS1Message with already received data
 * an returns a TrackPosition Message with all the information
 * 
 * @author stue
 */
public class SBS1DataAggregatorProcessor implements Processor {
	
	private static final String STA_MESSAGE_TYPE = "STA";
	private static final String ID_MESSAGE_TYPE = "ID";
	private static final String MSG_MESSAGE_TYPE = "MSG";

	private static final Logger LOGGER = LoggerFactory.getLogger(SBS1DataAggregatorProcessor.class);

	private Map<String, TrackPositionMessage> trackPositions;
	
	private Timer cleanupTimer;

	private Integer cleanupInterval;
	
	/**
	 * Default Constructor
	 */
	public SBS1DataAggregatorProcessor() {
		trackPositions = new HashMap<String, TrackPositionMessage>();
		
		cleanupTimer = new Timer();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void process(Exchange exchange) throws Exception {
		SBS1Message sbs1Message = exchange.getIn().getBody(SBS1Message.class);
		
		TrackPositionMessage trackPosition;
		synchronized(trackPositions){
			trackPosition = getUpdatedTrackPosition(sbs1Message);
		}
				
		LOGGER.info("Updated message: {}", trackPosition);
		
		exchange.getOut().setBody(trackPosition);
	}

	/**
	 * Returns an aggregated Message
	 * @param sbs1Message the sbs1Message
	 * @return the aggregated Message {@link TrackPositionMessage}
	 */
	private TrackPositionMessage getUpdatedTrackPosition(SBS1Message sbs1Message) {
		TrackPositionMessage trackPosition = getTrackPosition(sbs1Message);
		trackPosition = updateTrackPosition(trackPosition, sbs1Message);
		
		return trackPosition;
	}

	/**
	 * Updates the trackPosition with information from the given sbs1Message
	 * @param trackPosition the trackPosition
	 * @param sbs1Message the sbs1Message
	 * @return 
	 */
	private TrackPositionMessage updateTrackPosition(TrackPositionMessage trackPosition, SBS1Message sbs1Message) {
		trackPosition.setMessageReceived(new Date());
		trackPosition.setMessageGenerated(sbs1Message.getDateMessageGenerated(), sbs1Message.getTimeMessageGenerated());
		
		if(MSG_MESSAGE_TYPE.equals(sbs1Message.getMessageType())){
			LOGGER.info("Message received for track: {}", sbs1Message.getHexIdent());
			switch(sbs1Message.getTransmissionType()){
			case(1):
				trackPosition.setHex(sbs1Message.getHexIdent());
				trackPosition.setCallSign(sbs1Message.getCallsign());
				break;
			case(2):
				trackPosition.setHex(sbs1Message.getHexIdent());
				trackPosition.setAltitude(sbs1Message.getAltitude());
				trackPosition.setGroundSpeed(sbs1Message.getGroundSpeed());
				trackPosition.setHeading(sbs1Message.getHeading());
				trackPosition.setGeometry(sbs1Message.getLongitude(), sbs1Message.getLatitude());
				break;
			case(3):
				trackPosition.setHex(sbs1Message.getHexIdent());
				trackPosition.setAltitude(sbs1Message.getAltitude());
				trackPosition.setGeometry(sbs1Message.getLongitude(), sbs1Message.getLatitude());
				break;
			case(4):
				trackPosition.setHex(sbs1Message.getHexIdent());
				trackPosition.setGroundSpeed(sbs1Message.getGroundSpeed());
				trackPosition.setHeading(sbs1Message.getHeading());
				break;
			case(5):
			case(7):
				trackPosition.setHex(sbs1Message.getHexIdent());
				trackPosition.setAltitude(sbs1Message.getAltitude());
				break;
			case(8):
				trackPosition.setHex(sbs1Message.getHexIdent());
				break;
			default:
				break;
			}				
		} else if(ID_MESSAGE_TYPE.equals(sbs1Message.getMessageType())){
			LOGGER.info("ID Changed of track: {}", sbs1Message.getHexIdent());
			trackPosition.setCallSign(sbs1Message.getCallsign());
		} else if(STA_MESSAGE_TYPE.equals(sbs1Message.getMessageType())){
			LOGGER.info("STA Message, remove track: {}", sbs1Message.getHexIdent());
			trackPositions.remove(sbs1Message.getHexIdent());
		}
		return trackPosition;
	}

	/**
	 * Returns the current {@link TrackPositionMessage} from the trackPositions Map
	 * @param sbs1Message the sbs1Message
	 * @return the current {@link TrackPositionMessage} from the trackPositions Map
	 */
	private TrackPositionMessage getTrackPosition(SBS1Message sbs1Message) {
		String hexIdent = sbs1Message.getHexIdent();
		TrackPositionMessage trackPositionMessage;
		if (!trackPositions.containsKey(hexIdent)) {
			trackPositionMessage = new TrackPositionMessage();
			trackPositions.put(hexIdent, trackPositionMessage);
		}
		else{
			trackPositionMessage = trackPositions.get(hexIdent);
		}
		return trackPositionMessage;
	}

	/**
	 * Returns the current {@link TrackPositionMessage} from the trackPositions Map
	 * @param sbs1Message the sbs1Message
	 * @return the current {@link TrackPositionMessage} from the trackPositions Map
	 */
	private void cleanup() {
		synchronized(trackPositions){
			LOGGER.info("Removing Old TrackPositions({})", trackPositions.size());
			Iterator<Entry<String, TrackPositionMessage>> iterator = trackPositions.entrySet().iterator();
			Date oldestDate = DateUtils.addMilliseconds(new Date(), -cleanupInterval);
			while(iterator.hasNext()){
				Entry<String, TrackPositionMessage> trackPositionEntry  = iterator.next();
				TrackPositionMessage trackPositionMessage = trackPositionEntry.getValue();
				if(oldestDate.after(trackPositionMessage.getMessageReceived())){
					iterator.remove();
					LOGGER.info("Old message removed: {}", trackPositionMessage);
				}
			}
			
			LOGGER.info("Old messages removed: ({})", trackPositions.size());
		}
	}

	/**
	 * @return the cleanupInterval
	 */
	public Integer getCleanupInterval() {
		return cleanupInterval;
	}

	/**
	 * @param cleanupInterval the cleanupInterval to set
	 */
	@Required
	public void setCleanupInterval(Integer cleanupInterval) {
		this.cleanupInterval = cleanupInterval;
		startTimer();
	}

	/**
	 * Starts the cleanup Timer
	 */
	private void startTimer() {
		cleanupTimer.schedule(new TimerTask(){

			@Override
			public void run() {
				cleanup();
			}}, cleanupInterval,cleanupInterval);
	}
	
}
