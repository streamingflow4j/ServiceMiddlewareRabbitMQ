package com.sample.consumer.util;


import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Transformer;

@MessageEndpoint
public class Listener {

	@Transformer
	public String toTemp(String s){
		System.out.println(s);
		return s+" timeInsert: "+String.valueOf(System.currentTimeMillis());
	}
}
