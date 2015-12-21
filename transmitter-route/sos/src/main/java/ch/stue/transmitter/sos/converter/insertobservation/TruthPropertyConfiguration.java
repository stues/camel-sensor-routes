package ch.stue.transmitter.sos.converter.insertobservation;

import ch.stue.domain.Feature;

/**
 * Extends the {@link AbstractPropertyConfiguration} to describe
 * a Truth Result
 *
 * @author stue
 *
 */
public class TruthPropertyConfiguration extends AbstractPropertyConfiguration<Boolean> {

	public static final String TRUTH_TYPE_STRING = "Truth";

	private String featureTruthPropertyName;

	/**
	 * @return the featureTruthPropertyName
	 */
	public String getFeatureTruthPropertyName() {
		return this.featureTruthPropertyName;
	}

	/**
	 * @param featureTruthPropertyName the featureTruthPropertyName to set
	 */
	public void setFeatureTruthPropertyName(String featureTruthPropertyName) {
		this.featureTruthPropertyName = featureTruthPropertyName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean getValue(Feature<?> feature) {
		return feature.getProperty(getFeatureTruthPropertyName(), Boolean.class);
	}
}
