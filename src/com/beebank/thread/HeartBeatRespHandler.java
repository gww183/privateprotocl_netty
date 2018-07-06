package com.beebank.thread;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;

import com.beebank.util.Header;
import com.beebank.util.NettyMessage;

public class HeartBeatRespHandler extends ChannelHandlerAdapter implements
		ChannelInboundHandler {

	@Override
	public void channelActive(ChannelHandlerContext arg0) throws Exception {

	}

	@Override
	public void channelInactive(ChannelHandlerContext arg0) throws Exception {

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		NettyMessage nettyMessage = (NettyMessage)msg;
		if(nettyMessage.getHeader() != null &&
				nettyMessage.getHeader().getType() == (byte) 3) {
			System.out.println("Receive client heart beat message -->" + nettyMessage);
			NettyMessage heartBeat = buildHeartBeat();
			System.out.println("Send heart beat response message to client : -->" + heartBeat);
			ctx.writeAndFlush(heartBeat);
		} else {
			ctx.fireChannelRead(nettyMessage);
		}
		
	}

	private NettyMessage buildHeartBeat() {
		NettyMessage heatBeat = new NettyMessage();
		Header header = new Header();
		header.setType((byte)4);
		heatBeat.setHeader(header);
		return heatBeat;
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
		cause.printStackTrace();
		ctx.close();
	}
	
	

}
