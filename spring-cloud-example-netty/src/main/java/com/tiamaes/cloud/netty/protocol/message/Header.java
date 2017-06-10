package com.tiamaes.cloud.netty.protocol.message;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class Header implements Protocol{
	/**
	 * 消息编号
	 */
	private int id;
	/**
	 * 是否分包
	 */
	private boolean childPackage;
	/**
	 * 是否使用RAS加密
	 */
	private boolean enableRSA;
	/**
	 * 终端编号（对应数据库）
	 */
	private String terminalId;
	/**
	 * 终端出厂编号
	 */
	private String terminalNo;
	/**
	 * 终端类型
	 */
	private Type terminalType;
	/**
	 * 消息流水号
	 */
	private int serialNo;
	/**
	 * 消息体长度
	 */
	private int length;
	/**
	 * 消息总包数
	 */
	private int packetTotal;
	/**
	 * 包序号
	 */
	private int packetNo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isChildPackage() {
		return childPackage;
	}

	public void setChildPackage(boolean childPackage) {
		this.childPackage = childPackage;
	}

	public boolean isEnableRSA() {
		return enableRSA;
	}

	public void setEnableRSA(boolean enableRSA) {
		this.enableRSA = enableRSA;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getTerminalNo() {
		return terminalNo;
	}

	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}

	public Type getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(Type terminalType) {
		this.terminalType = terminalType;
	}

	public int getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

	public int getPacketTotal() {
		return packetTotal;
	}

	public void setPacketTotal(int packetTotal) {
		this.packetTotal = packetTotal;
	}

	public int getPacketNo() {
		return packetNo;
	}

	public void setPacketNo(int packetNo) {
		this.packetNo = packetNo;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(String.format("%04x", this.id));
		int property = length;
		if (this.enableRSA) {
			property = 0x0400 | property;
		}
		if (this.childPackage) {
			property = 0x2000 | property;
		}
		buffer.append(String.format("%04x", property));
		buffer.append(String.format("%012d", Integer.parseInt(terminalId)));
		buffer.append(String.format("%012d", Integer.parseInt(terminalNo)));
		buffer.append(String.format("%02x", getTerminalType().ordinal()));
		buffer.append(String.format("%04x", getSerialNo()));
		if (this.childPackage) {
			buffer.append(String.format("%04x", getPacketTotal()));
			buffer.append(String.format("%04x", getPacketNo()));
		}
		return buffer.toString().toLowerCase();

	}

	@Override
	public byte[] toBytes() {
		try {
			return Hex.decodeHex(toString().toCharArray());
		} catch (DecoderException e) {
			return null;
		}
	}

	public enum Type {
		NONE, LOCK, STATION, DEVICE;
	}
}