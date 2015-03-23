package ch.trackdata.sbs1route.message;

import java.io.Serializable;

/**
 * The Geometry Interface
 * 
 * @author stue
 *
 * @param <C> the stored values of the Geometry
 */
public interface Geometry<C> extends Serializable {

	/**
	 * @return the coordinates
	 */
	public abstract C getCoordinates();

	/**
	 * @param coordinates
	 *            the coordinates to set
	 */
	public abstract void setCoordinates(C coordinates);

	/**
	 * @return the type
	 */
	public abstract String getType();

}