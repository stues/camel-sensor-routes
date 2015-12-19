package ch.stue.receiver.sbs1.generator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import ch.stue.domain.PointGeometry;
import ch.stue.receiver.sbs1.domain.TrackPositionMessage;

/**
 * This Class instantiates a defined amount of tracks {@link #amountOfTracks}
 * and moves them according the defined interval {@link #updateInterval} the
 * results are written into the producer {@link #producerTemplate}
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

	private TrackGeneratorIntervalAware fixGeneratorInterval;

	private TrackGeneratorIntervalAware variableGeneratorInterval;

	private boolean useVariableInterval = false;

	private int amountOfTracks;

	private Thread offlineSourceThread;

	private ProducerTemplate producerTemplate;

	private final Random random = new Random();

	/**
	 * Instantiate the Array and start the update thread
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		if (isEnabled() && this.amountOfTracks > 0) {
			this.trackPositions = new ArrayList<TrackPositionMessage>(this.amountOfTracks);

			for (int i = 0; i < this.amountOfTracks; i++) {
				TrackPositionMessage trackPosition = createRandomTrackPosition();
				this.trackPositions.add(trackPosition);
			}
		}
	}

	/**
	 * Starts the generator
	 */
	public void startGenerator() {
		getUpdateIntervalAware().start(this);
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
		trackPosition.setGroundSpeed(200 + this.random.nextInt(400));
		trackPosition.setHeading(1 + this.random.nextInt(360));
		trackPosition.setAltitude(5000 + this.random.nextInt(15000));
		trackPosition.setGeometry(getRandomPointPosition());
		trackPosition.setMessageReceived(new Date());
		trackPosition.setMessageGenerated(new Date());
		return trackPosition;
	}

	/**
	 * @return a random hex identifier (mode-S)
	 */
	protected String getRandomHexIdent() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 2; i++) {
			builder.append((char) ('A' + this.random.nextInt(('Z' - 'A') + 1)));
		}
		builder.append(this.random.nextInt(10000));
		return builder.toString();
	}

	/**
	 * @return a random callsign
	 */
	protected String getRandomCallSign() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 3; i++) {
			builder.append((char) ('A' + this.random.nextInt(('Z' - 'A') + 1)));
		}
		builder.append(this.random.nextInt(1000));
		return builder.toString();
	}

	/**
	 * @return a random point position within the defined bounds
	 */
	protected PointGeometry getRandomPointPosition() {
		double latitudeOffset = (this.maxLatitude - this.minLatitude) * this.random.nextDouble();
		double randomLatitude = this.minLatitude + latitudeOffset;

		double longitudeOffset = (this.maxLongitude - this.minLongitude) * this.random.nextDouble();
		double randomLongitude = this.minLongitude + longitudeOffset;

		return new PointGeometry(randomLongitude, randomLatitude);
	}

	/**
	 * Starts the Track Generation
	 */
	public void generateTracks() {
		LOGGER.info("Connecting Source");
		if (isTrackGenerating()) {
			disconnectSource();
		}
		this.generating = true;
		this.offlineSourceThread = new Thread(new GeneratorRunnable());
		this.offlineSourceThread.setName(THREAD_ID);
		this.offlineSourceThread.start();
	}

	/**
	 * @return true if generating random tracks is started otherwise false
	 */
	public boolean isTrackGenerating() {
		return this.generating;
	}

	/**
	 * Stops offline source generating and updating tracks.
	 */
	public void disconnectSource() {
		if (isTrackGenerating() && this.offlineSourceThread != null) {
			this.offlineSourceThread.interrupt();
			this.offlineSourceThread = null;
		}
		this.generating = false;
	}

	/**
	 * move and update the given trackPosition according the attributes
	 *
	 * @param trackPosition
	 *            the trackPosition to move
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
	 *
	 * @param value
	 *            the value
	 * @param maxDelta
	 *            the maximum delta
	 * @return the calculated random value
	 */
	private int getRandomValue(int value, int maxDelta) {
		return value + (int) (Math.random() * maxDelta - (maxDelta / 2));
	}

	/**
	 * Update the altitude of the given TrackPosition
	 *
	 * @param trackPosition
	 *            the trackPosition to update
	 */
	private void updateAltitude(TrackPositionMessage trackPosition) {
		int halfMaxAltitudeDelta = this.maxAltitudeDelta / 2;
		int trackAltitude = getRandomValue(trackPosition.getAltitude(), this.maxAltitudeDelta);

		if (trackAltitude < halfMaxAltitudeDelta) {
			trackAltitude = halfMaxAltitudeDelta;
		} else if (trackAltitude > this.maxAltitudeDelta * 10) {
			trackAltitude = this.maxAltitudeDelta * 10;
		}
		trackPosition.setAltitude(trackAltitude);
	}

	/**
	 * Update the ground speed of the given TrackPosition
	 *
	 * @param trackPosition
	 *            the trackPosition to update
	 */
	private void updateGroundSpeed(TrackPositionMessage trackPosition) {
		int speedKnots = getRandomValue(trackPosition.getGroundSpeed(), this.maxSpeedDelta);
		if (speedKnots < this.maxSpeedDelta) {
			speedKnots = this.maxSpeedDelta;
		} else if (speedKnots > this.maxSpeedDelta * 20) {
			speedKnots = this.maxSpeedDelta * 20;
		}
		trackPosition.setGroundSpeed(speedKnots);
	}

	/**
	 * Update the position of the given TrackPosition
	 *
	 * @param trackPosition
	 *            the trackPosition to update
	 */
	private void updatePosition(TrackPositionMessage trackPosition) {
		PointGeometry geometry = trackPosition.getGeometry();
		double currentLongitude = geometry.getLongitude();
		double currentLatitude = geometry.getLatitude();

		final double kmhGroundSpeed = trackPosition.getGroundSpeed() * KNOTS_TO_KMH_FACTOR;
		double absolutDistance = kmhGroundSpeed * getUpdateInterval() / 3600000;

		double heading = trackPosition.getHeading();
		double latitudeDelta = absolutDistance * Math.cos(Math.toRadians(heading)) / 100.0;
		double longitudeDelta = absolutDistance * Math.sin(Math.toRadians(heading)) / 100.0;

		trackPosition.setGeometry(currentLongitude + longitudeDelta, currentLatitude + latitudeDelta);
	}

	/**
	 * Update the heading of the given TrackPosition
	 *
	 * @param trackPosition
	 *            the trackPosition to update
	 */
	private void updateHeading(TrackPositionMessage trackPosition) {

		PointGeometry geometry = trackPosition.getGeometry();
		double currentLongitude = geometry.getLongitude();
		double currentLatitude = geometry.getLatitude();

		if ((currentLatitude < this.minLatitude) || (currentLatitude > this.maxLatitude) || (currentLongitude < this.minLongitude) || (currentLongitude > this.maxLongitude)) {
			int heading = trackPosition.getHeading();
			if (this.random.nextBoolean()) {
				heading += 45;
			} else {
				heading -= 45;
			}
			trackPosition.setHeading(heading % 360);
		} else {
			if (this.random.nextInt(100) >= 99) {
				int heading = getRandomValue(trackPosition.getHeading(), this.maxHeadingDelta);
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
		return getUpdateIntervalAware().getUpdateInterval();
	}

	/**
	 * @return the updateInterval
	 */
	public int getTrackAmountPerInterval() {
		return getUpdateIntervalAware().getTrackAmount();
	}

	/**
	 * @return the current update interval aware instance
	 */
	public TrackGeneratorIntervalAware getUpdateIntervalAware() {
		if (this.useVariableInterval) {
			return this.variableGeneratorInterval;
		} else {
			return this.fixGeneratorInterval;
		}
	}

	/**
	 * @return the producerTemplate
	 */
	public ProducerTemplate getProducerTemplate() {
		return this.producerTemplate;
	}

	/**
	 * @param producerTemplate
	 *            the producerTemplate to set
	 */
	public void setProducerTemplate(ProducerTemplate producerTemplate) {
		this.producerTemplate = producerTemplate;
	}

	/**
	 * @return the minLatitude
	 */
	public double getMinLatitude() {
		return this.minLatitude;
	}

	/**
	 * @param minLatitude
	 *            the minLatitude to set
	 */
	public void setMinLatitude(double minLatitude) {
		this.minLatitude = minLatitude;
	}

	/**
	 * @return the maxLatitude
	 */
	public double getMaxLatitude() {
		return this.maxLatitude;
	}

	/**
	 * @param maxLatitude
	 *            the maxLatitude to set
	 */
	public void setMaxLatitude(double maxLatitude) {
		this.maxLatitude = maxLatitude;
	}

	/**
	 * @return the minLongitude
	 */
	public double getMinLongitude() {
		return this.minLongitude;
	}

	/**
	 * @param minLongitude
	 *            the minLongitude to set
	 */
	public void setMinLongitude(double minLongitude) {
		this.minLongitude = minLongitude;
	}

	/**
	 * @return the maxLongitude
	 */
	public double getMaxLongitude() {
		return this.maxLongitude;
	}

	/**
	 * @param maxLongitude
	 *            the maxLongitude to set
	 */
	public void setMaxLongitude(double maxLongitude) {
		this.maxLongitude = maxLongitude;
	}

	/**
	 * @return the maxAltitudeDelta
	 */
	public int getMaxAltitudeDelta() {
		return this.maxAltitudeDelta;
	}

	/**
	 * @param maxAltitudeDelta
	 *            the maxAltitudeDelta to set
	 */
	public void setMaxAltitudeDelta(int maxAltitudeDelta) {
		this.maxAltitudeDelta = maxAltitudeDelta;
	}

	/**
	 * @return the maxSpeedDelta
	 */
	public int getMaxSpeedDelta() {
		return this.maxSpeedDelta;
	}

	/**
	 * @param maxSpeedDelta
	 *            the maxSpeedDelta to set
	 */
	public void setMaxSpeedDelta(int maxSpeedDelta) {
		this.maxSpeedDelta = maxSpeedDelta;
	}

	/**
	 * @return the amountOfTracks
	 */
	public int getAmountOfTracks() {
		return this.amountOfTracks;
	}

	/**
	 * @param amountOfTracks
	 *            the amountOfTracks to set
	 */
	public void setAmountOfTracks(int amountOfTracks) {
		this.amountOfTracks = amountOfTracks;
	}

	/**
	 * @return the maxHeadingDelta
	 */
	public int getMaxHeadingDelta() {
		return this.maxHeadingDelta;
	}

	/**
	 * @param maxHeadingDelta
	 *            the maxHeadingDelta to set
	 */
	public void setMaxHeadingDelta(int maxHeadingDelta) {
		this.maxHeadingDelta = maxHeadingDelta;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the fixGeneratorInterval
	 */
	public TrackGeneratorIntervalAware getFixGeneratorInterval() {
		return this.fixGeneratorInterval;
	}

	/**
	 * @param fixGeneratorInterval
	 *            the fixGeneratorInterval to set
	 */
	public void setFixGeneratorInterval(TrackGeneratorIntervalAware fixGeneratorInterval) {
		this.fixGeneratorInterval = fixGeneratorInterval;
	}

	/**
	 * @return the variableGeneratorInterval
	 */
	public TrackGeneratorIntervalAware getVariableGeneratorInterval() {
		return this.variableGeneratorInterval;
	}

	/**
	 * @param variableGeneratorInterval
	 *            the variableGeneratorInterval to set
	 */
	public void setVariableGeneratorInterval(TrackGeneratorIntervalAware variableGeneratorInterval) {
		this.variableGeneratorInterval = variableGeneratorInterval;
	}

	/**
	 * @return the useVariableInterval
	 */
	public boolean isUseVariableInterval() {
		return this.useVariableInterval;
	}

	/**
	 * @param useVariableInterval
	 *            the useVariableInterval to set
	 */
	public void setUseVariableInterval(boolean useVariableInterval) {
		this.useVariableInterval = useVariableInterval;
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
			while (RandomTrackGenerator.this.generating) {
				try {
					int updateDuration = getUpdateInterval();
					if (LOGGER.isInfoEnabled()) {
						LOGGER.info("Tracks send - sleeping for {}s", updateDuration / 1000);
					}
					if (updateDuration > 0) {
						Thread.sleep(updateDuration);
					}

					for (TrackPositionMessage trackPosition : RandomTrackGenerator.this.trackPositions) {
						updateTrackPosition(trackPosition);
					}

					int trackAmountPerInterval = getTrackAmountPerInterval();
					for (int i = 0; i < trackAmountPerInterval; i++) {
						TrackPositionMessage trackPosition = RandomTrackGenerator.this.trackPositions.get(RandomTrackGenerator.this.currentTrack);
						RandomTrackGenerator.this.producerTemplate.sendBody(removeSomeProperties(trackPosition));
						RandomTrackGenerator.this.currentTrack = ++RandomTrackGenerator.this.currentTrack % RandomTrackGenerator.this.amountOfTracks;
					}
				} catch (InterruptedException e) {
					LOGGER.debug("Thread has been interrupted", e);
				} catch (Exception e) {
					LOGGER.warn("General Exception", e);
				}
			}
		}

		/**
		 * Does clone the given {@link TrackPositionMessage} and remove some
		 * random properties
		 *
		 * @param trackPosition
		 *            the trackPosition
		 * @return a clone of the given trackPosition with less properties
		 */
		private TrackPositionMessage removeSomeProperties(TrackPositionMessage trackPosition) {
			TrackPositionMessage trackPositionClone = SerializationUtils.clone(trackPosition);

			if (doRemoveProperty()) {
				trackPositionClone.setGeometry(null);
			}
			if (doRemoveProperty()) {
				trackPositionClone.setHeading(null);
			}
			if (doRemoveProperty()) {
				trackPositionClone.setAltitude(null);
			}
			if (doRemoveProperty()) {
				trackPositionClone.setCallSign(null);
			}
			return trackPositionClone;
		}

		/**
		 * @return does randomly return true
		 */
		private boolean doRemoveProperty() {
			return RandomTrackGenerator.this.random.nextInt(5) >= 4;
		}
	}

}
