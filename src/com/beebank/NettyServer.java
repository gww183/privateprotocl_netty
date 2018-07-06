package com.beebank;

import com.beebank.coder.NettyMessageDecoder;
import com.beebank.coder.NettyMessageEncoder;
import com.beebank.handler.LoginAuthRespHandler;
import com.beebank.thread.HeartBeatRespHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * @Description: 
 * @author gww
 * 2018年7月2日
 */
public class NettyServer {
	
	public static void main(String[] arg) {
		NettyServer server = new NettyServer();
		server.bind(8080);
	}
	
	public void bind(int port) {
		
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup);
			bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
				
				@Override
				protected void initChannel(SocketChannel socketChannel) throws Exception {
					socketChannel.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4, -8, 0));
					socketChannel.pipeline().addLast(new NettyMessageEncoder());
					socketChannel.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
					socketChannel.pipeline().addLast(new LoginAuthRespHandler());
//					socketChannel.pipeline().addLast("HeartBeatHandler", new HeartBeatRespHandler());
				}
			});
			ChannelFuture future =  bootstrap.bind(port).sync();
			future.channel().closeFuture().sync();
			System.out.println("Netty server strat ok ");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
		
		
		
		
		
	}
	
}
