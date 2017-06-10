package com.tiamaes.cloud.netty.protocol.utils;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.DecoderException;
public class ToASCII {
	//字符串转成ascii码
	public static String StringToASCII(String param){
		byte[] bytes = new byte[param.length()/2];
		for(int i=0;i<bytes.length;i++){  
			try {
			    bytes[i] = (byte) (0xff & Integer.parseInt(param.substring(  
					    i * 2, i * 2 + 2), 16));  

			} catch (Exception e) {
				e.printStackTrace();
			} 						   
		}
		String s="";
		try {
			 s = new String(bytes,"ASCII");
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;
	}
	//ascii码转字符串
	public static String StringToASCII1(String param) throws DecoderException{
		StringBuffer buffer=new StringBuffer();
			param=param.toUpperCase();
			char[] char1 = param.toCharArray();
			for(int i=0;i<char1.length;i++){
				int a=char1[i];
				String b=Integer.toHexString(a);
				buffer.append(b);
			}
			String str=buffer.toString();
			return str;
		
		
	}
}
