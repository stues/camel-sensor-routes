package ch.stue.receiver.sbs1;

import org.apache.camel.CamelContext;
import org.apache.camel.StartupListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import ch.stue.common.context.AbstractCamelStartupListener;
import ch.stue.receiver.sbs1.generator.RandomTrackGenerator;

/**
 * This {@link StartupListener} waits until the given context is fully loaded
 * once the context is loaded, the {@link RandomTrackGenerator} will be started
 */
public class SBS1GeneratorStartupListener extends AbstractCamelStartupListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(SBS1GeneratorStartupListener.class);

	private RandomTrackGenerator randomTrackGenerator;

	private boolean startGenerator;
	
	@Override
	protected void contextStarted(CamelContext context) {
		LOGGER.info("SBS1 generator routes started now Track Generator will be started");
		randomTrackGenerator.startGenerator();
	}
	
	/**
	 * @param randomTrackGenerator the randomTrackGenerator to set
	 */
	@Required
	public void setRandomTrackGenerator(RandomTrackGenerator randomTrackGenerator) {
		this.randomTrackGenerator = randomTrackGenerator;
	}

	/**
	 * @param startGenerator the startGenerator to set
	 */
	public void setStartGenerator(boolean startGenerator) {
		this.startGenerator = startGenerator;
	}
	
	@Override
	public void afterPropertiesSet() {
		if(startGenerator){
			super.afterPropertiesSet();
		}
	}
}
