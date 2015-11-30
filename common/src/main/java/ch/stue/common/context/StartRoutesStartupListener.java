package ch.stue.common.context;


import java.util.Arrays;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.StartupListener;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This {@link StartupListener} waits until the given context is fully loaded
 * once the context is loaded, the {@link SBS1DataReceiver} will be started
 */
public class StartRoutesStartupListener extends AbstractCamelStartupListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(StartRoutesStartupListener.class);

	private List<String> routeNames;

	private boolean startRoutes;
	
	@Override
	protected void contextStarted(CamelContext context) {
		LOGGER.info("Context Start: {}", context.getName());
		if(CollectionUtils.isNotEmpty(routeNames)){
			for(String routeName : routeNames){
				LOGGER.info("Starting route {}", routeName);
				try {
					context.startRoute(routeName);
				} catch (Exception e) {
					LOGGER.warn("An Error occurred while starting route", e);
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void afterPropertiesSet() {
		if(startRoutes
				&& CollectionUtils.isNotEmpty(routeNames)){
			super.afterPropertiesSet();
		}
	}
	
	/**
	 * @return the routeNames
	 */
	public List<String> getRouteNames() {
		return routeNames;
	}

	/**
	 * @param routeNames the routeNames to set
	 */
	public void setRouteNames(List<String> routeNames) {
		this.routeNames = routeNames;
	}
	
	/**
	 * @param routeName the route Name
	 */
	public void setRouteName(String routeName){
		this.routeNames = Arrays.asList(routeName);
	}
	
	/**
	 * @return the startRoutes
	 */
	public boolean isStartRoutes() {
		return startRoutes;
	}

	/**
	 * @param startRoutes the startRoutes to set
	 */
	public void setStartRoutes(boolean startRoutes) {
		this.startRoutes = startRoutes;
	}
	
	/**
	 * @param startRoutes the startRoutes to set
	 */
	public void setStartRoutes(List<Boolean> startRoutes) {
		this.startRoutes = isEnabled(startRoutes);
	}

	/**
	 * Checks all values of the given list depending on the value true or false will be returned
	 * @param startRoutes a list with booleans to check
	 * @return returns true if all given booleans are true otherwise false
	 */
	private boolean isEnabled(List<Boolean> startRoutes) {
		if(CollectionUtils.isNotEmpty(startRoutes)){
			for(Boolean startRoute : startRoutes){
				if(!startRoute){
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
