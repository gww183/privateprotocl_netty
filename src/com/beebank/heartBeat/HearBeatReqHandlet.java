package com.beebank.heartBeat;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.util.concurrent.ScheduledFuture;

import com.beebank.thread.HeartBeatTask;
import com.beebank.util.NettyMessage;

/**
 * @Description: 心跳
 * @author gww
 * 2018年7月2日
 */
public class HearBeatReqHandlet extends ChannelHandlerAdapter implements
		ChannelInboundHandler {
	private volatile ScheduledFuture<?> heartBeat;
	
	@Override
	public void channelActive(ChannelHandlerContext arg0) throws Exception {

	}

	@Override
	public void channelInactive(ChannelHandlerContext arg0) throws Exception {

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		NettyMessage message =  (NettyMessage) msg;
		if(message.getHeader() != null && message.getHeader().getType() == (byte)2) {
			heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatTask(ctx), 0, 5000, TimeUnit.MICROSECONDS);
		} else if (message.getHeader() != null && message.getHeader().getType() == (byte) 4) {
			System.out.println("client receive server heat beat message -->" + message);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext arg0)
			throws Exception {

	}

	@Override
	public void channelRegistered(ChannelHandlerContext arg0) throws Exception {

	}

	@Override
	public void channelUnregistered(ChannelHandlerContext arg0)
			throws Exception {

	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext arg0)
			throws Exception {

	}

	@Override
	public void userEventTriggered(ChannelHandlerContext arg0, Object arg1)
			throws Exception {

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}
	
	
}
