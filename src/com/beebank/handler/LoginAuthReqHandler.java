package com.beebank.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;

import com.beebank.util.Header;
import com.beebank.util.NettyMessage;

public class LoginAuthReqHandler extends ChannelHandlerAdapter implements
		ChannelInboundHandler {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(buildLoginReq());
	}

	private Object buildLoginReq() {
		NettyMessage nettyMessage = new NettyMessage();
		Header header = new Header();
		header.setType((byte)1);
		nettyMessage.setHeader(header);
		nettyMessage.setBody("It is request");
		return nettyMessage;
	}

	@Override
	public void channelInactive(ChannelHandlerContext arg0) throws Exception {

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		NettyMessage nettyMessage = (NettyMessage) msg;
		if(nettyMessage.getHeader() != null && nettyMessage.getHeader().getType() == (byte)2) {
			System.out.println("Receive from server response");
		}
		
		ctx.fireChannelRead(msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx)
			throws Exception {
		ctx.flush();
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
		ctx.close();
	}
	

}
