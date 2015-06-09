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
	
	@Override
	public void start(final RandomTrackGenerator randomTrackGenerator) {
		if(isConfigured()){
			if(initialDelay > 0){
				LOGGER.info("Initial Delay is set to {} ms", initialDelay);
				Timer timer = new Timer();
		        timer.schedule(new TimerTask() {

					@Override
					public void run() {
						LOGGER.info("Initial Delay is over start Random Track Generation");
						doStartInterval(randomTrackGenerator);
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
	
}
