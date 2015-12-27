package ch.stue.transmitter.websocket.domain;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Can be used to deserialize/serialize a OR predicate list from/to JSON
 *
 * @author stue
 */
public class NotJSONPredicate extends AbstractJSONSinglePredicate {

	public static final String NOT = "not";

	@JsonProperty(required = true, value = NOT)
	private JSONPredicate not;

	/**
	 * Default Constructor
	 */
	public NotJSONPredicate(){}

	/**
	 * Default Constructor
	 */
	public NotJSONPredicate(JSONPredicate predicate){
		this.not = predicate;
	}

	@Override
	protected Predicate<Object> getPredicate(Predicate<Object> predicate) {
		return PredicateUtils.notPredicate(predicate);
	}

	@Override
	public JSONPredicate getJSONPredicate() {
		return getNot();
	}

	@Override
	public void setJSONPredicate(JSONPredicate jsonPredicate) {
		setNot(jsonPredicate);
	}

	/**
	 * @return the not
	 */
	public JSONPredicate getNot() {
		return this.not;
	}

	/**
	 * @param not the not to set
	 */
	public void setNot(JSONPredicate not) {
		this.not = not;
	}
}
