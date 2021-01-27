package org.ksj.server.file.agent.client;

import org.ksj.server.file.agent.util.TelegramUtil;
import org.ksj.server.file.agent.vo.TelegramVo;

public class TcpClient {
	private TcpClientInterface tcpClientInt;
	
	public TelegramVo send(TelegramVo telegramVo) {
		byte[] byteArray = tcpClientInt.send(TelegramUtil.makeTelegram(telegramVo));
		return TelegramUtil.parsing(byteArray);
	}
	
	public void setTcpClientInt(TcpClientInterface tcpClientInt) {
		this.tcpClientInt = tcpClientInt;
	}
	
	
}
