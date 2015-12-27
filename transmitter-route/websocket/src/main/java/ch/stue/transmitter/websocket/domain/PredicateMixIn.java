package ch.stue.transmitter.websocket.domain;

import org.apache.commons.collections4.functors.AndPredicate;
import org.apache.commons.collections4.functors.ComparatorPredicate;
import org.apache.commons.collections4.functors.NotNullPredicate;
import org.apache.commons.collections4.functors.OrPredicate;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = PredicateMixIn.TYPE_PROPERTY_NAME)
@JsonSubTypes({
	@JsonSubTypes.Type(
			value = NotNullPredicate.class,
			name = PredicateMixIn.NOT_NULL_PREDICATE_NAME),
	@JsonSubTypes.Type(
			value = AndPredicate.class,
			name = PredicateMixIn.AND_PREDICATE_NAME),
	@JsonSubTypes.Type(
			value = OrPredicate.class,
			name = PredicateMixIn.OR_PREDICATE_NAME),
	@JsonSubTypes.Type(
			value = ComparatorPredicate.class,
			name = PredicateMixIn.COMPARATOR_PREDICATE_NAME)})
public interface PredicateMixIn {

	public static final String TYPE_PROPERTY_NAME = "type";

	public static final String NOT_NULL_PREDICATE_NAME = "notNull";

	public static final String COMPARATOR_PREDICATE_NAME = "compare";

	public static final String AND_PREDICATE_NAME = "and";

	public static final String OR_PREDICATE_NAME = "or";

}
