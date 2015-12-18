package ch.stue.transmitter.sos.converter.insertobservation;

import java.io.InputStream;
import java.util.Arrays;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * InsertObservation Configuration which can be configured by a JSON-File
 * 
 * @author stue
 *
 */
public class JsonInsertObservationSOSV2Configuration extends DefaultInsertObserrvationSOSV2Configuration implements ResourceLoaderAware, InitializingBean {

	private ResourceLoader resourceLoader;
	
	private String jsonConfig;
	
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
