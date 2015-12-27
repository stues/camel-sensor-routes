package ch.stue.transmitter.websocket.domain;

import org.apache.commons.collections4.Predicate;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface JSONPredicate {

	@JsonIgnore
	Predicate<Object> getPredicate();

	public static final String TYPE_PROPERTY_NAME = "type";

	public static final String FEATURE_PREDICATE_NAME = "featureValue";

	public static final String COMPARATOR_PREDICATE_NAME = "compare";

	public static final String MULTI_PREDICATE_NAME = "multi";

	public static final String OR_PREDICATE_NAME = "or";

}
