package ch.stue.transmitter.websocket.domain;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Can be used to deserialize/serialize a AND predicate list from/to JSON
 *
 * @author stue
 */
public class AndJSONPredicate extends AbstractJSONMultiPredicate {

	public static final String AND = "and";

	/**
	 * Default Constructor
	 */
	public AndJSONPredicate() {}

	/**
	 * Constructor with and predicates
	 * @param predicates the predicates to combine by and
	 */
	public AndJSONPredicate(JSONPredicate... predicates) {
		this.and = Arrays.asList(predicates);
	}

	@JsonProperty(required = true, value = AND)
	private Collection<JSONPredicate> and;

	@Override
	protected Predicate<Object> getPredicate(Collection<Predicate<Object>> predicates) {
		return PredicateUtils.allPredicate(predicates);
	}

	@Override
	public Collection<JSONPredicate> getJSONPredicates() {
		return getAnd();
	}

	@Override
	public void setJSONPredicates(Collection<JSONPredicate> jsonPredicates) {
		setAnd(jsonPredicates);
	}

	/**
	 * @return the and
	 */
	public Collection<JSONPredicate> getAnd() {
		return this.and;
	}

	/**
	 * @param and the and to set
	 */
	public void setAnd(Collection<JSONPredicate> and) {
		this.and = and;
	}
}
