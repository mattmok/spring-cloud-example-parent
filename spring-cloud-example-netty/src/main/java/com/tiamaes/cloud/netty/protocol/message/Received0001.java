package com.tiamaes.cloud.netty.protocol.message;

/**
 * 终端通用应答
 * 
 * example: 7e00011000000000010100017e
 * ---------------------------
 * |0001|1000000000|0101|0001|
 * ---------------------------
 * @author Chen
 *
 */
public class Received0001 extends Received {

	private State state;

	public Received0001(byte[] bytes) {
		super(bytes);
		this.state = State.values()[bytes[0]];
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public static enum State {
		FAILURE, SUCCESS;
	}
}
