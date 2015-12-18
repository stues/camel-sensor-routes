package ch.stue.transmitter.wfs.converter;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;

/**
 * A WFS Component for Camel
 *
 * @author stue
 */
public class WFSComponent extends UriEndpointComponent {

	protected Integer port = 80;

	protected String host = "127.0.0.1";

	/**
	 * Default Constructor
	 */
	public WFSComponent() {
		super(WFSEndpoint.class);
	}

	@Override
	protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {

		int port = extractPortNumber(remaining);
		String host = extractHostName(remaining);

		WFSEndpoint wfsEndpoint = new WFSEndpoint(this, uri, remaining, parameters);
		wfsEndpoint.setPort(port);
		wfsEndpoint.setHost(host);

		setProperties(wfsEndpoint, parameters);

		return wfsEndpoint;
	}

	/**
	 * Extracts the port number from the given string
	 * @param remaining the remaining URI string
	 * @return the extracted port number
	 */
	private int extractPortNumber(String remaining) {
		int index1 = remaining.indexOf(":");
		int index2 = remaining.indexOf("/");

		if ((index1 != -1) && (index2 != -1)) {
			String result = remaining.substring(index1 + 1, index2);
			return Integer.parseInt(result);
		} else {
			return this.port;
		}
	}

	/**
	 * Extracts the host name from the given string
	 * @param remaining the remaining URI string
	 * @return the extracted host name
	 */
	private String extractHostName(String remaining) {
		int index = remaining.indexOf(":");
		if (index != -1) {
			return remaining.substring(0, index);
		} else {
			return this.host;
		}
	}
}
