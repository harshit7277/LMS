package com.intimetec.lms.service;

import java.sql.Date;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intimetec.lms.security.Encryption;
import com.google.gson.Gson;
import com.intimetec.lms.dao.EmployeeDao;
import com.intimetec.lms.dao.EmployeeLeaveDetails;
import com.intimetec.lms.dao.FetchLeaveDao;
import com.intimetec.lms.dao.LeaveManageDao;
import com.intimetec.lms.exception.LeaveValidationException;
import com.intimetec.lms.exception.PasswordSecurityException;
import com.intimetec.lms.model.Employee;
import com.intimetec.lms.model.EmployeeLeaveType;
import com.intimetec.lms.model.Leave;
import com.intimetec.lms.utility.AddLeaveDate;
import com.intimetec.lms.utility.LmsConstants;
import com.intimetec.lms.validation.ValidateEmployeeLogin;
import com.intimetec.lms.validation.ValidateLeave;

@Service("employeeService")
public class EmployeeService {

	@Autowired
	private EmployeeDao employeeDao;
	@Autowired
	private LeaveManageDao leaveDao;
	@Autowired
	private FetchLeaveDao fetchLeave;
	@Autowired
	private EmployeeLeaveDetails employeeLeaveDetails;
	@Autowired
	private ValidateLeave validateLeave;
	@Autowired
	private ValidateEmployeeLogin validateEmployeeLogin;
	@Autowired
	private MailService mailService;

	final Logger logger = Logger.getLogger(EmployeeService.class.getName());

	public boolean applyLeave(Leave leavedetails, String gender) throws LeaveValidationException {
		boolean isApplied = false;
		Date dateTill = new AddLeaveDate().addFutureDate(leavedetails.getLeaveFrom(), leavedetails.getLeavenumber());
		leavedetails.setLeaveTill(dateTill);
		if (validateLeave.isValidateAllLeaveCondition(leavedetails, gender)) {
			isApplied = requestForLeave(leavedetails);
		}
		System.out.println(isApplied);
		return isApplied;
	}

	public boolean cancelLeave(int leaveId) {
		Leave leave = leaveDao.getLeaveFromLeaveId(leaveId);
		boolean isUpdated = leaveDao.isLeaveStatusUpdated(leaveId, LmsConstants.CANCEL);
		if (leave.getApproval().equalsIgnoreCase(LmsConstants.APPROVED_LEAVE)) {
			if (isUpdated) {
				isUpdated = isEmployeeLeaveUpdated(leaveId);
			}
		}
		return isUpdated;
	}

	public ArrayList<Leave> getLeaveDetails(String currentLoginUserId) {
		ArrayList<Leave> leave = new ArrayList<Leave>();
		leave = (ArrayList<Leave>) fetchLeave.getAllLeaveById(currentLoginUserId);
		return leave;
	}

	public Employee userProfile(String currentLoginUserId) {
		Employee employee = employeeDao.getEmployeeProfile(currentLoginUserId);
		return employee;
	}

	public boolean updatePassword(Employee employee, String oldPassword, String newPassword)
			throws PasswordSecurityException {
		String employeeNewPassword = Encryption.encrypt(newPassword);
		boolean isUpdated = false;
		if (isPasswordValidated(employee.getUsername(), oldPassword)) {
			isUpdated = employeeDao.isPasswordUpdated(employeeNewPassword, employee.getId());
		}
		return isUpdated;
	}

	public EmployeeLeaveType getLeaveBalance(String currentLoginUserId) {
		EmployeeLeaveType leaveBalance;
		leaveBalance = fetchLeave.getEmployeeBalance(currentLoginUserId);
		return leaveBalance;

	}

	private boolean requestForLeave(Leave leavedetails) {
		boolean isRequested = leaveDao.isLeaveRequested(leavedetails);
		if (isRequested) {
			mailService.sendMail(leavedetails);
		}
		return isRequested;
	}

	private boolean isEmployeeLeaveUpdated(int leaveId) {
		Leave leave = leaveDao.getLeaveFromLeaveId(leaveId);
		int totalLeave = leaveDao.leaveBalance(leave.getUserId(), leave.getLeavetype());
		int remainingLeave = totalLeave + leave.getLeavenumber();
		boolean status = employeeLeaveDetails.updateEmployeeLeaveNumber(leave.getUserId(), leave.getLeavetype(),
				remainingLeave);
		return status;
	}

	private boolean isPasswordValidated(String currentLoginUserName, String oldPassword)
			throws PasswordSecurityException {
		boolean result = validateEmployeeLogin.validateEmployeeCredentials(currentLoginUserName,
				Encryption.encrypt(oldPassword));
		return result;
	}

	public String generateToken(Employee employee) {
		Gson gson = new Gson();
		String token = null;
		try {
			token = Encryption.encrypt(gson.toJson(employee));
		} catch (PasswordSecurityException exception) {
			logger.error(exception);
		}
		return token;
	}

	public Employee getEmployeeDetailsFromName(String user) {
		return employeeDao.getEmployeeDetailsFromName(user);
	}

	public Employee getTokenObject(String header) {
		String token = header.toString().substring(LmsConstants.AUTHORIZATION_BEARER.length());
		String decryptedToken = null;
		try {
			decryptedToken = Encryption.decrypt(token);
		} catch (PasswordSecurityException exception) {
			logger.error(exception.getMessage());
		}
		Gson g = new Gson();
		return g.fromJson(decryptedToken, Employee.class);
	}

}
