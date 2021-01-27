package org.ksj.server.file.agent.client.service;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.ksj.server.file.agent.client.TcpClient;
import org.ksj.server.file.agent.cnst.Cnst;
import org.ksj.server.file.agent.util.ConvertUtil;
import org.ksj.server.file.agent.vo.TelegramVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);
	
	private TcpClient client;
	
	public void sendFile(String sendFilePath, String receiveFilePath) {
		// 1. FileSendReady
		String pubKey = this.fileSendReady(sendFilePath);
		LOGGER.debug("publicKey: {}", pubKey);
	}
	
	
	private String fileSendReady(String filePath) {
		TelegramVo vo = new TelegramVo();
		vo.setInst(Cnst.INST_REQ_FILE_SEND_READY);
		vo.addData(getHash(filePath));
		
		TelegramVo outVo = client.send(vo);
		
		return ConvertUtil.byteArrayToString(outVo.getData(0));
	}
	
	private byte[] getHash(String filePath) {
		File file = new File(filePath);
		try {
			return FileUtils.readFileToByteArray(file);
		} catch (IOException e) {
			// TODO 에러 정제 필요
			throw new RuntimeException(e);
		}
	}

	public void setClient(TcpClient client) {
		this.client = client;
	}
	
	
}
