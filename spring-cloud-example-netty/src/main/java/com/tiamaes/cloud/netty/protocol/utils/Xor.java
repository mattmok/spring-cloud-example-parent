package com.tiamaes.cloud.netty.protocol.utils;

import org.apache.commons.lang.ArrayUtils;

public class Xor {

	/**
	 * 校验校验码
	 * 
	 * @param messageBytes
	 * @return
	 */
	public static boolean check(byte[] messageBytes) {
		if (messageBytes.length >= 1) {
			int i = messageBytes.length - 1;
			byte[] bytes = ArrayUtils.remove(messageBytes, i);
			byte code = messageBytes[i];
			return xorValue(bytes) == code;
		} else {
			return false;
		}
	}

	/**
	 * 获取校验值
	 * 
	 * @param data
	 * @param pos
	 * @param len
	 * @return
	 */
	public static byte xorValue(byte[] bytes) {
		byte value = 0;
		for (int i = 0; i < bytes.length; i++) {
			value ^= bytes[i];
		}
		return value;
	}
}
