package ch.trackdata.sbs1route.generator;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This abstract class contains methods to initial delay a interval 
 * 
 * @author stue
 * 
 */
public abstract class AbstractGeneratorInterval implements TrackGeneratorIntervalAware {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractGeneratorInterval.class);
	
	private int initialDelay;
	
	private int duration;
	
	protected Timer timer;
	
	private int trackAmount;
	
	public AbstractGeneratorInterval(){
		timer = new Timer();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start(final RandomTrackGenerator randomTrackGenerator) {
		if(isConfigured()){
			if(initialDelay > 0){
				LOGGER.info("Initial Delay is set to {} ms", initialDelay);
		        timer.schedule(new TimerTask() {

					@Override
					public void run() {
						startInterval(randomTrackGenerator);
					}
				}, initialDelay);
			}
			else{
				LOGGER.info("No initial Delay is configured start Random Track Generation immediately");
				doStartInterval(randomTrackGenerator);
			}
		}
		else if(LOGGER.isInfoEnabled()){
			LOGGER.info("Interval is not configured");
		}
	}

	/**
	 * Starts the interval on the given {@link RandomTrackGenerator}
	 * @param randomTrackGenerator the random track generator
	 */
	private void startInterval(final RandomTrackGenerator randomTrackGenerator) {
		LOGGER.info("Initial Delay is over start Random Track Generation");
		if(duration > 0){
			if(LOGGER.isInfoEnabled()){
				LOGGER.info("Random Track Generator will be disconnected after {}ms", duration);
			}
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					if(LOGGER.isInfoEnabled()){
						LOGGER.info("Disconnect Random Track Generator");
					}
					randomTrackGenerator.disconnectSource();
				}
			}, duration);
		}
		doStartInterval(randomTrackGenerator);
	}
	
	/**
	 * is called after the initial delay has been  
	 * @param randomTrackGenerator
	 * @return 
	 */
	public abstract void doStartInterval(RandomTrackGenerator randomTrackGenerator);

	/**
	 * @return the initialDelay
	 */
	public int getInitialDelay() {
		return initialDelay;
	}

	/**
	 * @param initialDelay the initialDelay to set
	 */
	public void setInitialDelay(int initialDelay) {
		this.initialDelay = initialDelay;
	}

	/**
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * @return the trackAmount
	 */
	public int getTrackAmount() {
		return trackAmount;
	}

	/**
	 * @param trackAmount the trackAmount to set
	 */
	public void setTrackAmount(int trackAmount) {
		this.trackAmount = trackAmount;
	}
}
