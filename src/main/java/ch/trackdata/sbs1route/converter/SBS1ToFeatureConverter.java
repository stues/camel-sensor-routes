package ch.trackdata.sbs1route.converter;

import java.util.Map;

import org.apache.camel.BeanInject;
import org.apache.camel.Converter;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import ch.trackdata.sbs1route.message.GeoJSONFeature;
import ch.trackdata.sbs1route.message.PointGeometry;
import ch.trackdata.sbs1route.message.SBS1Message;

/**
 * Converts a {@link SBS1Message} to a {@link GeoJSONFeature}
 * 
 * @author stue
 */
@Converter
public class SBS1ToFeatureConverter {

	@BeanInject("sbs1PropertyNamePredicate")
	private Predicate<String> propertyNamePredicate;
	
	@BeanInject("sbs1ValuePredicate")
	private Predicate<Object> valuePredicate;
	
	/**
	 * Converts the given {@link SBS1Message} into a {@link GeoJSONFeature}
	 * 
	 * @param sbs1Message
	 *            the {@link SBS1Message}
	 * @return the {@link GeoJSONFeature} object
	 */
	@Converter
	public GeoJSONFeature<PointGeometry> convert(SBS1Message sbs1Message) {
		BidiMap<String, Object> properties = getProperties(sbs1Message);
		CollectionUtils.filter(properties.values(), valuePredicate);
		CollectionUtils.filter(properties.keySet(), propertyNamePredicate);
		
		PointGeometry pointGeometry;
		if(sbs1Message.getLatitude() != null || sbs1Message.getLongitude() != null){
			pointGeometry = new PointGeometry(sbs1Message.getLongitude(), sbs1Message.getLatitude());
		}
		else{
			pointGeometry = null;
		}
		GeoJSONFeature<PointGeometry> geoJSONFeature = new GeoJSONFeature<PointGeometry>(pointGeometry, properties);
		return geoJSONFeature;
	}

	/**
	 * Extracts all the values from the sbs1Message into a String to Object Map
	 * @param sbs1Message the message
	 * @return a map which contains property name to value entries
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static BidiMap<String,Object> getProperties(SBS1Message sbs1Message) {
		return new DualHashBidiMap<String,Object>((Map)new BeanMap(sbs1Message));
	}
}