package com.tiamaes.cloud.netty;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import com.tiamaes.cloud.netty.protocol.message.Header;
import com.tiamaes.cloud.netty.protocol.message.Received;

public class FactoryTest {

	@Test
	public void test() throws Exception{
		
		Header header = new Header();
		header.setId(0x0001);
		byte[] bytes = new byte[]{0};
		Received received = Received.getInstance(header, bytes);
		
		System.out.println(received);
	}
	
	@Test
	public void test2(){
		byte delimiter = 0x7E;
		
		char[] chars = Hex.encodeHex(new byte[]{delimiter});
		
		System.out.println(chars);
		for(char ch : chars){
			System.out.println((byte) Character.toUpperCase(ch));
		}
	}
}
