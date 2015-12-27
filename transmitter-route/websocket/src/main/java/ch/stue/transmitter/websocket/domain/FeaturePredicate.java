package ch.stue.transmitter.websocket.domain;

import org.apache.commons.collections4.Predicate;

import ch.stue.domain.Feature;

public class FeaturePredicate<T> implements Predicate<Object> {

	private final String propertyName;

	private final Predicate<T> predicate;

	private final Class<T> valueClazz;

	public FeaturePredicate(String propertyName, Predicate<T> predicate){
		this.propertyName = propertyName;
		this.predicate = predicate;
		this.valueClazz = null;
	}


	public FeaturePredicate(String propertyName, Class<T> valueClazz, Predicate<T> predicate){
		this.propertyName = propertyName;
		this.predicate = predicate;
		this.valueClazz = valueClazz;
	}

	@Override
	public boolean evaluate(Object object) {
		if(object instanceof Feature){
			Feature<?> feature = (Feature<?>)object;
			return evaluate(feature);
		}
		return false;
	}

	/**
	 * Evaluate the feature
	 * @param feature the feature to evaluate
	 * @return true if valid otherwise flase
	 */
	@SuppressWarnings("unchecked")
	protected boolean evaluate(Feature<?> feature) {
		if(this.valueClazz == null){
			T value = feature.getProperty(this.propertyName, this.valueClazz);
			return this.predicate.evaluate(value);
		}
		else{
			Object value = feature.getProperty(this.propertyName, Object.class);
			return this.predicate.evaluate((T)value);
		}
	}
}
