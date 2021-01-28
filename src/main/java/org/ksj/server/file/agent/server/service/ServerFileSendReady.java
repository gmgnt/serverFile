package org.ksj.server.file.agent.server.service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base64;
import org.ksj.server.file.agent.cnst.Cnst;
import org.ksj.server.file.agent.server.service.vo.PKIVo;
import org.ksj.server.file.agent.util.CacheUtil;
import org.ksj.server.file.agent.util.CipherUtil;
import org.ksj.server.file.agent.vo.TelegramVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ServerFileSendReady {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerFileSendReady.class);
	
	public TelegramVo fileSendReady(String key, TelegramVo inputVo) {
		// 원래 파일 Hash 저장. 파일 Hash는 origin 데이터 저장되어 있음
		CacheUtil.saveCache(key + Cnst.ORIGIN_FILE_HASH, Base64.encodeBase64String(inputVo.getData(0)));
		
		String publicKey = this.savePrivateKeyAndgetPubKey(key + Cnst.PRIVATE_KEY); // 키 생성 및 저장
		
		TelegramVo outVo = new TelegramVo();
		outVo.addData(publicKey.getBytes());
		outVo.setInst(Cnst.INST_RET_FILE_SEND_READY);
		
		return outVo; 
	}
	/**
	 * PrivateKey를 생성하여 Cache에 저장하고 publicKey를 리턴한다.
	 * 
	 * @param key 캐시에 저장할 Key
	 * @return publicKey
	 */
	private String savePrivateKeyAndgetPubKey(String key) {
		PKIVo vo = CipherUtil.createAsymmeticKey(); // 키 생성
		
		LOGGER.info("generate publicKey: {}", vo.getPublicKey());
		
		CacheUtil.saveCache(key, vo.getPrivateKey()); // cache에 키 저장
		
		return vo.getPublicKey();
	}
}
