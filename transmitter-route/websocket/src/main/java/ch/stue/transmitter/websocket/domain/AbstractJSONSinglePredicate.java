package ch.stue.transmitter.websocket.domain;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Provides some helper methods for predicates with one assignable predicate
 *
 * @author stue
 */
public abstract class AbstractJSONSinglePredicate extends AbstractJSONPredicate {

	@Override
	public Predicate<Object> getPredicate() {
		JSONPredicate jsonPredicate = getJSONPredicate();
		if(jsonPredicate == null){
			return PredicateUtils.falsePredicate();
		}
		else{
			Predicate<Object> predicate = convertPredicate(jsonPredicate);
			return getPredicate(predicate);
		}
	}

	/**
	 * Returns commons predicate for the given predicate
	 * @param jsonPredicate the json predicate to convert
	 * @return the apache commons predicate
	 */
	protected Predicate<Object> convertPredicate(JSONPredicate jsonPredicate) {
		return jsonPredicate.getPredicate();
	}

	/**
	 * Returns the custom predicate
	 * @param predicate contains predicate to evaluate is not null
	 * @return the apache commons predicate
	 */
	protected abstract Predicate<Object> getPredicate(Predicate<Object> predicate);

	/**
	 * @return returns the predicate
	 */
	@JsonIgnore
	public abstract JSONPredicate getJSONPredicate();

	/**
	 * @param jsonPredicate the predicate to set
	 */
	public abstract void setJSONPredicate(JSONPredicate jsonPredicate);
}
