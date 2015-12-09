package ch.stue.transmitter.sos.converter.insertobservation;

import ch.stue.domain.Feature;

/**
 * Extends the {@link AbstractPropertyConfiguration} to describe 
 * a Measurement Result
 * 
 * @author stue
 * 
 */
public class MeasurementPropertyConfiguration extends AbstractPropertyConfiguration<Number> {

	public static final String MEASUREMENT_TYPE_STRING = "Measurement";
	
	private String featureMeasurementPropertyName;

	private String measureUom;

	private Class<? extends Number> featureMeasurementPropertyClazz;

	/**
	 * {@inheritDoc}
	 */
	public String getFeatureMeasurementPropertyName() {
		return featureMeasurementPropertyName;
	}

	/**
	 * @param featureMeasurementPropertyName
	 *            the featureMeasurementPropertyName to set
	 */
	public void setFeatureMeasurementPropertyName(String featureMeasurementPropertyName) {
		this.featureMeasurementPropertyName = featureMeasurementPropertyName;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends Number> getFeatureMeasurementPropertyClazz() {
		return featureMeasurementPropertyClazz;
	}

	/**
	 * @param featureMeasurementPropertyClazz
	 *            the featureMeasurementPropertyClazz to set
	 */
	public void setFeatureMeasurementPropertyClazz(Class<? extends Number> featureMeasurementPropertyClazz) {
		this.featureMeasurementPropertyClazz = featureMeasurementPropertyClazz;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getMeasureUom() {
		return measureUom;
	}

	/**
	 * @param measureUom
	 *            the measureUom to set
	 */
	public void setMeasureUom(String measureUom) {
		this.measureUom = measureUom;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Number getValue(Feature<?> feature) {
		return feature.getProperty(getFeatureMeasurementPropertyName(), getFeatureMeasurementPropertyClazz());
	}
}
