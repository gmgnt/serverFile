package org.ksj.server.file.agent;

import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

public class TestServiceActivator {
	
	public Message echo(Message<Byte[]> msg) {
		System.out.println(msg.getPayload());
		System.out.println(msg.getHeaders());
		
		Message<String> message1 = MessageBuilder.withPayload("ksj")
		        .setHeader("foo", "bar")
		        .build();

		return message1;
	}

}
