package ch.stue.transmitter.sos.converter.insertobservation;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Configuration can be used to Describe a InsertObservation
 * 
 * @author stue
 *
 */
public class InsertObservationSOSV2Configuration implements ResourceLoaderAware, InitializingBean {

	private static final String SERVICE_NAME = "SOS";

	private static final String SOS_VERSION = "2.0.0";
	
	private List<? extends ObservedPropertyConfiguration<?>> observedProperties;
	
	private List<String> offerings;
	
	private ResourceLoader resourceLoader;
	
	private String jsonConfig;
	
	/**
	 * @return the service
	 */
	public String getService(){
		return SERVICE_NAME;
	}
	
	/**
	 * @return the version
	 */
	public String getVersion(){
		return SOS_VERSION;
	}
	
	/**
	 * @return the observedProperties
	 */
	public List<? extends ObservedPropertyConfiguration<?>> getObservedProperties() {
		return observedProperties;
	}

	/**
	 * @param observedProperties the observedProperties to set
	 */
	public void setObservedProperties(
			List<? extends ObservedPropertyConfiguration<?>> observedProperties) {
		this.observedProperties = observedProperties;
	}

	/**
	 * @return the offerings
	 */
	public List<String> getOfferings() {
		return offerings;
	}

	/**
	 * @param offerings the offerings to set
	 */
	public void setOfferings(List<String> offerings) {
		this.offerings = offerings;
	}
	
	/**
	 * @param resourceLoader the resourceLoader to set
	 */
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	/**
	 * @return the resourceLoader
	 */
	public Resource getResource(String location){
		return resourceLoader.getResource(location);
	}
	
	/**
	 * @return the jsonConfig
	 */
	public String getJsonConfig() {
		return jsonConfig;
	}

	/**
	 * @param jsonConfig the jsonConfig to set
	 */
	public void setJsonConfig(String jsonConfig) {
		this.jsonConfig = jsonConfig;
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		Resource resource = getResource(getJsonConfig());
		InputStream inputStream = resource.getInputStream();
		ObjectMapper mapper = new ObjectMapper();
		AbstractPropertyConfiguration<?>[] propertyConfiguration = mapper.readValue(inputStream, AbstractPropertyConfiguration[].class);
		if(propertyConfiguration != null){
			setObservedProperties(Arrays.asList(propertyConfiguration));
		}
	}
}
