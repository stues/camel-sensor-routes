package ch.stue.transmitter.websocket.domain;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Can be used to deserialize/serialize a OR predicate list from/to JSON
 *
 * @author stue
 */
public class FeatureIsNullJSONPredicate extends AbstractJSONPredicate {

	public static final String IS_NULL = "isNull";

	@JsonProperty(required = true, value = IS_NULL)
	private String nullPropertyName;

	/**
	 * Default Constructor
	 */
	public FeatureIsNullJSONPredicate() {
	}

	/**
	 * Constructor with property name
	 *
	 * @param nullPropertyName
	 */
	public FeatureIsNullJSONPredicate(String nullPropertyName) {
		this.nullPropertyName = nullPropertyName;
	}

	@Override
	public Predicate<Object> getPredicate() {
		return new FeaturePredicate<>(this.nullPropertyName, Object.class, PredicateUtils.nullPredicate());
	}

	/**
	 * @return the nullPropertyName
	 */
	public String getNullPropertyName() {
		return this.nullPropertyName;
	}

	/**
	 * @param nullPropertyName
	 *            the nullPropertyName to set
	 */
	public void setNullPropertyName(String nullPropertyName) {
		this.nullPropertyName = nullPropertyName;
	}
}
