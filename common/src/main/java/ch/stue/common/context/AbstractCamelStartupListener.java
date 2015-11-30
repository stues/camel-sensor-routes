package ch.stue.common.context;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.StartupListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

/**
 * This {@link StartupListener} waits until the given context is fully loaded
 * once the context is loaded, the {@link #contextStarted} method will be called
 */
public abstract class AbstractCamelStartupListener implements StartupListener, CamelContextAware, InitializingBean {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCamelStartupListener.class);

	private CamelContext context;

	/**
	 * Will be called when context has been started
	 * 
	 * @param context
	 *            the started context
	 */
	protected abstract void contextStarted(CamelContext context);

	/**
	 * @param context
	 *            the camel context to set
	 */
	@Required
	public void setCamelContext(CamelContext context) {
		this.context = context;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Called once the context started
	 */
	@Override
	public void onCamelContextStarted(CamelContext context, boolean alreadyStarted) throws Exception {
		LOGGER.info("CamelStartupListener will start the SBS1 data receiver, camelContext: {}, alreadyStarted: {}", context, alreadyStarted);
		if (this.context.equals(context)) {
			contextStarted(context);
		} else {
			LOGGER.info("Wrong context.");
		}
	}

	@Override
	public CamelContext getCamelContext() {
		return context;
	}

	/**
	 * Called after all the properties are set by spring
	 */
	public void afterPropertiesSet() {
		try {
			context.addStartupListener(this);
		} catch (Exception e) {
			LOGGER.warn("Add as startup listener failed.", e);
		}
	}

}
