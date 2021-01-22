package org.ksj.server.file.agent;

import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;

public class Main {

	public static void main(String[] args) {
		GenericXmlApplicationContext context = Main.setupContext();
		AbstractServerConnectionFactory serverConnFactory = context.getBean(AbstractServerConnectionFactory.class);
	}

	static GenericXmlApplicationContext setupContext() {
		return null;
	}
}
