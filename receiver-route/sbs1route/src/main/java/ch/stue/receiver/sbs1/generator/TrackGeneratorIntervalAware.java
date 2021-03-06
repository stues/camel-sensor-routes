package ch.stue.receiver.sbs1.generator;

/**
 * This interface describes Classes which can be asked for a update interval
 * duration
 * 
 * @author stue
 * 
 */
public interface TrackGeneratorIntervalAware {

	/**
	 * @return the time to wait between generating a new track 
	 */
	public int getUpdateInterval();

	/**
	 * @return the amount of tracks which shall be generated during the current interval
	 */
	public int getTrackAmount();
	
	/**
	 * @return whether this interval is configured or not;
	 */
	public boolean isConfigured();
	
	/**
	 * Is called by the Track Generator to notify the interval to start
	 */
	public void start(RandomTrackGenerator randomTrackGenerator);
}
