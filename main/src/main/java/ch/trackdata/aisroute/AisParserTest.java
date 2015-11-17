package ch.trackdata.aisroute;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import nl.esi.metis.aisparser.AISMessage;
import nl.esi.metis.aisparser.AISMessage01;
import nl.esi.metis.aisparser.AISMessage05;
import nl.esi.metis.aisparser.AISParser;
import nl.esi.metis.aisparser.HandleAISMessage;
import nl.esi.metis.aisparser.HandleInvalidInput;
import nl.esi.metis.aisparser.VDMLine;
import nl.esi.metis.aisparser.VDMMessage;
import nl.esi.metis.aisparser.annotations.Annotation;
import nl.esi.metis.aisparser.provenance.Provenance;
import nl.esi.metis.aisparser.provenance.VDMMessageProvenance;

public class AisParserTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HandleAISMessage messageHandler = new HandleAISMessage() {

			@Override
			public void handleAISMessage(AISMessage paramAISMessage) {

				if (paramAISMessage instanceof AISMessage01) {
					System.out.println(((AISMessage01) paramAISMessage).getUserID());
				}
				System.out.println(paramAISMessage.getMessageID());
				System.out.println(paramAISMessage);
				System.out.println("PROVENANCE: " + paramAISMessage.getProvenance().getProvenanceTree("\t  "));
				System.out.println("PROVENANCE ANNO: " + paramAISMessage.getProvenance().getProvenance().getAnnotations());
				VDMMessageProvenance vdmMessageProvenance = paramAISMessage.getProvenance().getProvenance();
				System.out.println("SOURCE: " + vdmMessageProvenance.getParts()[0].getSource());
				if (paramAISMessage instanceof AISMessage05) {
					System.out.println(((AISMessage05) paramAISMessage).getCallSign());
					System.out.println(((AISMessage05) paramAISMessage).getName());
				}
			}
		};

		HandleInvalidInput errorHandler = new HandleInvalidInput() {

			@Override
			public void handleInvalidSensorData(Provenance paramProvenance, String paramString) {
				System.err.println(paramString);
			}

			@Override
			public void handleInvalidVDMLine(VDMLine paramVDMLine) {
				System.err.println(paramVDMLine);
			}

			@Override
			public void handleInvalidVDMMessage(VDMMessage paramVDMMessage) {
				System.err.println(paramVDMMessage);
			}
		};
		Provenance provenance = new Provenance() {

			@Override
			public double getTime() {
				return new Date().getTime();
			}

			@Override
			public String getProvenanceTree(String paramString) {
				return "MESSAGE ID 1";
			}

			@Override
			public List<Annotation> getAnnotations() {
				return Collections.emptyList();
			}
		};
		AISParser aisParser = new AISParser(messageHandler, errorHandler);
		aisParser.handleSensorData(provenance, "!AIVDM,2,1,6,A,53Ema3T00000000000084PT60@F08DpT@u8n0V0t00000t0Ht0000000,0*62");
		aisParser.handleSensorData(provenance, "!AIVDM,2,2,6,A,000000000000000,2*22");
		aisParser.handleSensorData(provenance, "!AIVDM,1,1,,A,13@rK2002r01U80F7ObpGndL0H7l,0*7D");
	}

}
