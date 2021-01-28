package org.ksj.server.file.agent.util;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;

public class CipherUtil {
	public static byte[] encypt(byte[] plain, String publicKey) {
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
	
	public static byte[] decrypt(byte[] cipherText, String privateKey) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.decodeBase64(privateKey));
			cipher.init(Cipher.DECRYPT_MODE, KeyFactory.getInstance("RSA").generatePrivate(spec));
			return cipher.doFinal(cipherText);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException| InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
			// TODO 에러 정제
			throw new RuntimeException(e);
		}
	}
}
