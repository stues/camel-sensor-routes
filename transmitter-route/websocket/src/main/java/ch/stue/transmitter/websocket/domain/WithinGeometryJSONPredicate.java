package ch.stue.transmitter.websocket.domain;

/**
 * Abstract Class to simplify geometry predicates
 *
 * @author stue
 */
public class WithinGeometryJSONPredicate extends GeometryJSONPredicate {

	public static final String WITHIN = "within";

	private ch.stue.domain.Geometry<?> within;

	/**
	 * Default Constructor
	 */
	public WithinGeometryJSONPredicate(){
		this(null);
	}

	/**
	 * Constructor with geometry
	 * @param geometry the geometry
	 */
	public WithinGeometryJSONPredicate(ch.stue.domain.Geometry<?> geometry){
		//		super(SpatialPredicate.WITHIN);
		this.within = geometry;
	}

	//
	//	@Override
	//	protected Geometry<?> getGeometry() {
	//		return getWithin();
	//	}

	/**
	 * @return the within
	 */
	public ch.stue.domain.Geometry<?> getWithin() {
		return this.within;
	}


	/**
	 * @param within the within to set
	 */
	public void setWithin(ch.stue.domain.Geometry<?> within) {
		this.within = within;
	}

}
