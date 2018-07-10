package com.beebank.coder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;
import java.util.Map;

import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;

import com.beebank.util.NettyMessage;

public class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage> {
	
	private NettyMarshallingEncoder marshallingEncoder;
	
	public NettyMessageEncoder() {
		marshallingEncoder = MarshallingCodecCFactory.buildMarshallingEncoder();
	}
	
	@Override
	protected void encode(ChannelHandlerContext ctx, NettyMessage msg,
			List<Object> out) throws Exception {
		if(msg == null || msg.getHeader() == null) {
			throw new Exception("The encode message is null");
		}
		try{
			ByteBuf sendBuf = Unpooled.buffer();
			sendBuf.writeInt(msg.getHeader().getCrcCode());
			sendBuf.writeInt(msg.getHeader().getLength());
			sendBuf.writeLong(msg.getHeader().getSessionId());
			sendBuf.writeByte(msg.getHeader().getType());
			sendBuf.writeByte(msg.getHeader().getPriority());
			sendBuf.writeInt(msg.getHeader().getAttachment() == null ? 0 : msg.getHeader().getAttachment().size());

			
			String key = null;
			byte[] keyArray = null;
			Object value = null;
			if(msg.getHeader().getAttachment() != null){
				for(Map.Entry<String, Object> param : msg.getHeader().getAttachment().entrySet()) {
					key = param.getKey();
					keyArray = key.getBytes("UTF-8");
					sendBuf.writeInt(keyArray.length);
					sendBuf.writeBytes(keyArray);
					value = param.getValue();
					marshallingEncoder.encode(ctx, msg, sendBuf);
				}
			}
			 key = null;
			 keyArray = null;
			 value = null;
			 if(msg.getBody() != null) {
				 marshallingEncoder.encode(ctx, msg.getBody(), sendBuf);
			 }
			 
			 int readableBytes = sendBuf.readableBytes();
			 sendBuf.setInt(4, readableBytes);
			 
			 out.add(sendBuf);
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}

}
