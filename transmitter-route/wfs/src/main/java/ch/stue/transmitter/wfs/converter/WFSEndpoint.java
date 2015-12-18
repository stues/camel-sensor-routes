package ch.stue.transmitter.wfs.converter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;
import org.apache.commons.lang3.StringUtils;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.filter.identity.FeatureIdImpl;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;

public class WFSEndpoint extends DefaultEndpoint {

	private static final String DEFAULT_WFS_VERSION = "2.0.0";

	private URI uri;

	@UriPath(defaultValue = "127.0.0.1")
	private String host;

	@UriPath(defaultValue = "80")
	private Integer port;

	@UriPath
	@Metadata(required = "true")
	private final String resourceUri;

	@UriPath(defaultValue = DEFAULT_WFS_VERSION)
	private String wfsVersion;

	@UriParam
	private String workspace;

	@UriParam
	@Metadata(required = "true")
	private String layer;

	private DataStore dataStore;

	private SimpleFeatureType schema;

	private SimpleFeatureStore featureStore;

	public WFSEndpoint(WFSComponent component, String uri, String resourceUri, Map<String, Object> parameters) {
		super(uri, component);
		this.resourceUri = resourceUri;
		try {
			this.uri = new URI(uri);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public Producer createProducer() throws Exception {
		return new WFSProducer(this);
	}

	@Override
	public Consumer createConsumer(Processor processor) throws Exception {
		return null;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	/**
	 * Connects the service to the given WFS
	 *
	 * @param producer
	 *            the wfs producer
	 */
	public void connect(WFSProducer producer) {
		if (this.dataStore == null) {
			String serviceUrl = String.format("http://%s:%d%s", this.host, this.port, this.uri.getPath());
			String requestUrl = String.format("%s?service=WFS&version=%s&request=GetCapabilities", serviceUrl, getWfsVersion());
			String featureName = getFeatureName(this.workspace, this.layer);

			Map<String, Object> connectionParameters = Collections.<String, Object> singletonMap("WFSDataStoreFactory:GET_CAPABILITIES_URL", requestUrl);
			try {
				this.dataStore = DataStoreFinder.getDataStore(connectionParameters);
				this.schema = this.dataStore.getSchema(featureName);
				this.featureStore = (SimpleFeatureStore) this.dataStore.getFeatureSource(featureName);
			} catch (IOException e) {
				throw new RuntimeCamelException(String.format("Error connecting to WFS (%s) and retrieve Feature (%s)", requestUrl, featureName), e);
			}
		}
	}

	/**
	 * Create a feature within the WFS for the given valueMap
	 *
	 * @param valueMap mapping of property name to value
	 */
	public void insertFeature(Map<?, ?> valueMap) {
		List<AttributeDescriptor> attributeDescriptors = this.schema.getAttributeDescriptors();
		Object[] attributes = new Object[attributeDescriptors.size()];
		for (int i = 0; i < attributeDescriptors.size(); i++) {
			Name attributeName = attributeDescriptors.get(i).getName();
			if (valueMap.containsKey(attributeName.getLocalPart())) {
				attributes[i] = valueMap.get(attributeName.getLocalPart());
			}
		}

		SimpleFeature simpleFeature = new SimpleFeatureImpl(attributes, this.schema, new FeatureIdImpl("id"), false);

		Transaction transaction = new DefaultTransaction("insert");
		try {
			this.featureStore.setTransaction(transaction);
			this.featureStore.addFeatures(DataUtilities.collection(simpleFeature));
			transaction.commit();
		} catch (IOException e) {
			throw new RuntimeCamelException(String.format("An Error occurred while inserting a feature (%s)", simpleFeature), e);
		} finally {
			try {
				transaction.close();
			} catch (IOException e) {
				throw new RuntimeCamelException("An Error occurred while closing the transaction", e);
			}
		}
	}

	/**
	 * Connects the service to the given WFS
	 *
	 * @param producer
	 *            the wfs producer
	 */
	public void disconnect(WFSProducer producer) {
		if (this.dataStore != null) {
			this.dataStore.dispose();
			this.dataStore = null;
			this.schema = null;
			this.featureStore = null;
		}
	}

	/**
	 * Returns the feature name for the given workspace and layerName
	 *
	 * @param workspace
	 *            the workspace
	 * @param layerName
	 *            the layer name
	 * @return the feature name
	 */
	private static String getFeatureName(String workspace, String layerName) {
		StringBuilder stringBuilder = new StringBuilder();
		if (StringUtils.isNotEmpty(workspace)) {
			stringBuilder.append(workspace);
			stringBuilder.append('_');
		}
		stringBuilder.append(layerName);
		return stringBuilder.toString();
	}

	/**
	 * @return the URI
	 */
	public URI getUri() {
		return this.uri;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return this.host;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public Integer getPort() {
		return this.port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(Integer port) {
		this.port = port;
	}

	/**
	 * @return the wfsVersion
	 */
	public String getWfsVersion() {
		return this.wfsVersion == null ? DEFAULT_WFS_VERSION : this.wfsVersion;
	}

	/**
	 * @param wfsVersion
	 *            the wfsVersion to set
	 */
	public void setWfsVersion(String wfsVersion) {
		this.wfsVersion = wfsVersion;
	}

	/**
	 * @return the workspace
	 */
	public String getWorkspace() {
		return this.workspace;
	}

	/**
	 * @param workspace
	 *            the workspace to set
	 */
	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}

	/**
	 * @return the layer
	 */
	public String getLayer() {
		return this.layer;
	}

	/**
	 * @param layer the layer to set
	 */
	public void setLayer(String layer) {
		this.layer = layer;
	}

	/**
	 * @param uri
	 *            the uri to set
	 */
	public void setUri(URI uri) {
		this.uri = uri;
	}

	/**
	 * @return the resourceUri
	 */
	public String getResourceUri() {
		return this.resourceUri;
	}

	/**
	 * @return the protocol
	 */
	public String getProtocol() {
		return this.uri.getScheme();
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return this.uri.getPath();
	}
}
