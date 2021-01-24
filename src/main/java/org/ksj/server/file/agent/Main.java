package org.ksj.server.file.agent;

import java.util.Scanner;

import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.integration.ip.tcp.TcpInboundGateway;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;

public class Main {

	public static void main(String[] args) {
		GenericXmlApplicationContext context = Main.setupContext();
		TcpInboundGateway gateway = (TcpInboundGateway)context.getBean("inGateway");
		AbstractServerConnectionFactory serverConnFactory = context.getBean(AbstractServerConnectionFactory.class);
		
		System.out.println("server port: " + serverConnFactory.getPort());
		
		Scanner scanner = new Scanner(System.in);
		while (true) {

			final String input = scanner.nextLine();

			if ("q".equals(input.trim())) {
				break;
			}
			else {
				System.out.println("else rararara");
//				String result = gateway.
//				System.out.println(result);
			}
		}

		System.out.println("Exiting application...bye.");
		System.exit(0);

	}

	static GenericXmlApplicationContext setupContext() {
		GenericXmlApplicationContext context = new GenericXmlApplicationContext();
		
		System.out.println("Start Server ...");
		
		context.load("classpath:tcp.xml");
		context.registerShutdownHook();
		context.refresh();
		
		return context;
	}
}
