package org.ksj.server.file.agent;

import java.security.Security;
import java.util.Scanner;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.integration.ip.tcp.TcpInboundGateway;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;

public class Main {
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		// ECDSA를 사용하기 위해 프로퍼티 추가
		Security.addProvider(new BouncyCastleProvider());
		
		GenericXmlApplicationContext context = Main.setupContext();
		TcpInboundGateway gateway = (TcpInboundGateway)context.getBean("inGateway");
		AbstractServerConnectionFactory serverConnFactory = context.getBean(AbstractServerConnectionFactory.class);
		
		LOGGER.info("server port: {}", serverConnFactory.getPort());
		
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
		
		LOGGER.debug("Start Server ...");
		
		context.load("classpath:tcp.xml");
		context.registerShutdownHook();
		context.refresh();
		
		return context;
	}
}
