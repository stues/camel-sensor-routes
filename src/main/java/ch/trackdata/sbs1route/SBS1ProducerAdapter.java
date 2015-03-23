package ch.trackdata.sbs1route;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reads from the Channel and parses a the given message
 */
public class SBS1ProducerAdapter extends ChannelHandlerAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(SBS1ProducerAdapter.class);

	private ProducerTemplate producerTemplate;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(producerTemplate != null){
			try {
				producerTemplate.sendBody(msg);
			} catch (Exception ex) {
				LOGGER.warn("Send message failed.", ex);
			}
		}
		super.channelRead(ctx, msg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		LOGGER.warn("An error occurred!", cause);
		ctx.close();
	}

	/**
	 * @return the producerTemplate
	 */
	public ProducerTemplate getProducerTemplate() {
		return producerTemplate;
	}

	/**
	 * @param producerTemplate the producerTemplate to set
	 */
	public void setProducerTemplate(ProducerTemplate producerTemplate) {
		this.producerTemplate = producerTemplate;
	}

}
