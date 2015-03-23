package ch.trackdata.sbs1route.generator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import ch.trackdata.sbs1route.message.PointGeometry;
import ch.trackdata.sbs1route.message.TrackPositionMessage;

/**
 * This Class instantiates a defined amount of tracks {@link #amountOfTracks}
 * and moves them according the defined interval {@link #updateInterval}
 * the results are written into the producer {@link #producerTemplate}
 */
public class RandomTrackGenerator implements InitializingBean {

	private static final double KNOTS_TO_KMH_FACTOR = 1.852;

	private static final String THREAD_ID = "RandomTrackGenerator";

	private static final Logger LOGGER = LoggerFactory.getLogger(RandomTrackGenerator.class);

	private double minLatitude = 44;
	private double maxLatitude = 49;
	private double minLongitude = 4.5;
	private double maxLongitude = 14;

	private int maxAltitudeDelta = 4000;
	private int maxSpeedDelta = 40;
	private int maxHeadingDelta = 40;
	
	private boolean enabled = true;
	
	private List<TrackPositionMessage> trackPositions = null;

	private int currentTrack;
	
	private boolean generating = false;

	private int updateInterval;

	private int amountOfTracks;

	private Thread offlineSourceThread;

	private ProducerTemplate producerTemplate;

	private final Random random = new Random();

	/**
	 * Called after all spring properties are set Instantiate the Array and
	 * start the update thread
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		if(isEnabled() && amountOfTracks > 0){
			trackPositions = new ArrayList<TrackPositionMessage>(amountOfTracks);
	
			for (int i = 0; i < amountOfTracks; i++) {
				TrackPositionMessage trackPosition = createRandomTrackPosition();
				trackPositions.add(trackPosition);
			}
	
			generateTracks();
		}
	}

	/**
	 * Creates a new {@link TrackPositionMessage} which contains random data
	 * 
	 * @return the {@link TrackPositionMessage} which contains random data
	 */
	private TrackPositionMessage createRandomTrackPosition() {
		TrackPositionMessage trackPosition = new TrackPositionMessage();
		trackPosition.setHex(getRandomHexIdent());
		trackPosition.setCallSign(getRandomCallSign());
		trackPosition.setGroundSpeed(200 + random.nextInt(400));
		trackPosition.setHeading(1 + random.nextInt(360));
		trackPosition.setAltitude(5000 + random.nextInt(15000));
		trackPosition.setGeometry(getRandomPointPosition());
		trackPosition.setMessageReceived(new Date());
		trackPosition.setMessageGenerated(new Date());
		trackPosition.setIsOnGround(false);
		return trackPosition;
	}

