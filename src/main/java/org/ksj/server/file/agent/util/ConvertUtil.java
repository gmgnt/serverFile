package org.ksj.server.file.agent.util;

public class ConvertUtil {

	public static long byteArrayToLong(byte[] byteArray) {
		return Long.valueOf(byteArrayToString(byteArray));
	}
	
	public static int byteArrayToInt(byte[] byteArray) {
		return Integer.valueOf(byteArrayToString(byteArray));
	}
	
	public static String byteArrayToString(byte[] byteArray) {
		StringBuffer buffer = new StringBuffer();
		for(int i = 0 ; i < byteArray.length ; ++i) {
			buffer.append((char)byteArray[i]);
		}
		
		return buffer.toString();
	}
}
