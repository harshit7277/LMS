package com.intimetec.lms.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.intimetec.lms.exception.PasswordSecurityException;
import com.intimetec.lms.exception.PermissionNotGrantedException;
import com.intimetec.lms.model.Employee;
import com.intimetec.lms.model.EmployeeLeaveType;
import com.intimetec.lms.service.AdminService;
import com.intimetec.lms.service.EmployeeService;
import com.intimetec.lms.utility.LmsConstants;

@RestController
@RequestMapping(value = "/manageEmployees")
public class ManageEmployee {

	final static Logger LOGGER = Logger.getLogger(ManageEmployee.class.getName());

	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private AdminService adminService;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<String> addEmployee(@RequestBody Employee employee, HttpServletRequest request) {
		Employee currentLoginEmployee = employeeService.getTokenObject(request.getHeader(LmsConstants.AUTHORIZATION));
		boolean isAdded = false;
		ResponseEntity<String> response = null;
		try {
			isAdded = adminService.addEmployee(employee, currentLoginEmployee.getDesignation());
		} catch (PasswordSecurityException | PermissionNotGrantedException | SQLException exception) {
			LOGGER.error(exception.getMessage());
			response = internalServerException(exception);
		}
		if (isAdded) {
			response = new ResponseEntity<String>(LmsConstants.USER_ADDED, HttpStatus.CREATED);
		}
		return response;
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<String> updateEmployee(@RequestBody Employee employeeDataFromRequest,
			HttpServletRequest request) {
		Employee currentLoginEmployee = employeeService.getTokenObject(request.getHeader(LmsConstants.AUTHORIZATION));
		boolean isUpdate = false;
		ResponseEntity<String> response = null;
		try {
			isUpdate = adminService.updateEmployee(employeeDataFromRequest, currentLoginEmployee.getDesignation());
		} catch (PermissionNotGrantedException exception) {
			response = new ResponseEntity<String>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			LOGGER.error(exception.getMessage());
		}
		if (isUpdate) {
			response = new ResponseEntity<String>(LmsConstants.USER_UPDATED, HttpStatus.OK);
		}
		return response;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteEmployee(@RequestBody MultiValueMap<String, String> formParams,
			HttpServletRequest request) {
		boolean isDeleted = false;
		ResponseEntity<String> response = null;
		Employee currentLoginEmployee = employeeService.getTokenObject(request.getHeader(LmsConstants.AUTHORIZATION));
		try {
			isDeleted = adminService.deleteEmployee(formParams.getFirst(LmsConstants.EMPLOYEE_ID),
					currentLoginEmployee.getDesignation());
		} catch (PermissionNotGrantedException exception) {
			response = new ResponseEntity<String>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			LOGGER.error(exception.getMessage());
		}
		if (isDeleted) {
			response = new ResponseEntity<String>(LmsConstants.USER_DELETED, HttpStatus.OK);
		}
		return response;
	}

	@RequestMapping(value = "/leaveBalance", method = RequestMethod.POST)
	public ResponseEntity<EmployeeLeaveType> getLeaveBalance(@RequestParam(LmsConstants.EMPLOYEE_ID) String id) {
		EmployeeLeaveType employeeLeaveBalance = adminService.getLeaveBalance(id);
		return new ResponseEntity<EmployeeLeaveType>(employeeLeaveBalance, HttpStatus.OK);
	}

	@RequestMapping(value = "/updatePassword", method = RequestMethod.PUT)
	public ResponseEntity<String> updatePassword(@RequestBody MultiValueMap<String, String> formParams,
			HttpServletRequest request) {
		Employee currentLoginEmployee = employeeService.getTokenObject(request.getHeader(LmsConstants.AUTHORIZATION));
		ResponseEntity<String> response = null;
		boolean isPasswordUpdated = false;
		try {
			isPasswordUpdated = employeeService.updatePassword(currentLoginEmployee,
					formParams.getFirst(LmsConstants.EMPLOYEE_OLDPASSWORD),
					formParams.getFirst(LmsConstants.EMPLOYEE_NEWPASSWORD));
		} catch (PasswordSecurityException exception) {
			LOGGER.error(exception.getMessage());
			response = internalServerException(exception);
		}
		if (isPasswordUpdated) {
			response = new ResponseEntity<String>(LmsConstants.PASSWORD_CHANGED, HttpStatus.OK);
		}
		return response;
	}

	private ResponseEntity<String> internalServerException(Exception exception) {
		return new ResponseEntity<String>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
