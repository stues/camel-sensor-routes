package ch.stue.transmitter.sos.converter.insertobservation;

import ch.stue.domain.Feature;
import ch.stue.domain.Geometry;

/**
 * Extends the {@link AbstractPropertyConfiguration} with further attributes for
 * Geometric Observation Results
 * 
 * @author stue
 * 
 * @param <T>
 *            the Type of Object which will be added as result
 */
public class AbstractGeometryPropertyConfiguration<T extends Geometry<?>> extends AbstractPropertyConfiguration<T> {
	
	private String geometryMeasurementPropertyName;

	private Class<T> geometryMeasurementPropertyClazz;

	private boolean useFeatureGeometry;

	/**
	 * @return the geometryMeasurementPropertyName
	 */
	public String getGeometryMeasurementPropertyName() {
		return geometryMeasurementPropertyName;
	}

	/**
	 * @param geometryMeasurementPropertyName
	 *            the geometryMeasurementPropertyName to set
	 */
	public void setGeometryMeasurementPropertyName(String geometryMeasurementPropertyName) {
		this.geometryMeasurementPropertyName = geometryMeasurementPropertyName;
	}

	/**
	 * @return the geometryMeasurementPropertyClazz
	 */
	public Class<T> getGeometryMeasurementPropertyClazz() {
		return geometryMeasurementPropertyClazz;
	}

	/**
	 * @param geometryMeasurementPropertyClazz
	 *            the geometryMeasurementPropertyClazz to set
	 */
	public void setGeometryMeasurementPropertyClazz(Class<T> geometryMeasurementPropertyClazz) {
		this.geometryMeasurementPropertyClazz = geometryMeasurementPropertyClazz;
	}

	/**
	 * @return the useFeatureGeometry
	 */
	public boolean isUseFeatureGeometry() {
		return useFeatureGeometry;
	}

	/**
	 * @param useFeatureGeometry
	 *            the useFeatureGeometry to set
	 */
	public void setUseFeatureGeometry(boolean useFeatureGeometry) {
		this.useFeatureGeometry = useFeatureGeometry;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T getValue(Feature<?> feature) {
		if (useFeatureGeometry) {
			return (T) feature.getGeometry();
		} else {
			return feature.getProperty(getGeometryMeasurementPropertyName(), getGeometryMeasurementPropertyClazz());
		}
	}
}
