package ch.stue.transmitter.websocket.domain;

import org.apache.commons.collections4.Predicate;

import ch.stue.domain.Feature;

/**
 * Predicate class to check for values of a given Feature
 *
 * @author stue
 *
 * @param <T> the type of the predicate
 */
public class FeaturePredicate<T> implements Predicate<Object> {

	private final String propertyName;

	private final Predicate<T> predicate;

	private final Class<T> valueClazz;

	/**
	 * Constructor with property name and predicate
	 * @param propertyName the property name
	 * @param predicate the predicate to test on the property
	 */
	public FeaturePredicate(String propertyName, Predicate<T> predicate){
		this.propertyName = propertyName;
		this.predicate = predicate;
		this.valueClazz = null;
	}

	/**
	 * Constructor with property name, valueClazz and predicate
	 * @param propertyName the property name
	 * @param valueClazz the value clazz
	 * @param predicate the predicate
	 */
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
