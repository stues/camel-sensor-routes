package ch.stue.transmitter.wfs.converter.inserttransaction;

import javax.xml.bind.JAXBElement;

import ch.stue.domain.Feature;

/**
 * This Helper class provides a Method {@link #getInsertTransaction} to
 * transform a given {@link Feature} into a {@link JAXBElement} which contains a
 * {@link TransactionType}
 * 
 * @author stue
 * 
 */
public class InsertTransactionJAXBHelper {

//	private static final net.opengis.gml.v_3_1_1.ObjectFactory GML_OBJECT_FACTORY = new net.opengis.gml.v_3_1_1.ObjectFactory();
//	private static final net.opengis.wfs.v_1_1_0.ObjectFactory WFS_OBJECT_FACTORY = new net.opengis.wfs.v_1_1_0.ObjectFactory();
//
//	/**
//	 * Creates a new {@link InsertObservationType} as {@link JAXBElement} for
//	 * the given feature with the given configuration
//	 * 
//	 * @param configuration
//	 *            the configuration
//	 * @param feature
//	 *            the feature
//	 * 
//	 * @return the {@link JAXBElement} which contains a
//	 *         {@link InsertObservationType}
//	 */
//	@SuppressWarnings("unchecked")
//	public static JAXBElement<TransactionType> getWFSInsertTransaction(InsertTransactionWFS110Configuration configuration, Feature<?> feature) {
//
//		List<Object> insertElementTypes = new LinkedList<Object>();
//		InsertElementType insertElementType = WFS_OBJECT_FACTORY.createInsertElementType();
//		FeatureTypeType featureTypeType = WFS_OBJECT_FACTORY.createFeatureTypeType();
//		
//		featureTypeType.setDefaultSRS("EPSG:4326");
//					
//
//		TransactionType wsfInsertTransactionType = WFS_OBJECT_FACTORY.createTransactionType();
//		wsfInsertTransactionType.setService(configuration.getService());
//		wsfInsertTransactionType.setVersion(configuration.getVersion());
//		wsfInsertTransactionType.setInsertOrUpdateOrDelete(insertElementTypes);
//		JAXBElement<TransactionType> transaction = WFS_OBJECT_FACTORY.createTransaction(wsfInsertTransactionType);
//
//		return transaction;
//	}
}
