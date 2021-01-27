package org.ksj.server.file.agent.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class CipherUtil {
	public static byte[] encypt(byte[] plain, String publicKey) {
		try {
			Cipher cipher = Cipher.getInstance("ECDSA", Security.getProvider("BC"));
			SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.decodeBase64(publicKey),"ECDSA");
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			return cipher.doFinal(plain);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
			// TODO 에러 정제
			throw new RuntimeException(e);
		}
	}
}
