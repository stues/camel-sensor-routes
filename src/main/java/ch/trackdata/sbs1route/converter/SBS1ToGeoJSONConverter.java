package ch.trackdata.sbs1route.converter;

import org.apache.camel.Converter;
import org.apache.commons.collections.BeanMap;

import ch.trackdata.sbs1route.message.GeoJSONFeature;
import ch.trackdata.sbs1route.message.PointGeometry;
import ch.trackdata.sbs1route.message.SBS1Message;

/**
 * Converts a {@link SBS1Message} to a {@link GeoJSONFeature}
 * 
 * @author stue
 */
@Converter
public class SBS1ToGeoJSONConverter {

	/**
	 * Converts the given {@link SBS1Message} into a {@link GeoJSONFeature}
	 * 
	 * @param sbs1Message
	 *            the {@link SBS1Message}
	 * @return the {@link GeoJSONFeature} object
	 */
	@Converter
	@SuppressWarnings("unchecked")
	public static GeoJSONFeature convert(SBS1Message sbs1Message) {
		BeanMap sbs1Map = new BeanMap(sbs1Message);
		PointGeometry pointGeometry;
		if(sbs1Message.getLatitude() != null || sbs1Message.getLongitude() != null){
			pointGeometry = new PointGeometry(sbs1Message.getLongitude(), sbs1Message.getLatitude());
		}
		else{
			pointGeometry = null;
		}
		GeoJSONFeature geoJSONFeature = new GeoJSONFeature(pointGeometry, sbs1Map);
		return geoJSONFeature;
	}
}