	/**
	 * @return a random hex identifier (mode-S)
	 */
	protected String getRandomHexIdent() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 2; i++) {
			builder.append((char)('A' + random.nextInt(('Z'-'A')+1)));
		}
		builder.append(random.nextInt(10000));
		return builder.toString();
	}

	/**
	 * @return a random callsign
	 */
	protected String getRandomCallSign() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 3; i++) {
			builder.append((char)('A' + random.nextInt(('Z'-'A')+1)));
		}
		builder.append(random.nextInt(1000));
		return builder.toString();
	}

	/**
	 * @return a random point position within the defined bounds
	 */
	protected PointGeometry getRandomPointPosition() {
		double latitudeOffset = (maxLatitude - minLatitude) * random.nextDouble();
		double randomLatitude = minLatitude + latitudeOffset;

		double longitudeOffset = (maxLongitude - minLongitude) * random.nextDouble();
		double randomLongitude = minLongitude + longitudeOffset;

		return new PointGeometry(randomLongitude, randomLatitude);
	}

	/**
	 * Starts the Track Generation
	 */
	public void generateTracks() {
		LOGGER.info("Connecting Source");
		if (isTrackGenerating() && offlineSourceThread != null) {
			generating = false;
			offlineSourceThread.interrupt();
			offlineSourceThread = null;
		}
		generating = true;
		offlineSourceThread = new Thread(new GeneratorRunnable());
		offlineSourceThread.setName(THREAD_ID);
		offlineSourceThread.start();
	}

	/**
	 * @return true if generating random tracks is started otherwise false
	 */
	public boolean isTrackGenerating() {
		return generating;
	}

	/**
	 * Stops offline source generating and updating tracks.
	 */
	public void disconnectSource() {
		generating = true;
	}

	/**
	 * move and update the given trackPosition according the attributes 
	 * @param trackPosition the trackPosition to move
	 */
	private void updateTrackPosition(TrackPositionMessage trackPosition) {
		updateHeading(trackPosition);
		updatePosition(trackPosition);
		updateGroundSpeed(trackPosition);
		updateAltitude(trackPosition);
		trackPosition.setMessageReceived(new Date());
		trackPosition.setMessageGenerated(new Date());
	}

	/**
	 * Returns the new value +- half of the max delta
	 * @param value the value
	 * @param maxDelta the maximum delta
	 * @return the calculated random value
	 */
	private int getRandomValue(int value, int maxDelta){
		return value + (int)( Math.random() * maxDelta - (maxDelta / 2));
	}
	
	/**
	 * Update the altitude of the given TrackPosition
	 * @param trackPosition the trackPosition to update
	 */
	private void updateAltitude(TrackPositionMessage trackPosition) {
		int halfMaxAltitudeDelta = maxAltitudeDelta/2;
		int trackAltitude = getRandomValue(trackPosition.getAltitude(), maxAltitudeDelta);

		if (trackAltitude < halfMaxAltitudeDelta) {
			trackAltitude = halfMaxAltitudeDelta;
		} else if (trackAltitude > maxAltitudeDelta*10) {
			trackAltitude = maxAltitudeDelta*10;
		}
		trackPosition.setAltitude(trackAltitude);
	}
	
	/**
	 * Update the ground speed of the given TrackPosition
	 * @param trackPosition the trackPosition to update
	 */
	private void updateGroundSpeed(TrackPositionMessage trackPosition) {
		int speedKnots = getRandomValue(trackPosition.getGroundSpeed(), maxSpeedDelta);
		if (speedKnots < maxSpeedDelta) {
			speedKnots = maxSpeedDelta;
		} else if (speedKnots > maxSpeedDelta*20) {
			speedKnots = maxSpeedDelta*20;
		}
		trackPosition.setGroundSpeed(speedKnots);
	}
	
	/**
	 * Update the position of the given TrackPosition
	 * @param trackPosition the trackPosition to update
	 */
	private void updatePosition(TrackPositionMessage trackPosition) {
		PointGeometry geometry = (PointGeometry) trackPosition.getGeometry();
		double currentLongitude = geometry.getLongitude();
		double currentLatitude = geometry.getLatitude();

		final double kmhGroundSpeed = trackPosition.getGroundSpeed() * KNOTS_TO_KMH_FACTOR;
		double absolutDistance = kmhGroundSpeed * (double) updateInterval / 3600000;

		double heading = trackPosition.getHeading();
		double latitudeDelta = absolutDistance * Math.cos(Math.toRadians(heading)) / 100.0;
		double longitudeDelta = absolutDistance * Math.sin(Math.toRadians(heading)) / 100.0;

		trackPosition.setGeometry(currentLongitude + longitudeDelta, currentLatitude + latitudeDelta);
	}

	/**
	 * Update the heading of the given TrackPosition
	 * @param trackPosition the trackPosition to update
	 */
	private void updateHeading(TrackPositionMessage trackPosition) {
		
		PointGeometry geometry = (PointGeometry) trackPosition.getGeometry();
		double currentLongitude = geometry.getLongitude();
		double currentLatitude = geometry.getLatitude();
		
		if ((currentLatitude < minLatitude) || (currentLatitude > maxLatitude) || (currentLongitude < minLongitude) || (currentLongitude > maxLongitude)) {
			int heading = trackPosition.getHeading();
			if (random.nextBoolean()) {
				heading += 45;
			} else {
				heading -= 45;
			}
			trackPosition.setHeading(heading % 360);
		} else {
			if (random.nextInt(100)>=99) {
				int heading = getRandomValue(trackPosition.getHeading(),maxHeadingDelta);
				if (heading <= 0) {
					heading += 360;
				} else if (heading > 360) {
					heading -= 360;
				}
				trackPosition.setHeading(heading);
			}
		}
	}

	/**
	 * @return the updateInterval
	 */
	public int getUpdateInterval() {
		return updateInterval;
	}

	/**
	 * @param updateInterval the updateInterval to set
	 */
	public void setUpdateInterval(int updateInterval) {
		this.updateInterval = updateInterval;
	}

	/**
	 * @return the producerTemplate
	 */
	public ProducerTemplate getProducerTemplate() {
		return producerTemplate;
	}

	/**
	 * @param producerTemplate the producerTemplate to set
	 */
	public void setProducerTemplate(ProducerTemplate producerTemplate) {
		this.producerTemplate = producerTemplate;
	}

	/**
	 * @return the minLatitude
	 */
	public double getMinLatitude() {
		return minLatitude;
	}
	
	/**
	 * @param minLatitude the minLatitude to set
	 */
	public void setMinLatitude(double minLatitude) {
		this.minLatitude = minLatitude;
	}

	/**
	 * @return the maxLatitude
	 */
	public double getMaxLatitude() {
		return maxLatitude;
	}

	/**
	 * @param maxLatitude the maxLatitude to set
	 */
	public void setMaxLatitude(double maxLatitude) {
		this.maxLatitude = maxLatitude;
	}

	/**
	 * @return the minLongitude
	 */
	public double getMinLongitude() {
		return minLongitude;
	}

	/**
	 * @param minLongitude the minLongitude to set
	 */
	public void setMinLongitude(double minLongitude) {
		this.minLongitude = minLongitude;
	}

	/**
	 * @return the maxLongitude
	 */
	public double getMaxLongitude() {
		return maxLongitude;
	}

	/**
	 * @param maxLongitude the maxLongitude to set
	 */
	public void setMaxLongitude(double maxLongitude) {
		this.maxLongitude = maxLongitude;
	}

	/**
	 * @return the maxAltitudeDelta
	 */
	public int getMaxAltitudeDelta() {
		return maxAltitudeDelta;
	}

	/**
	 * @param maxAltitudeDelta the maxAltitudeDelta to set
	 */
	public void setMaxAltitudeDelta(int maxAltitudeDelta) {
		this.maxAltitudeDelta = maxAltitudeDelta;
	}

	/**
	 * @return the maxSpeedDelta
	 */
	public int getMaxSpeedDelta() {
		return maxSpeedDelta;
	}

	/**
	 * @param maxSpeedDelta the maxSpeedDelta to set
	 */
	public void setMaxSpeedDelta(int maxSpeedDelta) {
		this.maxSpeedDelta = maxSpeedDelta;
	}

	/**
	 * @return the amountOfTracks
	 */
	public int getAmountOfTracks() {
		return amountOfTracks;
	}

	/**
	 * @param amountOfTracks the amountOfTracks to set
	 */
	public void setAmountOfTracks(int amountOfTracks) {
		this.amountOfTracks = amountOfTracks;
	}

	/**
	 * @return the maxHeadingDelta
	 */
	public int getMaxHeadingDelta() {
		return maxHeadingDelta;
	}

	/**
	 * @param maxHeadingDelta the maxHeadingDelta to set
	 */
	public void setMaxHeadingDelta(int maxHeadingDelta) {
		this.maxHeadingDelta = maxHeadingDelta;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Runnable which updates the Track Information from time to time
	 */
	protected class GeneratorRunnable implements Runnable {

		/**
		 * Used when thread is launched. Works as long it is not stopped by
		 * external interrupt.
		 */
		@Override
		public void run() {
			while (generating) {
				try {
					LOGGER.info("Tracks send - sleeping for " + updateInterval / 1000 + "s");
					Thread.sleep(updateInterval);
					for (TrackPositionMessage trackPosition: trackPositions) {
						updateTrackPosition(trackPosition);
					}
					LOGGER.info("Sending Tracks");
					
					TrackPositionMessage trackPosition = trackPositions.get(currentTrack);
					
					producerTemplate.sendBody(removeSomeProperties(trackPosition));
					
					currentTrack = ++currentTrack%amountOfTracks;
					
				} catch (InterruptedException e) {
					LOGGER.warn("Problems with thread ", e);
				} catch (Exception e) {
					LOGGER.warn("General Exception", e);
				}
			}
		}

		/**
		 * Does clone the given {@link TrackPositionMessage} and remove some random properties
		 * 
		 * @param trackPosition the trackPosition
		 * @return a clone of the given trackPosition with less properties 
		 */
		private TrackPositionMessage removeSomeProperties(TrackPositionMessage trackPosition) {
			TrackPositionMessage trackPositionClone = SerializationUtils.clone(trackPosition);
			
			if(doRemoveProperty()){
				trackPositionClone.setGeometry(null);
			}
			if(doRemoveProperty()){
				trackPositionClone.setHeading(null);
			}
			if(doRemoveProperty()){
				trackPositionClone.setAltitude(null);
			}
			if(doRemoveProperty()){
				trackPositionClone.setCallSign(null);
			}
			if(doRemoveProperty()){
				trackPositionClone.setIsOnGround((Boolean)null);
			}
			return trackPositionClone;
		}
		
		/**
		 * @return does randomly return true
		 */
		private boolean doRemoveProperty() {
			return random.nextInt(5)>=4;
		}
	}


}
