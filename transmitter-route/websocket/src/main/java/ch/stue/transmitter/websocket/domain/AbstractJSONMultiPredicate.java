package ch.stue.transmitter.websocket.domain;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Provides some helper methods for predicates with multiple assignable predicates
 *
 * @author stue
 */
public abstract class AbstractJSONMultiPredicate extends AbstractJSONPredicate {

	@Override
	public Predicate<Object> getPredicate() {
		Collection<JSONPredicate> jsonPredicates = getJSONPredicates();
		if(CollectionUtils.isEmpty(jsonPredicates)){
			return PredicateUtils.falsePredicate();
		}
		else{
			Collection<Predicate<Object>> predicates = convertPredicates(jsonPredicates);
			return getPredicate(predicates);
		}
	}

	/**
	 * Returns commons predicates for the given collection of predicates
	 * @param jsonPredicates the json predicates to convert
	 * @return the apache commons predicates
	 */
	protected Collection<Predicate<Object>> convertPredicates(Collection<JSONPredicate> jsonPredicates) {
		Collection<Predicate<Object>> realPredicates = new ArrayList<Predicate<Object>>(jsonPredicates.size());
		for(JSONPredicate predicate : jsonPredicates){
			realPredicates.add(predicate.getPredicate());
		}
		return realPredicates;
	}

	/**
	 * Returns the custom predicate
	 * @param predicates contains predicates to evaluate is not empty
	 * @return the apache commons predicate
	 */
	protected abstract Predicate<Object> getPredicate(Collection<Predicate<Object>> predicates);

	/**
	 * @return returns the multiple predicates
	 */
	@JsonIgnore
	public abstract Collection<JSONPredicate> getJSONPredicates();

	/**
	 * @param jsonPredicates the multiple predicates to set
	 */
	public abstract void setJSONPredicates(Collection<JSONPredicate> jsonPredicates);

}
