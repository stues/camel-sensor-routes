package ch.stue.transmitter.sos.converter.insertobservation;

import java.io.InputStream;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.stue.transmitter.sos.converter.SOSConfiguration;

/**
 * Default Insert observation sos v2.0 configuration
 *
 * @author stue
 */
public class SOSConfigurationFactory implements FactoryBean<SOSConfiguration>, ResourceLoaderAware {

	private ResourceLoader resourceLoader;

	private String[] jsonConfig;

	/**
	 * @param resourceLoader
	 *            the resourceLoader to set
	 */
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	/**
	 * @return the resourceLoader
	 */
	public Resource getResource(String location) {
		return this.resourceLoader.getResource(location);
	}

	/**
	 * @return the jsonConfig
	 */
	public String[] getJsonConfig() {
		return this.jsonConfig;
	}

	/**
	 * @param jsonConfig
	 *            the jsonConfig to set
	 */
	public void setJsonConfig(String... jsonConfig) {
		this.jsonConfig = jsonConfig;
	}

	@Override
	public SOSConfiguration getObject() throws Exception {
		SOSConfiguration sosConfiguration = new SOSConfiguration();
		if (this.jsonConfig != null && this.jsonConfig.length > 0) {
			for (String jsonConfigResource : this.jsonConfig) {
				Resource resource = getResource(jsonConfigResource);
				InputStream inputStream = resource.getInputStream();
				ObjectMapper mapper = new ObjectMapper();
				DefaultInsertObservationSOSV2Configuration sosV2Configuration = mapper.readValue(inputStream, DefaultInsertObservationSOSV2Configuration.class);
				sosV2Configuration.afterPropertiesSet();
				sosConfiguration.addConfiguration(sosV2Configuration);
			}
		}
		return sosConfiguration;
	}

	@Override
	public Class<SOSConfiguration> getObjectType() {
		return SOSConfiguration.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}