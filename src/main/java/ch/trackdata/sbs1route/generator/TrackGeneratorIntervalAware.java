package ch.trackdata.sbs1route.generator;

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
	 * @return whether this interval is configured or not;
	 */
	public boolean isConfigured();
}
