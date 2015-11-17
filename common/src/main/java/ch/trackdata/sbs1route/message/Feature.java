package ch.trackdata.sbs1route.message;

import java.util.Map;

public interface Feature<T extends Geometry<?>> {
	
	public T getGeometry();
	
	public Map<String, Object> getProperties();
	
	public abstract <O> O getProperty(String propertyName, Class<O> clazz);
	
	public String getType();
}
