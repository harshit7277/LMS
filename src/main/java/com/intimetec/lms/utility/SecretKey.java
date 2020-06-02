package com.intimetec.lms.utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public final class SecretKey {

	public static byte[] getSecretKey() throws IOException {
		Resource resource = new ClassPathResource("secretkey.properties");
		InputStream input = resource.getInputStream();
		Properties properties = new Properties();
		properties.load(input);
		String key = properties.getProperty("key");
		return converKeyInByte(key);

	}

	private static byte[] converKeyInByte(String key) {
		byte[] secretKey = new byte[key.length()];
		for (int i = 0; i < key.length(); i++) {
			secretKey[i] = (byte) key.charAt(i);
		}
		return secretKey;
	}
}
