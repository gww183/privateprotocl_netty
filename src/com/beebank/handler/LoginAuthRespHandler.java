package com.beebank.handler;


import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.beebank.util.Header;
import com.beebank.util.NettyMessage;

public class LoginAuthRespHandler extends ChannelHandlerAdapter implements ChannelInboundHandler {
	
	private Map<String, Boolean> nodeCheck = new ConcurrentHashMap<String, Boolean>();
	
	private String[] whiteList = {"127.0.0.1"};
	
	@Override
	public void channelActive(ChannelHandlerContext channelhandlercontext)
			throws Exception {
		System.out.println("channelActive");
	}

	@Override
	public void channelInactive(ChannelHandlerContext channelhandlercontext)
			throws Exception {
		System.out.println("channelInactive");
		
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx,
			Object msg) throws Exception {
		NettyMessage nettyMessage = (NettyMessage) msg;
		if(nettyMessage.getHeader() != null && nettyMessage.getHeader().getType() == (byte)1) {
			String nodeIndex = ctx.channel().remoteAddress().toString();
			NettyMessage loginResp = null;
			if(nodeCheck.containsKey(nodeCheck)) {
				loginResp = buildResponse((byte) -1);
			} else {
				InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
				String ip = address.getAddress().getHostAddress();
				boolean isOK = false;
				for(String WIP : whiteList) {
					if(WIP.equals(ip)) {
						isOK = true;
						break;
					}
				}
				
				loginResp = isOK ?  buildResponse((byte) 0) : buildResponse((byte) -1);
				
				if(isOK) {
					nodeCheck.put(nodeIndex, true);
				}
				
				System.out.println("The login response is :" +loginResp + "body [" + loginResp.getBody() + "]");
				ctx.writeAndFlush(loginResp);
			}
		} else {
			ctx.fireChannelRead(nettyMessage);
		}
		
	}
	
	/**
	 * 
	 * @Title: buildResponse 
	 * @Description: 
	 * @param b
	 * @return
	 * @author gww
	 * 2018年6月29日
	 */
	private NettyMessage buildResponse(byte b) {
		NettyMessage nettyMessage = new NettyMessage();
		Header header = new Header();
		header.setType((byte)2);
		nettyMessage.setHeader(header);
		nettyMessage.setBody(b);
		return nettyMessage;
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext channelhandlercontext)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void channelRegistered(ChannelHandlerContext channelhandlercontext)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext channelhandlercontext)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void channelWritabilityChanged(
			ChannelHandlerContext channelhandlercontext) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext channelhandlercontext,
			Object obj) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// 清除缓存
		nodeCheck.remove(ctx.channel().remoteAddress().toString());
		ctx.close();
		ctx.fireExceptionCaught(cause);
	}
	
	
}
