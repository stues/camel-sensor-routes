package ch.stue.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * {@link AbstractGeometry} JSON Class provides coordinates and a type member
 *
 * @author stue
 *
 */
public abstract class AbstractGeometry<C> implements Geometry<C> {

	private static final long serialVersionUID = 1L;

	public static final String TYPE_PROPERTY_NAME = "type";
	public static final String COORDINATES_PROPERTY_NAME = "coordinates";

	@JsonProperty(COORDINATES_PROPERTY_NAME)
	private C coordinates;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public C getCoordinates() {
		return this.coordinates;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCoordinates(C coordinates) {
		this.coordinates = coordinates;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract String getType();

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
}
