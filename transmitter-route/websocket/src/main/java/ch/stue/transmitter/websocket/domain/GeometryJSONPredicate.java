package ch.stue.transmitter.websocket.domain;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import ch.stue.domain.Geometry;
import ch.stue.transmitter.websocket.FeatureGeometryPredicate;
import ch.stue.transmitter.websocket.GeometryConverterHelper;

/**
 * Abstract Class to simplify geometry predicates
 *
 * @author stue
 */
public class GeometryJSONPredicate extends AbstractJSONPredicate implements Map.Entry<SpatialPredicate, Geometry<?>> {

	@JsonIgnore
	private SpatialPredicate spatialPredicate;

	@JsonIgnore
	private Geometry<?> geometry;

	/**
	 * Default Constructor
	 */
	public GeometryJSONPredicate() {
		this(null,null);
	}

	/**
	 * Constructor with spatial predicate
	 *
	 * @param spatialPredicate
	 *            the spatial predicate
	 */
	public GeometryJSONPredicate(SpatialPredicate spatialPredicate) {
		this(spatialPredicate, null);
	}

	/**
	 * Constructor with spatial predicate
	 *
	 * @param spatialPredicate
	 *            the spatial predicate
	 * @param geometry
	 *            the geometry
	 */
	public GeometryJSONPredicate(SpatialPredicate spatialPredicate, Geometry<?> geometry) {
		this.spatialPredicate = spatialPredicate != null ? spatialPredicate : SpatialPredicate.EQUALS;
		this.geometry = geometry;
	}

	@Override
	public SpatialPredicate getKey() {
		return this.spatialPredicate;
	}

	@Override
	public Geometry<?> getValue() {
		return this.geometry;
	}

	@Override
	public Geometry<?> setValue(Geometry<?> geometry) {
		this.geometry = geometry;
		return this.geometry;
	}

	/**
	 * @param spatialPredicate
	 *            the spatialPredicate to set
	 */
	@JsonAnySetter
	public void setSpatialPredicate(String name, Geometry<?> geometry) {
		if(SpatialPredicate.isNameKnown(name)){
			this.spatialPredicate = SpatialPredicate.getSpatialPredicate(name);
			this.geometry = geometry;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Predicate<Object> getPredicate() {
		if (this.spatialPredicate != null) {
			com.vividsolutions.jts.geom.Geometry jtsGeometry = GeometryConverterHelper.getGeometry(this.geometry);
			if (jtsGeometry != null) {
				return (Predicate) new FeatureGeometryPredicate(jtsGeometry, this.spatialPredicate);
			}
		}
		return PredicateUtils.falsePredicate();
	}

	/**
	 * @return all the keys which can uniqly identified by this class mapped to the actual class (GeometryJSONPredicate)
	 */
	public static Map<String, Class<? extends JSONPredicate>> getJSONUniqueIdentifiers(){
		Map<String, Class<?extends JSONPredicate>> keyToClassMap = new HashMap<String, Class<?extends JSONPredicate>>();
		for(SpatialPredicate spatialPredicate : SpatialPredicate.values()){
			keyToClassMap.put(spatialPredicate.getName(), GeometryJSONPredicate.class);
		}
		return keyToClassMap;
	}
}
