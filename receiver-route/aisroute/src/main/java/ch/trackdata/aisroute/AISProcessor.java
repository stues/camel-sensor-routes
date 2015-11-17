package ch.trackdata.aisroute;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import nl.esi.metis.aisparser.AISMessage;
import nl.esi.metis.aisparser.AISParser;
import nl.esi.metis.aisparser.HandleAISMessage;
import nl.esi.metis.aisparser.HandleInvalidInput;
import nl.esi.metis.aisparser.VDMLine;
import nl.esi.metis.aisparser.VDMMessage;
import nl.esi.metis.aisparser.annotations.Annotation;
import nl.esi.metis.aisparser.provenance.Provenance;
import nl.esi.metis.aisparser.provenance.VDMLineProvenance;
import nl.esi.metis.aisparser.provenance.VDMMessageProvenance;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Processor which converts a VDML Message into a AisMessage it updates the
 * Exchange out body if conversion was successful otherwise Exchange out will be
 * null
 * 
 * @author stue
 * 
 */
public class AISProcessor implements Processor {

	private static final Logger LOGGER = LoggerFactory.getLogger(AISProcessor.class);

	private AISParser aisParser;

	private ExchangeAwareProvenance provenance;

	/**
	 * Default Constructor
	 */
	public AISProcessor() {

		HandleInvalidInput errorHandler = new LoggerHandleInvalidInput();

		HandleAISMessage messageHandler = new ExchangeAwareHandleAISMessage();

		aisParser = new AISParser(messageHandler, errorHandler);

		provenance = new ExchangeAwareProvenance();
	}

	@Override
	public void process(final Exchange exchange) {
		
		provenance.setExchange(exchange);
		
		String message = exchange.getIn().getBody(String.class);
		message = message.trim();
		exchange.getOut().setBody(null);
		aisParser.handleSensorData(provenance, message);
	}

	/**
	 * Handle AIS Message implementation which sets the body of the exchange
	 * 
	 * @author stue
	 * 
	 */
	private static final class ExchangeAwareHandleAISMessage implements HandleAISMessage {

		@Override
		public void handleAISMessage(AISMessage aisMessage) {

			VDMMessageProvenance vdmMessageProvenance = aisMessage.getProvenance().getProvenance();
			VDMLineProvenance vdmLineProvenance = vdmMessageProvenance.getParts()[0];
			if (vdmLineProvenance != null && vdmLineProvenance.getSource() instanceof ExchangeAwareProvenance) {
				ExchangeAwareProvenance exchangeAwareProvenance = (ExchangeAwareProvenance) vdmLineProvenance.getSource();

				LOGGER.info("Update message: {}", aisMessage);
				exchangeAwareProvenance.getExchange().getOut().setBody(aisMessage);
			}
		}
	}

	/**
	 * InvalidInput handler which simply logs the issues
	 * 
	 * @author stue
	 * 
	 */
	private static final class LoggerHandleInvalidInput implements HandleInvalidInput {

		@Override
		public void handleInvalidSensorData(Provenance paramProvenance, String paramString) {
			LOGGER.warn("Invalid Sensor Data for: " + paramString);
		}

		@Override
		public void handleInvalidVDMLine(VDMLine paramVDMLine) {
			LOGGER.warn("Invalid VDMLine: " + paramVDMLine);
		}

		@Override
		public void handleInvalidVDMMessage(VDMMessage paramVDMMessage) {
			LOGGER.warn("Invalid VDMMessage: " + paramVDMMessage);
		}
	}

	/**
	 * Provenance which is aware of the message exchange and callback
	 * 
	 * @author stue
	 */
	private static final class ExchangeAwareProvenance implements Provenance {

		private Exchange exchange;

		@Override
		public double getTime() {
			return new Date().getTime();
		}

		@Override
		public String getProvenanceTree(String paramString) {
			return paramString;
		}

		@Override
		public List<Annotation> getAnnotations() {
			return Collections.emptyList();
		}

		/**
		 * @return the exchange
		 */
		public Exchange getExchange() {
			return exchange;
		}

		/**
		 * @param exchange
		 *            the exchange to set
		 */
		public void setExchange(Exchange exchange) {
			this.exchange = exchange;
		}
	}

}
