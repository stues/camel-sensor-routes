package ch.trackdata.demo;

import org.apache.camel.CamelContext;
import org.apache.camel.StartupListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class CamelStartupListener implements StartupListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(CamelStartupListener.class);
	
	private TrafficScanner trafficScanner;

	/**
	 * @param trafficScanner the trafficScanner to set
	 */
	@Required
	public void setTrafficScanner(TrafficScanner trafficScanner) {
		this.trafficScanner = trafficScanner;
	}

	@Required
	public void setCamelContext (CamelContext context) {
        try {
			context.addStartupListener (this);
		} 
        catch (Exception e) {
			LOGGER.warn("Add as startup listener failed.", e);
		}
	}
	
	@Override
	public void onCamelContextStarted(CamelContext context,
			boolean alreadyStarted) throws Exception {
		LOGGER.info("CamelStartupListener will start the traffic scanner, camelContext: {}, alreadyStarted: {}", context, alreadyStarted);
		if ("demoCamelContext".equals(context.getName()) && !alreadyStarted) {
			trafficScanner.launchScanner();
		}
		else {
			LOGGER.info("Wrong context or already started.");
		}
	}
}
