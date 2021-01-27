package org.ksj.server.file.agent;

import java.security.Security;
import java.util.Scanner;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.ksj.server.file.agent.client.service.ClientService;
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
//		TcpInboundGateway gateway = (TcpInboundGateway)context.getBean("inGateway");
		ClientService clientService = (ClientService)context.getBean("clientService");
		AbstractServerConnectionFactory serverConnFactory = context.getBean(AbstractServerConnectionFactory.class);
		
		LOGGER.info("server port: {}", serverConnFactory.getPort());
		
		//TODO 서버가 기동이 안될때 처리필요
//		if(!serverConnFactory.isListening()) {
//			LOGGER.error("System Shutdown... Server is not listening");
//			System.exit(1);
//			
//			return;
//		}
		
		Scanner scanner = new Scanner(System.in);
		// CLI 명령어 Input
		while (true) {

			String[] input = scanner.nextLine().split(" ");

			// 종료
			if ("quit".equals(input[0].trim())) {
				break;
			}
			
			switch(input[0]) {
			case "send":
				clientService.sendFile(input[1], input[2]);
				break;
			default:
				LOGGER.info("{} is not INST.", input[0]);
				System.out.println(input[0] + " is not INST.");
				break;
			}
			
		}
		
		scanner.close();
		LOGGER.info("Exiting application");
		System.exit(0);

	}

	static GenericXmlApplicationContext setupContext() {
		GenericXmlApplicationContext context = new GenericXmlApplicationContext();
		
		LOGGER.debug("Start Server ...");
		
		context.load("classpath:server.xml","classpath:client.xml","classpath:beans.xml");
		context.registerShutdownHook();
		context.refresh();
		
		return context;
	}
}
