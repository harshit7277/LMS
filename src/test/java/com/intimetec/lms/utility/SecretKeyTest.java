package com.intimetec.lms.utility;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import org.junit.Test;

public class SecretKeyTest {

	@Test
	public void converKeyInByteTest() throws IOException {
		assertNotNull(SecretKey.getSecretKey());
	}

}
