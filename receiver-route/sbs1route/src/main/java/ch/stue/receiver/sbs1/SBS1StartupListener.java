package ch.stue.receiver.sbs1;

import org.apache.camel.CamelContext;
import org.apache.camel.StartupListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import ch.stue.common.context.StartRoutesStartupListener;

/**
 * This {@link StartupListener} waits until the given context is fully loaded
 * once the context is loaded, the {@link SBS1DataReceiver} will be started
 */
public class SBS1StartupListener extends StartRoutesStartupListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(SBS1StartupListener.class);

	private SBS1DataReceiver sbs1DataReceiver;

	/**
	 * @param sbs1DataReceiver
	 *            the sbs1DataReceiver to set
	 */
	@Required
	public void setSbs1DataReceiver(SBS1DataReceiver sbs1DataReceiver) {
		this.sbs1DataReceiver = sbs1DataReceiver;
	}

	@Override
	protected void contextStarted(CamelContext context) {
		super.contextStarted(context);

		LOGGER.info("SBS1 routes started now the SBS1 data receiver will be started");
		sbs1DataReceiver.launchDataReceiver();
	}
}
