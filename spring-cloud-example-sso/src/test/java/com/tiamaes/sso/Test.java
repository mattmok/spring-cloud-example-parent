package com.tiamaes.sso;

import org.apache.commons.codec.binary.Hex;

public class Test {
	
	@org.junit.Test
	public void test1() throws Exception{
		byte[] bytes = new byte[5];
		bytes[0] = 0;
		bytes[1] = 0;
		bytes[2] = 0;
		bytes[3] = 0;
		bytes[4] = 1;
		
		System.out.println(new String(Hex.encodeHex(bytes)));
		
		byte[] bytes2 = "00001".getBytes();
		for(byte b : bytes2){
			System.out.println(b);
		}
	}
}
