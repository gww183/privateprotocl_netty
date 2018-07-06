package com.beebank.util;

import java.util.Map;

/**
 * @Description: 头
 * @author gww
 * 2018年6月29日
 */
public class Header {
	// 校验码
	private int crcCode = 0xacef0101;
	// 长度
	private int length;
	// sessionId
	private long sessionId;
	// 类型
	private byte type;
	// 优先级
	private byte priority;
	//扩展
	private Map<String, Object> attachment;
	public int getCrcCode() {
		return crcCode;
	}
	public void setCrcCode(int crcCode) {
		this.crcCode = crcCode;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public long getSessionId() {
		return sessionId;
	}
	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	public byte getPriority() {
		return priority;
	}
	public void setPriority(byte priority) {
		this.priority = priority;
	}
	public Map<String, Object> getAttachment() {
		return attachment;
	}
	public void setAttachment(Map<String, Object> attachment) {
		this.attachment = attachment;
	}
	@Override
	public String toString() {
		return "Header [crcCode = " + crcCode + " , length = " + length + ""
				+ ",sessionId = " + sessionId + ", type = " + type + ", priority = " + priority + ""
						+ ", attachment = " + attachment + "]";
	}
	
	
	
	
}
