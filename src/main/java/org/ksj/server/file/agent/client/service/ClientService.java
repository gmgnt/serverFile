package org.ksj.server.file.agent.client.service;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.ksj.server.file.agent.client.TcpClient;
import org.ksj.server.file.agent.cnst.Cnst;
import org.ksj.server.file.agent.util.CipherUtil;
import org.ksj.server.file.agent.util.ConvertUtil;
import org.ksj.server.file.agent.util.HashUtil;
import org.ksj.server.file.agent.vo.TelegramVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);
	
	private TcpClient client;
	
	public void sendFile(String sendFilePath, String receiveFilePath) {
		// File Read
		byte[] file = this.readFile(sendFilePath);
		
		// 1. FileSendReady
		String pubKey = this.fileSendReady(file);
		LOGGER.debug("publicKey: {}", pubKey);
		
		// 2. 파일 암호화
		// TODO 암호화 구현 필요
		byte[] encFile = file;
		
		// 3. 암호화 파일 Hash 추출
		// TODO 암호화 파일 Hash 추출
		
		// 4. 암호화 파일 전송
		String encFileHash = this.sendFile(receiveFilePath, encFile);
		LOGGER.debug("encFileHash: {}", encFileHash);
		
	}
	
	
	private String fileSendReady(byte[] file) {
		TelegramVo vo = new TelegramVo();
		vo.setInst(Cnst.INST_REQ_FILE_SEND_READY);
		vo.addData(HashUtil.sha256(file));
		
		LOGGER.debug("file Hash: {}", ConvertUtil.byteArrayToString(vo.getData(0)));
		
		TelegramVo outVo = client.send(vo);
		
		return ConvertUtil.byteArrayToString(outVo.getData(0));
	}
	
	private String sendFile(String filePath, byte[] encFile) {
		TelegramVo vo = new TelegramVo();
		vo.setInst(Cnst.INST_REQ_FILE_SEND);
		vo.addData(filePath.getBytes());
		vo.addData(encFile);
		
		TelegramVo outVo = client.send(vo);
		
		return ConvertUtil.byteArrayToString(outVo.getData(0));
		
	}
	
	private byte[] readFile(String filePath) {
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
