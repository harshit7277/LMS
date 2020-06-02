package com.intimetec.lms.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.intimetec.lms.security.Encryption;
import com.intimetec.lms.service.EmployeeService;
import com.intimetec.lms.exception.PasswordSecurityException;
import com.intimetec.lms.model.Employee;
import com.intimetec.lms.utility.LmsConstants;
import com.intimetec.lms.validation.ValidateEmployeeLogin;

@Controller
public class EmployeeLogin {

	final Logger LOGGER = Logger.getLogger(EmployeeLogin.class.getName());
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private ValidateEmployeeLogin validateEmployeeLogin;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<String> doLogin(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
		HttpSession session = servletRequest.getSession();
		ResponseEntity<String> response;
		boolean result = false;
		try {
			result = validateEmployeeLogin.validateEmployeeCredentials(
					servletRequest.getParameter(LmsConstants.EMPLOYEE_NAME),
					Encryption.encrypt(servletRequest.getParameter(LmsConstants.EMPLOYEE_PASSWORD)));
		} catch (PasswordSecurityException exception) {
			LOGGER.error(exception.getMessage());
			response = new ResponseEntity<String>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}
		if (result) {
			Employee employee = null;
			employee = employeeService
					.getEmployeeDetailsFromName(servletRequest.getParameter(LmsConstants.EMPLOYEE_NAME));
			String token = employeeService.generateToken(employee);
			session.setAttribute(LmsConstants.TOKEN, token);
			session.setAttribute(LmsConstants.EMPLOYEE_NAME, employee.getUsername());
			session.setAttribute(LmsConstants.EMPLOYEE_ID, employee.getId());
			session.setAttribute(LmsConstants.EMPLOYEE_DESIGNATION, employee.getDesignation());
			response = new ResponseEntity<String>(employee.getDesignation(), HttpStatus.OK);
			return response;
		} else {
			response = new ResponseEntity<String>(LmsConstants.LOGIN_FAILED, HttpStatus.OK);
			return response;
		}
	}
}
