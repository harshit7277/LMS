package com.intimetec.lms.validation;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.intimetec.lms.dao.LeaveManageDao;
import com.intimetec.lms.exception.LeaveValidationException;
import com.intimetec.lms.model.Leave;
import com.intimetec.lms.utility.LmsConstants;

@Component("validateLeave")
public class ValidateLeave {
	boolean validate = false;

	@Autowired
	private LeaveManageDao leaveDao;

	public boolean isValidateAllLeaveCondition(Leave leaveDetails, String gender) throws LeaveValidationException {
		boolean currentDateStatus = isValidatecurrentdate(leaveDetails);
		boolean balanceStatus = isValidateLeaveBalance(leaveDetails);
		boolean leaveDateStatus = isValidateLeaveDate(leaveDetails);
		boolean validateLeaveTypeStatus = isValidateLeaveType(leaveDetails, gender);
		if (balanceStatus && leaveDateStatus && currentDateStatus && validateLeaveTypeStatus) {
			validate = true;
		} else {
			validate = false;
		}
		return validate;
	}

	private boolean isValidateLeaveBalance(Leave leaveDetails) throws LeaveValidationException {

		int numberOfLeaves = leaveDetails.getLeavenumber();
		int remainingLeaves = leaveDao.leaveBalance(leaveDetails.getUserId(), leaveDetails.getLeavetype());
		if (numberOfLeaves <= remainingLeaves) {
			validate = true;
		} else {
			throw new LeaveValidationException(LmsConstants.LEAVE_BALANCE_LOW);
		}
		return validate;
	}

	private boolean isValidateLeaveDate(Leave leaveDetails) throws LeaveValidationException {
		ArrayList<Leave> employeeLeaveDate = leaveDao.getEmployeeLeaveDate(leaveDetails.getUserId());
		for (int arrayLength = 0; arrayLength < employeeLeaveDate.size(); arrayLength++) {
			Date dateFrom = employeeLeaveDate.get(arrayLength).getLeaveFrom();
			Date dateTill = employeeLeaveDate.get(arrayLength).getLeaveTill();
			if (leaveDetails.getLeaveFrom().equals(dateFrom) || leaveDetails.getLeaveFrom().equals(dateTill)) {
				throw new LeaveValidationException(LmsConstants.LEAVE_APPLIED_FOR_SAMEDATE);
			} else if (leaveDetails.getLeaveFrom().after(dateFrom) && leaveDetails.getLeaveFrom().before(dateTill)) {
				throw new LeaveValidationException(LmsConstants.LEAVE_APPLIED_FOR_SAMEDATE);
			} else if (leaveDetails.getLeaveTill().after(dateFrom) && leaveDetails.getLeaveFrom().before(dateTill)) {
				throw new LeaveValidationException(LmsConstants.LEAVE_APPLIED_FOR_SAMEDATE);
			} else {
				validate = true;
			}
		}
		return validate;
	}

	private boolean isValidatecurrentdate(Leave leaveDetails) throws LeaveValidationException {
		long millis = System.currentTimeMillis();
		Date dateFrom = leaveDetails.getLeaveFrom();
		java.sql.Date currentDate = new java.sql.Date(millis);
		if (dateFrom.after(currentDate)) {
			validate = true;
		} else {
			throw new LeaveValidationException(LmsConstants.LEAVE_APPLIED_FOR_PASTDATE);
		}
		return validate;
	}

	private static boolean isValidateLeaveType(Leave leaveDetails, String gender) throws LeaveValidationException {
		boolean validateLeaveType = false;
		if (leaveDetails.getLeavetype().equals(LmsConstants.MATERNITY)) {
			if (gender.equals(LmsConstants.FEMALE)) {
				validateLeaveType = true;
			} else {
				validateLeaveType = false;
			}
		} else {
			validateLeaveType = true;
		}
		return validateLeaveType;
	}
}
