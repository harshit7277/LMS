package com.intimetec.lms.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.intimetec.lms.model.Employee;
import com.intimetec.lms.service.AdminService;
import com.intimetec.lms.service.EmployeeService;
import com.intimetec.lms.utility.LmsConstants;

@RestController()
@RequestMapping(value = "/employees")
public class EmployeeDetail {

	final Logger LOGGER = Logger.getLogger(EmployeeDetail.class.getName());

	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private AdminService adminService;

	@RequestMapping(value = "/details", method = RequestMethod.GET)
	public ResponseEntity<ArrayList<Employee>> getEmployeeDetails() {
		ArrayList<Employee> employeeList = null;
		employeeList = adminService.getAllEmployeeDetails();
		return new ResponseEntity<ArrayList<Employee>>(employeeList, HttpStatus.OK);
	}

	@RequestMapping(value = "/token", method = RequestMethod.GET)
	public ResponseEntity<String> getToken(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String employeeToken = (String) session.getAttribute(LmsConstants.TOKEN);
		return new ResponseEntity<String>(employeeToken, HttpStatus.OK);
	}

	@RequestMapping(value = "/userid", method = RequestMethod.POST)
	public ResponseEntity<Employee> getEmployeeFromUserId(@RequestParam(LmsConstants.EMPLOYEE_ID) String id) {
		Employee employee = adminService.getEmployeeFromUserId(id);
		return new ResponseEntity<Employee>(employee, HttpStatus.OK);
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public ResponseEntity<Employee> profileDetails(HttpServletRequest request) {
		Employee currentLoginEmployee = null;
		currentLoginEmployee = employeeService.getTokenObject(request.getHeader(LmsConstants.AUTHORIZATION));
		Employee employeeProfile = employeeService.userProfile(currentLoginEmployee.getId());
		return new ResponseEntity<Employee>(employeeProfile, HttpStatus.OK);
	}

}
