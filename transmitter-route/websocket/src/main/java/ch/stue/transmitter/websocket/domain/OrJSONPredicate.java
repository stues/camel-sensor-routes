package ch.stue.transmitter.websocket.domain;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Can be used to deserialize/serialize a OR predicate list from/to JSON
 *
 * @author stue
 */
public class OrJSONPredicate extends AbstractJSONMultiPredicate {

	public static final String OR = "or";

	/**
	 * Default Constructor
	 */
	public OrJSONPredicate() {}

	/**
	 * Constructor with and predicates
	 * @param predicates the predicates to combine by or
	 */
	public OrJSONPredicate(JSONPredicate... predicates) {
		this.or = Arrays.asList(predicates);
	}

	@JsonProperty(required = true, value = OR)
	private Collection<JSONPredicate> or;

	@Override
	protected Predicate<Object> getPredicate(Collection<Predicate<Object>> predicates) {
		return PredicateUtils.anyPredicate(predicates);
	}

	@Override
	public Collection<JSONPredicate> getJSONPredicates() {
		return getOr();
	}

	@Override
	public void setJSONPredicates(Collection<JSONPredicate> jsonPredicates) {
		setOr(jsonPredicates);
	}

	/**
	 * @return the or predicates
	 */
	public Collection<JSONPredicate> getOr() {
		return this.or;
	}

	/**
	 * @param or the or to set
	 */
	public void setOr(Collection<JSONPredicate> or) {
		this.or = or;
	}
}
