package ch.stue.receiver.sbs1.generator;


/**
 * This {@link TrackGeneratorIntervalAware} defines a Fix update interval
 * 
 * @author stue
 * 
 */
public class FixGeneratorInterval extends AbstractGeneratorInterval {

	private int updateInterval = -1;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getUpdateInterval() {
		return updateInterval;
	}

	@Override
	public boolean isConfigured() {
		return updateInterval >= 0;
	}
	
	/**
	 * @param updateInterval the updateInterval to set
	 */
	public void setUpdateInterval(int updateInterval) {
		this.updateInterval = updateInterval;
	}
	
	/**
	 * Start Track generation immediately
	 */
	@Override
	public void doStartInterval(RandomTrackGenerator randomTrackGenerator) {
		randomTrackGenerator.generateTracks();
	}
}
