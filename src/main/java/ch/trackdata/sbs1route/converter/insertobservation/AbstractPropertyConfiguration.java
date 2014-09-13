package ch.trackdata.sbs1route.converter.insertobservation;


public abstract class AbstractPropertyConfiguration<T> implements ObservedPropertyConfiguration<T> {
	
	private String observationName; 
	
	private String procedure;
	
	private String observedProperty;
			
	private String featureOfInterestPrefix;
	
	private String featureIdentPropertyName;

	private String featureTitlePropertyName;
		
	private boolean createNullValueMessages;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getObservationName() {
		return observationName;
	}
	
	/**
	 * @param observationName the observationName to set
	 */
	public void setObservationName(String observationName) {
		this.observationName = observationName;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getProcedure() {
		return procedure;
	}
	
	/**
	 * @param procedure the procedure to set
	 */
	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getObservedProperty() {
		return observedProperty;
	}
	
	/**
	 * @param observedProperty the observedProperty to set
	 */
	public void setObservedProperty(String observedProperty) {
		this.observedProperty = observedProperty;
	}
		
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFeatureOfInterestPrefix() {
		return featureOfInterestPrefix;
	}
	/**
	 * @param featureOfInterestPrefix the featureOfInterestPrefix to set
	 */
	public void setFeatureOfInterestPrefix(String featureOfInterestPrefix) {
		this.featureOfInterestPrefix = featureOfInterestPrefix;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFeatureIdentPropertyName() {
		return featureIdentPropertyName;
	}
	
	/**
	 * @param featureIdentPropertyName the featureIdentPropertyName to set
	 */
	public void setFeatureIdentPropertyName(String featureIdentPropertyName) {
		this.featureIdentPropertyName = featureIdentPropertyName;
	}
		
	/**
	 * @return the featureTitlePropertyName
	 */
	public String getFeatureTitlePropertyName() {
		return featureTitlePropertyName;
	}

	/**
	 * @param featureTitlePropertyName the featureTitlePropertyName to set
	 */
	public void setFeatureTitlePropertyName(String featureTitlePropertyName) {
		this.featureTitlePropertyName = featureTitlePropertyName;
	}

	/**
	 * @return the createNullValueMessages
	 */
	public boolean isCreateNullValueMessages() {
		return createNullValueMessages;
	}

	/**
	 * @param createNullValueMessages the createNullValueMessages to set
	 */
	public void setCreateNullValueMessages(boolean createNullValueMessages) {
		this.createNullValueMessages = createNullValueMessages;
	}
}
