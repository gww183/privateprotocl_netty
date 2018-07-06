package com.beebank.thread;

import io.netty.channel.ChannelHandlerContext;

import com.beebank.util.Header;
import com.beebank.util.NettyMessage;

public class HeartBeatTask implements Runnable {
	
	private final ChannelHandlerContext ctx;
	
	public HeartBeatTask(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}
	
	@Override
	public void run() {
		NettyMessage heatBeat = buildHeatBeat();
		System.out.println("Client sent heart beat message to server" + heatBeat);
		ctx.writeAndFlush(heatBeat);
	}

	private NettyMessage buildHeatBeat() {
		NettyMessage heatBeat = new NettyMessage();
		Header header = new Header();
		header.setType((byte)3);
		heatBeat.setHeader(header);
		return heatBeat;
	}

}
