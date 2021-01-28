package org.ksj.server.file.agent.server.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.ksj.server.file.agent.cnst.Cnst;
import org.ksj.server.file.agent.util.CacheUtil;
import org.ksj.server.file.agent.util.CipherUtil;
import org.ksj.server.file.agent.util.ConvertUtil;
import org.ksj.server.file.agent.util.HashUtil;
import org.ksj.server.file.agent.vo.TelegramVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerFileReceive {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerFileReceive.class);
	
	public TelegramVo fileReceive(String key, TelegramVo input) {
		// 1. 암호화 파일 Hash 추출
		byte[] encFile = input.getData(1);
		
		byte[] encFileHash = HashUtil.sha256(encFile);
		
		// 2. 복호화 수행
		// PrivateKey 조회
		String privateKey = CacheUtil.getCache(key + Cnst.PRIVATE_KEY); // cache에 키 조회
		// 대칭키 복호화 수행
		byte[] symmeticKey = CipherUtil.decryptAsymmetic(input.getData(2), privateKey);
		byte[] decFile = CipherUtil.decyptSymmetic(encFile, Base64.encodeBase64String(symmeticKey));
		
		// 3. 복호화 파일 Hash 비교
		String decFileHash = Base64.encodeBase64String(HashUtil.sha256(decFile));
		String originFileHash = CacheUtil.getCache(key + Cnst.ORIGIN_FILE_HASH);
		if(!decFileHash.equals(originFileHash)) {
			LOGGER.error("file hash is different. origin File Hash: {}, dec file hash: {}", originFileHash, decFileHash);
			//TODO 에러 정제 필요
			throw new RuntimeException();
		}
		
		// 4. 파일 생성
		String filePath = ConvertUtil.byteArrayToString(input.getData(0));
		
		File file = new File(filePath);
		try(FileOutputStream stream = new FileOutputStream(file)) {
			stream.write(decFile);
		} catch(IOException e) {
			// TODO 에러 정제 필요
			throw new RuntimeException(e);
		}
		
		TelegramVo outVo = new TelegramVo();
		outVo.setInst(Cnst.INST_RES_FILE_SEND);
		outVo.addData(encFileHash);
		
		return outVo;
	}
}
