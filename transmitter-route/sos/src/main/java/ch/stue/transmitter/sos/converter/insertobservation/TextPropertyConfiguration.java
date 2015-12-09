package ch.stue.transmitter.sos.converter.insertobservation;

import ch.stue.domain.Feature;

/**
 * Extends a {@link AbstractGeometryPropertyConfiguration} to be able to
 * describe a Text Observation
 * 
 * @author stue
 */
public class TextPropertyConfiguration extends AbstractPropertyConfiguration<String> {

	public static final String TEXT_TYPE_STRING = "Text";
	
	private String featureTextPropertyName;

	/**
	 * @return the featureTextPropertyName
	 */
	public String getFeatureTextPropertyName() {
		return featureTextPropertyName;
	}

	/**
	 * @param featureTextPropertyName
	 *            the featureTextPropertyName to set
	 */
	public void setFeatureTextPropertyName(String featureTextPropertyName) {
		this.featureTextPropertyName = featureTextPropertyName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue(Feature<?> feature) {
		return feature.getProperty(getFeatureTextPropertyName(), String.class);
	}
}
