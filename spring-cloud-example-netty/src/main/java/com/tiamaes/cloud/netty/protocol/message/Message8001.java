package com.tiamaes.cloud.netty.protocol.message;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;


public class Message8001 extends Message {
	
	public Message8001(Header header){
		this(header, State.SUCCESS);
	}

	public Message8001(Header header, State state) {
		Body8001 body = new Body8001();
		body.setState(state);
		body.setMessageId(header.getId());
		body.setSerialNo(header.getSerialNo());
		
		Header _header = new Header();
		_header.setId(0x8001);
		_header.setChildPackage(header.isChildPackage());
		_header.setEnableRSA(header.isEnableRSA());
		_header.setTerminalId(header.getTerminalId());
		_header.setTerminalNo(header.getTerminalNo());
		_header.setTerminalType(header.getTerminalType());
		setHeader(_header);
		setBody(body);
	}

	public class Body8001 implements Body {

		private int messageId;
		private int serialNo;
		private State state;

		public int getMessageId() {
			return messageId;
		}

		public void setMessageId(int messageId) {
			this.messageId = messageId;
		}

		public int getSerialNo() {
			return serialNo;
		}

		public void setSerialNo(int serialNo) {
			this.serialNo = serialNo;
		}

		public State getState() {
			return state;
		}

		public void setState(State state) {
			this.state = state;
		}

		@Override
		public byte[] toBytes() {
			StringBuffer bufferStr = new StringBuffer();
			bufferStr.append(String.format("%04X", this.serialNo));
			bufferStr.append(String.format("%04X", this.messageId));
			bufferStr.append(String.format("%02X", this.state.ordinal()));
			try {
				return Hex.decodeHex(bufferStr.toString().toCharArray());
			} catch (DecoderException e) {
				return null;
			}
		}

	}

	public enum State {
		FAILURE, SUCCESS;
	}
}
