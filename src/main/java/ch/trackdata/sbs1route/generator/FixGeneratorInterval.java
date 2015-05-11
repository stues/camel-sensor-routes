package ch.trackdata.sbs1route.generator;

import org.springframework.beans.factory.InitializingBean;

/**
 * This interface describes Classes which can be asked for a update interval
 * duration
 * 
 * @author stue
 * 
 */
public class FixGeneratorInterval implements TrackGeneratorIntervalAware, InitializingBean {

	private int updateInterval = -1;
	
	private RandomTrackGenerator randomTrackGenerator;

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
	 * @return the randomTrackGenerator
	 */
	public RandomTrackGenerator getRandomTrackGenerator() {
		return randomTrackGenerator;
	}

	/**
	 * @param randomTrackGenerator the randomTrackGenerator to set
	 */
	public void setRandomTrackGenerator(RandomTrackGenerator randomTrackGenerator) {
		this.randomTrackGenerator = randomTrackGenerator;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
//		randomTrackGenerator.setEnabled(true);
	}
}
