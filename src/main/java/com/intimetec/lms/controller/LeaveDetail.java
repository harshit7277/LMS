package com.intimetec.lms.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.intimetec.lms.model.Employee;
import com.intimetec.lms.model.EmployeeLeaveType;
import com.intimetec.lms.model.Leave;
import com.intimetec.lms.service.AdminService;
import com.intimetec.lms.service.EmployeeService;
import com.intimetec.lms.service.LeadService;
import com.intimetec.lms.service.ManagerService;
import com.intimetec.lms.utility.LmsConstants;

@RestController
@RequestMapping(value = "/leaves")
public class LeaveDetail {

	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private LeadService leadService;
	@Autowired
	private ManagerService managerService;
	@Autowired
	private AdminService adminService;

	@RequestMapping(value = "/details", method = RequestMethod.GET)
	public ResponseEntity<ArrayList<Leave>> getEmployeeLeave(HttpServletRequest request) {
		Employee currentLoginEmployee = employeeService.getTokenObject(request.getHeader(LmsConstants.AUTHORIZATION));
		ArrayList<Leave> employeeLeaveList = null;
		if (currentLoginEmployee.getDesignation().equals(LmsConstants.LEAD)) {
			employeeLeaveList = leadService.employeeLeaveForApprove(currentLoginEmployee.getId());
		} else if (currentLoginEmployee.getDesignation().equals(LmsConstants.MANAGER)) {
			employeeLeaveList = managerService.employeeLeaveForApprove(currentLoginEmployee.getId());
		} else if (currentLoginEmployee.getDesignation().equals(LmsConstants.EMPLOYEE_ADMIN)) {
			employeeLeaveList = adminService.employeeLeaveForApprove();
		}
		return new ResponseEntity<ArrayList<Leave>>(employeeLeaveList, HttpStatus.OK);
	}

	@RequestMapping(value = "/leaveBalance", method = RequestMethod.GET)
	public ResponseEntity<EmployeeLeaveType> getEmployeeLeaveBalance(HttpServletRequest request) {
		Employee currentLoginEmployee = employeeService.getTokenObject(request.getHeader(LmsConstants.AUTHORIZATION));
		EmployeeLeaveType employeeLeaveBalance = null;
		employeeLeaveBalance = employeeService.getLeaveBalance(currentLoginEmployee.getId());
		return new ResponseEntity<EmployeeLeaveType>(employeeLeaveBalance, HttpStatus.OK);
	}

	@RequestMapping(value = "/leaveStatus", method = RequestMethod.GET)
	public ResponseEntity<ArrayList<Leave>> getEmployeeLeaveStatus(HttpServletRequest request) {
		ArrayList<Leave> employeeLeaveStatusList = null;
		Employee currentLoginEmployee = employeeService.getTokenObject(request.getHeader(LmsConstants.AUTHORIZATION));
		employeeLeaveStatusList = employeeService.getLeaveDetails(currentLoginEmployee.getId());
		return new ResponseEntity<ArrayList<Leave>>(employeeLeaveStatusList, HttpStatus.OK);
	}
}
