package org.ksj.server.file.agent.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.util.Arrays;
import org.ksj.server.file.agent.server.service.vo.PKIVo;

public class CipherUtil {
	/**
	 * Base64 Encoding된 PrivateKey 및 PublicKey 생성
	 * 
	 * @return PrivateKey, PublicKey
	 */
	public static PKIVo createAsymmeticKey() {
		PKIVo vo = new PKIVo();
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(3072, new SecureRandom());

			KeyPair keyPair = generator.generateKeyPair();
			vo.setPrivateKey(Base64.encodeBase64String(keyPair.getPrivate().getEncoded()));
			vo.setPublicKey(Base64.encodeBase64String(keyPair.getPublic().getEncoded()));

		} catch (NoSuchAlgorithmException e) {
			// TODO 에러 상세화
			throw new RuntimeException(e);
		}
		return vo;
	}
	/**
	 * PublicKey를 이용하여 평문을 암호화한다.
	 * 
	 * @param plain 평문
	 * @param publicKey Base64인코딩된 PublicKey
 	 * @return cipher Text
	 */
	public static byte[] encyptAsymmetic(byte[] plain, String publicKey) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
			cipher.init(Cipher.ENCRYPT_MODE, KeyFactory.getInstance("RSA").generatePublic(spec));
			return cipher.doFinal(plain);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | InvalidKeySpecException e) {
			// TODO 에러 정제
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * PrivateKey를 이용하여 cipherText를 평문으로 복호화한다.
	 * 
	 * @param cipherText 암호화된 문장
	 * @param privateKey Base64 인코딩된 PrivateKey
	 * @return Plain text
	 */
	public static byte[] decryptAsymmetic(byte[] cipherText, String privateKey) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			PKCS8EncodedKeySpec  spec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
			cipher.init(Cipher.DECRYPT_MODE, KeyFactory.getInstance("RSA").generatePrivate(spec));
			return cipher.doFinal(cipherText);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException| InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
			// TODO 에러 정제
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 대칭키 암호화에 필요한 키를 생성한다.
	 *  
	 * @return Base64 Encoding된 키
	 */
	public static String createSymmeticKey() {
		try {
			KeyGenerator generator = KeyGenerator.getInstance("AES");
			generator.init(256, new SecureRandom());
			return Base64.encodeBase64String(generator.generateKey().getEncoded());
		} catch (NoSuchAlgorithmException e) {
			// TODO 에러 정제
			throw new RuntimeException();
		}
	}
	
	public static byte[] encyptSymmetic(byte[] plain, String key) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			SecretKeySpec spec = new SecretKeySpec(Base64.decodeBase64(key), "AES");
			cipher.init(Cipher.ENCRYPT_MODE, spec, new IvParameterSpec(Arrays.copyOfRange(Base64.decodeBase64(key), 0, 16)));
			return cipher.doFinal(plain);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
			// TODO 에러 정제
			throw new RuntimeException(e);
		}
	}
	
	public static byte[] decyptSymmetic(byte[] cipherText, String key) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			SecretKeySpec spec = new SecretKeySpec(Base64.decodeBase64(key), "AES");
			cipher.init(Cipher.DECRYPT_MODE, spec, new IvParameterSpec(Arrays.copyOfRange(Base64.decodeBase64(key), 0, 16)));
			return cipher.doFinal(cipherText);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
			// TODO 에러 정제
			throw new RuntimeException(e);
		}
	}	
}
