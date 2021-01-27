package org.ksj.server.file.agent;

import org.ksj.server.file.agent.server.service.ServerFileSendReady;
import org.ksj.server.file.agent.util.TelegramUtil;
import org.ksj.server.file.agent.vo.TelegramVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;



public class TestServiceActivator {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestServiceActivator.class);

	private static final String IP_TCP_REMOTE_PORT = "ip_tcp_remotePort";
	
	private ServerFileSendReady fileSendReady;

	public Message<byte[]> echo(Message<byte[]> msg) {
		// 현재 접속 중인 클라이언트 구분을 위한 값을 헤더에서 추출
		int remotePort = (Integer) msg.getHeaders().get(IP_TCP_REMOTE_PORT);
		String key = String.valueOf(remotePort); // cache에서 사용하게 될 prefix
		LOGGER.debug(String.valueOf(remotePort));

		byte[] byteArray = msg.getPayload();
		// Vo형태로 전문 파싱
		TelegramVo inputVo = TelegramUtil.parsing(byteArray);
		LOGGER.debug(inputVo.toString());

		TelegramVo outVo = null;
		switch (inputVo.getInst()) {
		case 1000:
			outVo = fileSendReady.fileSendReady(key, inputVo);
			break;
		default:
			throw new RuntimeException();
		}

		
		Message<byte[]> retMessage = MessageBuilder.withPayload(TelegramUtil.makeTelegram(outVo))
//		        .setHeader("foo", "bar")
				.build();

		return retMessage;
	}

	public void setFileSendReady(ServerFileSendReady fileSendReady) {
		this.fileSendReady = fileSendReady;
	}
}
