package com.intimetec.lms.utility;

import static org.junit.Assert.assertNotNull;

import java.sql.Date;

import org.junit.Before;
import org.junit.Test;

public class AddLeaveDateTest {

	AddLeaveDate addLeaveDate;
	@Before
	public void createObject() {
		addLeaveDate = new AddLeaveDate();
	}
	
	@Test
	public void addFutureDateTest() {
		Date date = Date.valueOf("2020-04-08");
		assertNotNull(addLeaveDate.addFutureDate(date,3));
	}

}
