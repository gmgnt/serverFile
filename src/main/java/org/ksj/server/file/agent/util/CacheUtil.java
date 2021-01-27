package org.ksj.server.file.agent.util;

import java.util.HashMap;
import java.util.Map;

public class CacheUtil {
	
	// TODO Cache로 변경 후 삭제 예정
	private static Map<String, String> cache = new HashMap<String, String>();
	
	public static void saveCache(String key, String value) {
			cache.put(key, value);
	}
	
	public static String getCache(String key) {
		return cache.get(key);
	}
	
	public static String deleteCache(String key) {
		return cache.remove(key);
	}

}
