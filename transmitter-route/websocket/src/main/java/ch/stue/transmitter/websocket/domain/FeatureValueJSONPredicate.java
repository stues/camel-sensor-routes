package ch.stue.transmitter.websocket.domain;

import java.util.Comparator;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;
import org.apache.commons.collections4.functors.ComparatorPredicate;
import org.apache.commons.collections4.functors.ComparatorPredicate.Criterion;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Can be used to deserialize/serialize a Predicate which can be applied on
 * values of features
 *
 * @author stue
 */
public class FeatureValueJSONPredicate implements JSONPredicate {

	public static final String PROPERTY = "property";
	public static final String CRITERIA = "criteria";
	public static final String VALUE = "value";

	@JsonProperty(required = true, value = CRITERIA)
	private Criterion criterion;

	@JsonProperty(required = true, value = PROPERTY)
	private String propertyName;

	@JsonProperty(required = true, value = VALUE)
	private Object value;

	/**
	 * Default Constructor
	 */
	public FeatureValueJSONPredicate(){}

	/**
	 * Constructor with criteria, property and value
	 * @param criterion the criteria
	 * @param propertyName the property
	 * @param value the value
	 */
	public FeatureValueJSONPredicate(Criterion criterion, String propertyName, Object value){
		this.criterion = criterion;
		this.propertyName = propertyName;
		this.value = value;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Predicate<Object> getPredicate() {
		Comparator<Object> comparator = (Comparator<Object>) ComparatorFactory.getComparator(this.value);
		if (comparator != null) {
			Predicate comparatorPredicate = ComparatorPredicate.<Object> comparatorPredicate(this.value, comparator, this.criterion);
			return new FeaturePredicate<>(this.propertyName, this.value.getClass(), comparatorPredicate);
		} else {
			return PredicateUtils.falsePredicate();
		}
	}

	/**
	 * @return the criterion
	 */
	public Criterion getCriterion() {
		return this.criterion;
	}

	/**
	 * @param criterion
	 *            the criterion to set
	 */
	public void setCriterion(Criterion criterion) {
		this.criterion = criterion;
	}

	/**
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return this.propertyName;
	}

	/**
	 * @param propertyName
	 *            the propertyName to set
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return this.value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}
}
