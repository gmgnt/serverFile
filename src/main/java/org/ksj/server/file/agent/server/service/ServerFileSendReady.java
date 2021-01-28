package org.ksj.server.file.agent.server.service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base64;
import org.ksj.server.file.agent.cnst.Cnst;
import org.ksj.server.file.agent.server.service.vo.PKIVo;
import org.ksj.server.file.agent.util.CacheUtil;
import org.ksj.server.file.agent.util.ConvertUtil;
import org.ksj.server.file.agent.vo.TelegramVo;
import org.springframework.stereotype.Service;

@Service
public class ServerFileSendReady {
	public TelegramVo fileSendReady(String key, TelegramVo inputVo) {
		// 원래 파일 Hash 저장. 파일 Hash는 origin 데이터 저장되어 있음
		CacheUtil.saveCache(key + Cnst.ORIGIN_FILE_HASH, ConvertUtil.byteArrayToString(inputVo.getData(0)));
		
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
		PKIVo vo = this.Generate(); // 키 생성
		
		CacheUtil.saveCache(key, vo.getPrivateKey()); // cache에 키 저장
		
		return vo.getPublicKey();
	}

	/**
	 * Base64 Encoding된 PrivateKey 및 PublicKey 생성
	 * 
	 * @return PrivateKey, PublicKey
	 */
	private PKIVo Generate() {
		PKIVo vo = new PKIVo();
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(2048, new SecureRandom());

			KeyPair keyPair = generator.generateKeyPair();
			vo.setPrivateKey(Base64.encodeBase64String(keyPair.getPrivate().getEncoded()));
			vo.setPublicKey(Base64.encodeBase64String(keyPair.getPublic().getEncoded()));

		} catch (NoSuchAlgorithmException e) {
			// TODO 에러 상세화
			throw new RuntimeException(e);
		}
		return vo;
	}
}
