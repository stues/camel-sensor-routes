package ch.stue.transmitter.wfs.component;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultProducer;
import org.apache.commons.beanutils.BeanMap;

/**
 * The WFS insert transaction Producer
 *
 * @author stue
 *
 */
public class WFSProducer extends DefaultProducer {

	/**
	 * Constructor with WFS-Endpoint
	 *
	 * @param endpoint
	 *            the endpoint
	 */
	public WFSProducer(WFSEndpoint endpoint) {
		super(endpoint);
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		Message in = exchange.getIn();
		Object message = in.getMandatoryBody();
		//		if (!(message == null || message instanceof Map)) {
		//			message = in.getMandatoryBody(Map.class);
		//		}
		Map<?, ?> valueMap;
		if (message instanceof Map) {
			valueMap = (Map<?,?>)message;
		}
		else {
			valueMap = new BeanMap(message);
		}

		getEndpoint().insertFeature(valueMap);
	}

	@Override
	public WFSEndpoint getEndpoint() {
		return (WFSEndpoint) super.getEndpoint();
	}

	@Override
	public void doStart() throws Exception {
		super.doStart();
		getEndpoint().connect(this);
	}

	@Override
	public void doStop() throws Exception {
		getEndpoint().disconnect(this);
		super.doStop();
	}
}