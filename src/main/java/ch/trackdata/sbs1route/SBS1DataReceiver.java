package ch.trackdata.sbs1route;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Opens a TCP connection to the given host on the given port,
 * The {@link SBS1ProducerAdapter} receives the data from the Channel 
 */
public class SBS1DataReceiver {
	private static final Logger LOGGER = LoggerFactory.getLogger(SBS1DataReceiver.class); 
	
	private String host;
	private int port;
	private boolean enabled = true;
	
	private SBS1ProducerAdapter sbs1ProducerAdapter;
	
	/**
	 * @param host the host name
	 */
	@Required
	public void setHost(String host) {
		this.host = host;
	}
	
	/**
	 * @param port the port of the host
	 */
	@Required
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @param sbs1ProducerAdapter the {@link SBS1ProducerAdapter}
	 */
	@Required
	public void setSBS1ProducerAdapter(SBS1ProducerAdapter sbs1ProducerAdapter) {
		this.sbs1ProducerAdapter = sbs1ProducerAdapter;
	}
	
	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	/**
	 * Launches the data receiver in a new worker Thread
	 */
	public void launchDataReceiver() {
		if(isEnabled()){
		LOGGER.info("Launch the SBS1 data receiver.");
		
		Thread worker = new Thread(new Runnable() {
			public void run() {
				startDataReceiver();
			}
		});
		worker.start();

		LOGGER.info("Launch the sbs1 data receiver has passed.");
		}
		else {
			LOGGER.info("SBS1 Data Receiver is disabled");
		}
	}
	
	/**
	 * Start receiving data from the {@link SocketChannel}
	 */
	public void startDataReceiver() {
		LOGGER.info("Connect to host: {}, port: {}", host, port);
		
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			Bootstrap bootstrap = new Bootstrap(); // (1)
			bootstrap.group(workerGroup); // (2)
			bootstrap.channel(NioSocketChannel.class); // (3)
			bootstrap.option(ChannelOption.SO_KEEPALIVE, true); // (4)
			bootstrap.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel channel) throws Exception {
					// Decoders
					channel.pipeline().addLast("frameDecoder", new LineBasedFrameDecoder(200));
					channel.pipeline().addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8));
					LOGGER.info("Adding the scanner to pipeline: {}", sbs1ProducerAdapter);
					channel.pipeline().addLast(sbs1ProducerAdapter);
				}
			});

			// Start the client.
			ChannelFuture f = bootstrap.connect(host, port).sync(); // (5)

			// Wait until the connection is closed.
			f.channel().closeFuture().sync();
		} 
		catch (InterruptedException e) {
			LOGGER.warn("Process the SBS1 messages failed.", e);
		} 
		finally {
			workerGroup.shutdownGracefully();
		}
		
		LOGGER.info("Scanner has finished.");
	}
}
