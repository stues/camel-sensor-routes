package ch.stue.transmitter.sos.converter.insertobservation;

import ch.stue.domain.PointGeometry;

/**
 * Extends a {@link AbstractGeometryPropertyConfiguration} to be able to describe a {@PointGeometry}
 * Observation 
 * 
 * @author stue
 */
public class PointGeometryPropertyConfiguration extends AbstractGeometryPropertyConfiguration<PointGeometry> {

	public static final String POINT_GEOMETRY_TYPE_STRING = "PointGeometry";
	
	private String srsName;
	
	private String pointId;

	/**
	 * @return the srsName
	 */
	public String getSrsName() {
		return srsName;
	}

	/**
	 * @param srsName the srsName to set
	 */
	public void setSrsName(String srsName) {
		this.srsName = srsName;
	}

	/**
	 * @return the pointId
	 */
	public String getPointId() {
		return pointId;
	}

	/**
	 * @param pointId the pointId to set
	 */
	public void setPointId(String pointId) {
		this.pointId = pointId;
	}
	
}
