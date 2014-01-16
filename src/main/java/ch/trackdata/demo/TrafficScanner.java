package ch.trackdata.demo;

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

public class TrafficScanner {
	private static final Logger LOGGER = LoggerFactory.getLogger(TrafficScanner.class); 
	
	private String host;
	private int port;
	
	private SBS1Parser sbs1Parser;
	
	@Required
	public void setHost(String host) {
		this.host = host;
	}
	
	@Required
	public void setPort(int port) {
		this.port = port;
	}

	@Required
	public void setSBS1Parser(SBS1Parser sbs1Parser) {
		this.sbs1Parser = sbs1Parser;
	}
	
	public void launchScanner() {
		LOGGER.info("Launch the scanner.");
		
		Thread worker = new Thread(new Runnable() {
			public void run() {
				startScanner();
			}
		});
		worker.start();

		LOGGER.info("Launch the scanner has passed.");
	}
	
	public void startScanner() {
		LOGGER.info("Connect to host: {}, port: {}", host, port);
		
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			Bootstrap b = new Bootstrap(); // (1)
			b.group(workerGroup); // (2)
			b.channel(NioSocketChannel.class); // (3)
			b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					// Decoders
					ch.pipeline().addLast("frameDecoder", new LineBasedFrameDecoder(200));
					ch.pipeline().addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8));
					LOGGER.info("Adding the scanner to pipeline: {}", sbs1Parser);
					ch.pipeline().addLast(sbs1Parser/*new SBS1Parser()*/);
				}
			});

			// Start the client.
			ChannelFuture f = b.connect(host, port).sync(); // (5)

			// Wait until the connection is closed.
			f.channel().closeFuture().sync();
		} 
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			LOGGER.warn("Process the SBS1 messages failed.", e);
		} 
		finally {
			workerGroup.shutdownGracefully();
		}
		
		LOGGER.info("Scanner has finished.");
	}

}
