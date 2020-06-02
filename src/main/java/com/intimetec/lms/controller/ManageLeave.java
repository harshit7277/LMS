package com.intimetec.lms.controller;

import java.sql.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.intimetec.lms.exception.LeaveValidationException;
import com.intimetec.lms.exception.PermissionNotGrantedException;
import com.intimetec.lms.model.Employee;
import com.intimetec.lms.model.EmployeeLeaveType;
import com.intimetec.lms.model.Leave;
import com.intimetec.lms.service.AdminService;
import com.intimetec.lms.service.EmployeeService;
import com.intimetec.lms.service.LeadService;
import com.intimetec.lms.service.ManagerService;
import com.intimetec.lms.utility.LmsConstants;

@RestController
@RequestMapping(value = "/manageLeaves")
public class ManageLeave {

	final static Logger LOGGER = Logger.getLogger(LeaveDetail.class.getName());

	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private LeadService leadService;
	@Autowired
	private ManagerService managerService;
	@Autowired
	private AdminService adminService;

	@RequestMapping(value = "/leaveRequest", method = RequestMethod.POST)
	public ResponseEntity<String> leaveRequest(@RequestParam(LmsConstants.LEAVE_FROM) Date leaveFrom,
			@RequestParam(LmsConstants.LEAVE_LEAVETYPE) String leavetype,
			@RequestParam(LmsConstants.LEAVE_LEAVENUMBER) int leavenumber, HttpServletRequest request) {
		Employee currentLoginEmployee = employeeService.getTokenObject(request.getHeader(LmsConstants.AUTHORIZATION));
		Leave employeeLeave = new Leave();
		employeeLeave.setUserId(currentLoginEmployee.getId());
		employeeLeave.setLeaveFrom(leaveFrom);
		employeeLeave.setLeavetype(leavetype);
		employeeLeave.setLeavenumber(leavenumber);
		employeeLeave.setApproval(LmsConstants.LEAVE_APPLIED);
		boolean isLeaveApplied = false;
		ResponseEntity<String> response = null;
		Employee employee;
		try {
			employee = employeeService.userProfile(currentLoginEmployee.getId());
			isLeaveApplied = employeeService.applyLeave(employeeLeave, employee.getGender());
		} catch (LeaveValidationException exception) {
			LOGGER.error(exception.getMessage());
			response = internalServerException(exception);
		}
		if (isLeaveApplied) {
			response = new ResponseEntity<String>(LmsConstants.LEAVE_REQUESTED, HttpStatus.OK);
		}
		return response;
	}

	@RequestMapping(value = "/approve", method = RequestMethod.POST)
	public ResponseEntity<String> approveLeave(@RequestParam(LmsConstants.LEAVE_LEAVEID) int leaveid,
			HttpServletRequest request) {
		boolean status = false;
		ResponseEntity<String> response = null;
		Employee currentLoginEmployee = employeeService.getTokenObject(request.getHeader(LmsConstants.AUTHORIZATION));
		if (currentLoginEmployee.getId() != null) {
			try {
				if (currentLoginEmployee.getDesignation().equals(LmsConstants.LEAD)) {
					status = leadService.approveLeave(leaveid,currentLoginEmployee);
				} else if (currentLoginEmployee.getDesignation().equals(LmsConstants.MANAGER)) {
					status = managerService.approveLeave(leaveid,currentLoginEmployee);
				} else if (currentLoginEmployee.getDesignation().equals(LmsConstants.EMPLOYEE_ADMIN)) {
					status = adminService.approveLeave(leaveid,currentLoginEmployee);
				}
			} catch (PermissionNotGrantedException exception) {
				LOGGER.error(exception.getMessage());
				internalServerException(exception);
			}
			if (status) {
				response = new ResponseEntity<String>(LmsConstants.LEAVE_APPROVED, HttpStatus.OK);
			}
		}
		return response;
	}

	@RequestMapping(value = "/reject", method = RequestMethod.POST)
	public ResponseEntity<String> rejectLeave(@RequestParam(LmsConstants.LEAVE_LEAVEID) int leaveid,
			HttpServletRequest request) {
		boolean status = false;
		ResponseEntity<String> response = null;
		Employee currentLoginEmployee = employeeService.getTokenObject(request.getHeader(LmsConstants.AUTHORIZATION));
		if (currentLoginEmployee.getId() != null) {
			try {
				if (currentLoginEmployee.getDesignation().equals(LmsConstants.LEAD)) {
					status = leadService.rejectLeave(leaveid,currentLoginEmployee);
				} else if (currentLoginEmployee.getDesignation().equals(LmsConstants.MANAGER)) {
					status = managerService.rejectLeave(leaveid,currentLoginEmployee);
				} else if (currentLoginEmployee.getDesignation().equals(LmsConstants.EMPLOYEE_ADMIN)) {
					status = adminService.rejectLeave(leaveid,currentLoginEmployee);
				}
			} catch (PermissionNotGrantedException exception) {
				LOGGER.error(exception.getMessage());
				internalServerException(exception);
			}
			if (status) {
				response = new ResponseEntity<String>(LmsConstants.LEAVE_REJECTED, HttpStatus.OK);
			}
		}
		return response;
	}

	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public ResponseEntity<String> cancelLeave(@RequestParam(LmsConstants.LEAVE_LEAVEID) int leaveid,
			HttpServletRequest request) {
		boolean cancelStatus = false;
		ResponseEntity<String> response = null;
		cancelStatus = employeeService.cancelLeave(leaveid);
		if (cancelStatus) {
			response = new ResponseEntity<String>(LmsConstants.LEAVE_CANCEL, HttpStatus.OK);
		}
		return response;
	}

	@RequestMapping(value = "/updateLeaveBalance", method = RequestMethod.PUT)
	public ResponseEntity<String> updateLeaveBalance(@RequestBody EmployeeLeaveType employeeLeaveBalance, HttpServletRequest request) {
		Employee currentLoginEmployee = employeeService.getTokenObject(request.getHeader(LmsConstants.AUTHORIZATION));
		ResponseEntity<String> response = null;
		response = new ResponseEntity<String>(LmsConstants.LEAVEBALANCE_NOT_UPDATED, HttpStatus.OK);
		if (adminService.updateEmployeeBalance(employeeLeaveBalance,currentLoginEmployee.getDesignation())) {
			response = new ResponseEntity<String>(LmsConstants.LEAVEBALANCE_UPDATED, HttpStatus.OK);
		}
		return response;
	}

	private ResponseEntity<String> internalServerException(Exception exception) {
		return new ResponseEntity<String>(exception.getMessage(), HttpStatus.OK);
	}
}
