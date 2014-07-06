package ch.trackdata.sbs1route.message;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author stue
 *
 */
public abstract class AbstractGeometry<C> {
		
	@JsonProperty("type")
	private final String type;
	
	@JsonProperty("coordinates")
	private List<C> coordinates;
	
	/**
	 * @return the coordinates
	 */
	public List<C> getCoordinates() {
		return coordinates;
	}

	/**
	 * @param coordinates the coordinates to set
	 */
	public void setCoordinates(List<C> coordinates) {
		this.coordinates = coordinates;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type of geometry
	 */
	public AbstractGeometry(String type){
		this.type = type;
	}
}
