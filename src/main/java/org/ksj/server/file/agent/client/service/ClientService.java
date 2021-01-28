package org.ksj.server.file.agent.client.service;

import java.io.File;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
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
		
		// 2. AES 키 생성 및 해당 키 비대칭 암호화 수행
		String symmeticKey = CipherUtil.createSymmeticKey();
		LOGGER.debug("symmeticKey: {}, byte length: {}", symmeticKey, Base64.decodeBase64(symmeticKey).length);
		String encSymmeticKey = Base64.encodeBase64String(CipherUtil.encyptAsymmetic(Base64.decodeBase64(symmeticKey), pubKey));
		
		// 2. 파일 암호화
		byte[] encFile = CipherUtil.encyptSymmetic(file, symmeticKey);
		
		// 3. 암호화 파일 Hash 추출
		String encFileHash = Base64.encodeBase64String(HashUtil.sha256(encFile));
		LOGGER.debug("encFileHash: {}", encFileHash);
		
		// 4. 암호화 파일 전송
		String receiveEncFileHash = this.sendFile(receiveFilePath, encFile, encSymmeticKey);
		LOGGER.debug("encFileHash: {}", receiveEncFileHash);
		
		// 5. 암호화 파일 Hash 검증
		if(!encFileHash.equals(receiveEncFileHash)) {
			LOGGER.error("encFileHash Error. encFileHash: {}, receiveEncFileHash: {}", encFileHash, receiveEncFileHash);
		}
		
	}
	
	
	private String fileSendReady(byte[] file) {
		TelegramVo vo = new TelegramVo();
		vo.setInst(Cnst.INST_REQ_FILE_SEND_READY);
		vo.addData(HashUtil.sha256(file));
		
		LOGGER.debug("file Hash: {}", Base64.encodeBase64String(vo.getData(0)));
		
		TelegramVo outVo = client.send(vo);
		
		return  ConvertUtil.byteArrayToString(outVo.getData(0));
	}
	
	private String sendFile(String filePath, byte[] encFile, String encSymmeticKey) {
		TelegramVo vo = new TelegramVo();
		vo.setInst(Cnst.INST_REQ_FILE_SEND);
		vo.addData(filePath.getBytes());
		vo.addData(encFile);
		vo.addData(Base64.decodeBase64(encSymmeticKey));
		
		TelegramVo outVo = client.send(vo);
		
		return Base64.encodeBase64String(outVo.getData(0));
		
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
