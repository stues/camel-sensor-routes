package ch.trackdata.sbs1route;

import org.apache.camel.CamelContext;
import org.apache.camel.StartupListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * This {@link StartupListener} waits until the given context is fully loaded
 * once the context is loaded, the {@link SBS1Scanner} will be started
 */
public class CamelStartupListener implements StartupListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(CamelStartupListener.class);

	private SBS1Scanner sbs1Scanner;

	private String camelContextName;

	/**
	 * @param sbs1Scanner
	 *            the sbs1Scanner to set
	 */
	@Required
	public void setSbs1Scanner(SBS1Scanner sbs1Scanner) {
		this.sbs1Scanner = sbs1Scanner;
	}

	/**
	 * @param camelContextName
	 *            the camelContextName to set
	 */
	public void setCamelContextName(String camelContextName) {
		this.camelContextName = camelContextName;
	}

	/**
	 * @param context
	 *            the camel context to set
	 */
	@Required
	public void setCamelContext(CamelContext context) {
		try {
			setCamelContextName(context.getName());
			context.addStartupListener(this);
		} catch (Exception e) {
			LOGGER.warn("Add as startup listener failed.", e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Called once the context started
	 */
	@Override
	public void onCamelContextStarted(CamelContext context, boolean alreadyStarted) throws Exception {
		LOGGER.info("CamelStartupListener will start the SBS1 scanner, camelContext: {}, alreadyStarted: {}", context, alreadyStarted);
		if (camelContextName.equals(context.getName()) && !alreadyStarted) {
			sbs1Scanner.launchScanner();
		} else {
			LOGGER.info("Wrong context or already started.");
		}
	}
}
