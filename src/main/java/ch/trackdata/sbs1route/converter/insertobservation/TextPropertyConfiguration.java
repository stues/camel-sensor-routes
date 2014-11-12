package ch.trackdata.sbs1route.converter.insertobservation;

import ch.trackdata.sbs1route.message.GeoJSONFeature;

/**
 * Extends a {@link AbstractGeometryPropertyConfiguration} to be able to
 * describe a Text Observation
 * 
 * @author stue
 */
public class TextPropertyConfiguration extends AbstractPropertyConfiguration<String> {

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
	public String getValue(GeoJSONFeature<?> feature) {
		return feature.getProperty(getFeatureTextPropertyName(), String.class);
	}
}
