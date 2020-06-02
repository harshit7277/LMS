package com.intimetec.lms.security;

import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.intimetec.lms.exception.PasswordSecurityException;

@RunWith(Parameterized.class)
public class EncryptionTest {

	@Parameter(0)
	public String data;

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { "rahul" }, { "hk" }, { "pk" } };
		return Arrays.asList(data);
	}

	@Test
	public void encryptTest() throws PasswordSecurityException {
		assertNotNull(Encryption.encrypt(data));
	}

	@Test
	public void decryptTest() throws PasswordSecurityException {
		assertNotNull(Encryption.decrypt(Encryption.encrypt(data)));
	}
}
