package com.intimetec.lms.security;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.intimetec.lms.exception.PasswordSecurityException;
import com.intimetec.lms.utility.SecretKey;

import java.util.Base64;

public class Encryption {

	private static final String ALGO = "AES";

	private static byte[] getSecretKey() throws IOException {
		return SecretKey.getSecretKey();
	}

	public static String encrypt(String data) throws PasswordSecurityException {
		byte[] encodedValue = null;
		try {
			Key key = generateKey();
			Cipher cipher = Cipher.getInstance(ALGO);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			encodedValue = cipher.doFinal(data.getBytes());
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| IOException | BadPaddingException exception) {
			throw new PasswordSecurityException(exception.getMessage());
		}
		return Base64.getEncoder().encodeToString(encodedValue);

	}

	public static String decrypt(String encryptedData) throws PasswordSecurityException {
		byte[] decryptValue = null;
		try {
			Key key = generateKey();
			Cipher cipher = Cipher.getInstance(ALGO);
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decordedValue = Base64.getDecoder().decode(encryptedData);
			decryptValue = cipher.doFinal(decordedValue);

		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| IOException | BadPaddingException exception) {
			throw new PasswordSecurityException(exception.getMessage());
		}
		return new String(decryptValue);
	}

	private static Key generateKey() throws IOException {
		return new SecretKeySpec(getSecretKey(), ALGO);
	}
}
