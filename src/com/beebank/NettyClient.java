package com.beebank;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.beebank.coder.NettyMessageDecoder;
import com.beebank.coder.NettyMessageEncoder;
import com.beebank.handler.LoginAuthReqHandler;
import com.beebank.heartBeat.HearBeatReqHandlet;

public class NettyClient {
	
	public static void main(String arg[]) {
		NettyClient client = new NettyClient();
		client.connent(8080);
	}
	
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	
	EventLoopGroup group =  new NioEventLoopGroup();
	
	public void connent(int port) {
		try{
			Bootstrap b = new Bootstrap();
			b.group(group);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.TCP_NODELAY, true);
			b.handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel socketChannel) throws Exception {
					socketChannel.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4, -8, 0));
					socketChannel.pipeline().addLast(new NettyMessageEncoder());
					socketChannel.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
					socketChannel.pipeline().addLast(new LoginAuthReqHandler());
					socketChannel.pipeline().addLast("HeartBeatHandler", new HearBeatReqHandlet());
				}
			});
			
			ChannelFuture future = b.connect("127.0.0.1", port).sync();
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			executor.execute(new Runnable() {
				
				@Override
				public void run() {
					try{
						TimeUnit.SECONDS.sleep(5);
						connent(port);
					} catch(Exception e) {
						
					}
				}
			});
		}
	}
	
	
	
}
