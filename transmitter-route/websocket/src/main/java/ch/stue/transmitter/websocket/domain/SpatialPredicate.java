package ch.stue.transmitter.websocket.domain;

import java.io.Serializable;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Describes the different spatial predicate
 *
 * provides methods to calculate the predicate for two geometries
 *
 * @author stue
 */
public enum SpatialPredicate implements Serializable {
	EQUALS("equals") {
		@Override
		public boolean evaluate(com.vividsolutions.jts.geom.Geometry object1, com.vividsolutions.jts.geom.Geometry object2) {
			return object1.equals(object2);
		}
	},
	DISJOINT("disjoint") {
		@Override
		public boolean evaluate(com.vividsolutions.jts.geom.Geometry object1, com.vividsolutions.jts.geom.Geometry object2) {
			return object1.disjoint(object2);
		}
	},
	TOUCHES("touches") {
		@Override
		public boolean evaluate(com.vividsolutions.jts.geom.Geometry object1, com.vividsolutions.jts.geom.Geometry object2) {
			return object1.touches(object2);
		}
	},
	CONTAINS("contains") {
		@Override
		public boolean evaluate(com.vividsolutions.jts.geom.Geometry object1, com.vividsolutions.jts.geom.Geometry object2) {
			return object1.contains(object2);
		}
	},
	COVERS("covers") {
		@Override
		public boolean evaluate(com.vividsolutions.jts.geom.Geometry object1, com.vividsolutions.jts.geom.Geometry object2) {
			return object1.covers(object2);
		}
	},
	INTERSECTS("intersects") {
		@Override
		public boolean evaluate(com.vividsolutions.jts.geom.Geometry object1, com.vividsolutions.jts.geom.Geometry object2) {
			return object1.intersects(object2);
		}
	},
	WITHIN("within") {
		@Override
		public boolean evaluate(com.vividsolutions.jts.geom.Geometry object1, com.vividsolutions.jts.geom.Geometry object2) {
			return object1.within(object2);
		}
	},
	COVEREDBY("coveredBy") {
		@Override
		public boolean evaluate(com.vividsolutions.jts.geom.Geometry object1, com.vividsolutions.jts.geom.Geometry object2) {
			return object1.coveredBy(object2);
		}
	};

	private final String name;

	private static final BidiMap<SpatialPredicate, String> spatialPredicateMap;
	static{
		spatialPredicateMap = new DualHashBidiMap<SpatialPredicate, String>();
		spatialPredicateMap.put(EQUALS, EQUALS.getName());
		spatialPredicateMap.put(DISJOINT, DISJOINT.getName());
		spatialPredicateMap.put(TOUCHES, TOUCHES.getName());
		spatialPredicateMap.put(CONTAINS, CONTAINS.getName());
		spatialPredicateMap.put(COVERS, COVERS.getName());
		spatialPredicateMap.put(INTERSECTS, INTERSECTS.getName());
		spatialPredicateMap.put(WITHIN, WITHIN.getName());
		spatialPredicateMap.put(COVEREDBY, COVEREDBY.getName());
	}

	/**
	 * Constructor with name
	 * @param name the name of the spatial predicate (to be used within json)
	 */
	private SpatialPredicate (String name) {
		this.name = name;
	}



	/**
	 * Returns true if the spatial predicate matches for the two given
	 * geometries
	 *
	 * @param object1
	 *            the first geometry
	 * @param object2
	 *            the second geometry
	 * @return true if spatial predicatae matches
	 */
	public abstract boolean evaluate(com.vividsolutions.jts.geom.Geometry object1, com.vividsolutions.jts.geom.Geometry object2);


	/**
	 * Creates a spatial predicate for the given spatial predicate name
	 * @param spatialPredicateName the name of the spatial predicate
	 * @return the Spatial Predicate
	 */
	@JsonCreator
	public static SpatialPredicate getSpatialPredicate (String spatialPredicateName) {
		if(spatialPredicateName == null) {
			throw new IllegalArgumentException("Spatial Predicate Name is null");
		}

		if(isNameKnown(spatialPredicateName)){
			return spatialPredicateMap.getKey(spatialPredicateName);
		}

		throw new IllegalArgumentException(String.format("Spatial Predicate Name '%s' is not known", spatialPredicateName));
	}


	/**
	 * Returns true if name can be mapped to a spatial predicate
	 * @param spatialPredicateName the name to check
	 * @return true if name can be mapped to a spatial predicate otherwise false
	 */
	public static boolean isNameKnown(String spatialPredicateName){
		return spatialPredicateMap.containsValue(spatialPredicateName);
	}

	/**
	 * @return the name
	 */
	@JsonValue
	public String getName() {
		return this.name;
	}

}