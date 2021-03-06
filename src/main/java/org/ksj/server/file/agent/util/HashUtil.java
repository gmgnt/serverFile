package org.ksj.server.file.agent.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

public class HashUtil {

	public static byte[] sha256(String msg) {
		try {
	    MessageDigest md = MessageDigest.getInstance("SHA-256");
	    md.update(msg.getBytes());
	    
	    return  Base64.encodeBase64String(md.digest()).getBytes();
		} catch(NoSuchAlgorithmException e) {
			//TODO RuntimeException 정제
			throw new RuntimeException(e);
		}
	}
	
	public static byte[] sha256(byte[] msg) {
		try {
	    MessageDigest md = MessageDigest.getInstance("SHA-256");
	    md.update(msg);
	    
	    return Base64.encodeBase64String(md.digest()).getBytes();
		} catch(NoSuchAlgorithmException e) {
			//TODO RuntimeException 정제
			throw new RuntimeException(e);
		}
	}
	
	public static byte[] sha128(byte[] msg) {
		try {
	    MessageDigest md = MessageDigest.getInstance("SHA1");
	    md.update(msg);
	    
	    return Base64.encodeBase64String(md.digest()).getBytes();
		} catch(NoSuchAlgorithmException e) {
			//TODO RuntimeException 정제
			throw new RuntimeException(e);
		}
	}

}
