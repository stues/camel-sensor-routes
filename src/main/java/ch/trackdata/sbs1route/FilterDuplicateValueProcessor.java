package ch.trackdata.sbs1route;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import ch.trackdata.sbs1route.message.GeoJSONFeature;

/**
 * Checks whether a previous message contained the same value if so, the property will be removed
 * 
 * @author stue
 */
public class FilterDuplicateValueProcessor implements Processor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FilterDuplicateValueProcessor.class);

	private Map<Object, GeoJSONFeature<?>> cachedFeatures;
	
	private Timer cleanupTimer;

	private Integer cleanupInterval;
	
	private String idProperty;
	private String comparableDateProperty;
	
	/**
	 * Default Constructor
	 */
	public FilterDuplicateValueProcessor() {
		cachedFeatures = new ConcurrentHashMap<Object, GeoJSONFeature<?>>();
		
		cleanupTimer = new Timer();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void process(Exchange exchange) throws Exception {
		GeoJSONFeature<?> feature = exchange.getIn().getBody(GeoJSONFeature.class);
		
		feature = getUpdatedFeature(feature);
				
		LOGGER.info("Updated message: {}", feature);
		
		exchange.getOut().setBody(feature);
	}

	/**
	 * Checks whether the feature with the given id and time is already cached
	 * if so all properties which are equal will be removed
	 * otherwise the property will be added to the cache
	 * @param feature the feature to update
	 * @return the updated feature
	 */
	private GeoJSONFeature<?> getUpdatedFeature(GeoJSONFeature<?> feature) {
		Object id = feature.getProperty(idProperty, Object.class);
		GeoJSONFeature<?> cachedFeature = cachedFeatures.get(id);
		if(cachedFeature != null){
			Date featureComparable = feature.getProperty(comparableDateProperty, Date.class);
			Date cachedComparable = cachedFeature.getProperty(comparableDateProperty, Date.class);
			if(featureComparable.compareTo(cachedComparable) == 0){
				feature = updateFeature(feature, cachedFeature);	
			}
			else{
				cachedFeatures.put(id, feature);
			}	
		}
		else{
			cachedFeatures.put(id, feature);
		}
		
		return feature;
	}

	/**
	 * Removes all the Properties from the feature which are already contained within the cachedFeature
	 * @param feature the feature
	 * @param cachedFeature the cached feature
	 * @return the updated feature
	 */
	private GeoJSONFeature<?> updateFeature(GeoJSONFeature<?> feature, GeoJSONFeature<?> cachedFeature) {

		Map<String, Object> featureProperties = feature.getProperties();
		Map<String, Object> cachedProperties = cachedFeature.getProperties();
		
		for(String property : cachedProperties.keySet()){
			if(!(property.equals(idProperty) || property.equals(comparableDateProperty))){
				if(featureProperties.containsKey(property) 
						&& featureProperties.get(property).equals(cachedProperties.get(property))){
					featureProperties.remove(property);
				}
			}
		}
		
		for(String property : featureProperties.keySet()){
			if(!(property.equals(idProperty) || property.equals(comparableDateProperty))){
				cachedProperties.put(property, featureProperties.get(property));
			}
		}
		
		return feature;
	}

	/**
	 * Removes old Features from the cache which exeed the cleanupInterval-Time
	 */
	private void cleanup() {
		LOGGER.info("Removing Old Features({})", cachedFeatures.size());
		Iterator<Entry<Object, GeoJSONFeature<?>>> iterator = cachedFeatures.entrySet().iterator();
		Date oldestDate = DateUtils.addMilliseconds(new Date(), -cleanupInterval);
		while(iterator.hasNext()){
			Entry<Object, GeoJSONFeature<?>> featureEntry  = iterator.next();
			GeoJSONFeature<?> feature = featureEntry.getValue();
			if(oldestDate.after(feature.getProperty(comparableDateProperty, Date.class))){
				iterator.remove();
				LOGGER.info("Old cached feature removed: {}", feature);
			}
		}
		
		LOGGER.info("Old feature removed: ({})", cachedFeatures.size());
	}

	/**
	 * @return the cleanupInterval
	 */
	public Integer getCleanupInterval() {
		return cleanupInterval;
	}

	/**
	 * @param cleanupInterval the cleanupInterval to set
	 */
	@Required
	public void setCleanupInterval(Integer cleanupInterval) {
		this.cleanupInterval = cleanupInterval;
		startTimer();
	}

	/**
	 * Starts the cleanup Timer
	 */
	private void startTimer() {
		cleanupTimer.schedule(new TimerTask(){

			@Override
			public void run() {
				cleanup();
			}}, cleanupInterval,cleanupInterval);
	}

	/**
	 * @return the idProperty
	 */
	public String getIdProperty() {
		return idProperty;
	}

	/**
	 * @param idProperty the idProperty to set
	 */
	public void setIdProperty(String idProperty) {
		this.idProperty = idProperty;
	}

	/**
	 * @return the comparableDateProperty
	 */
	public String getComparableDateProperty() {
		return comparableDateProperty;
	}

	/**
	 * @param comparableDateProperty the comparableDateProperty to set
	 */
	public void setComparableDateProperty(String comparableDateProperty) {
		this.comparableDateProperty = comparableDateProperty;
	}
	
}
