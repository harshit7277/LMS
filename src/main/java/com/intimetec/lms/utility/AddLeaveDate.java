package com.intimetec.lms.utility;

import java.sql.Date;
import java.util.Calendar;

public class AddLeaveDate {

	public Date addFutureDate(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, days);
		return new Date(calendar.getTimeInMillis());
	}
}
