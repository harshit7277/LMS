package com.intimetec.lms.service;

import com.intimetec.lms.security.Encryption;
import com.intimetec.lms.utility.LmsConstants;

import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.intimetec.lms.dao.EmployeeDao;
import com.intimetec.lms.dao.EmployeeLeaveDetails;
import com.intimetec.lms.dao.FetchLeaveDao;
import com.intimetec.lms.dao.LeaveManageDao;
import com.intimetec.lms.exception.PasswordSecurityException;
import com.intimetec.lms.exception.PermissionNotGrantedException;
import com.intimetec.lms.model.Employee;
import com.intimetec.lms.model.EmployeeLeaveType;
import com.intimetec.lms.model.Leave;

@Service("adminService")
public class AdminService extends ManagerService {
	public static final String DOMAIN = "@intimetec.com";
	public static final String INSERT_TEXT = "@1234";

	@Autowired
	private EmployeeDao employeeDao;
	@Autowired
	private LeaveManageDao leaveDao;
	@Autowired
	private EmployeeLeaveDetails employeeLeaveDetails;
	@Autowired
	private FetchLeaveDao fetchLeave;

	public ArrayList<Leave> employeeLeaveForApprove() {
		ArrayList<Leave> leave = new ArrayList<Leave>();
		leave = employeeLeaveDetails.employeeNotApprovedLeaveInfo(LmsConstants.MANAGER);
		return leave;

	}

	@Override
	public boolean approveLeave(int leaveId, Employee currentLoginEmployee) {
		boolean isApproved = false;
		if (currentLoginEmployee.getDesignation().equals(LmsConstants.EMPLOYEE_ADMIN)) {
			isApproved = leaveDao.isLeaveStatusUpdated(leaveId, LmsConstants.APPROVAL);
		}
		boolean isUpdated = false;
		if (isApproved) {
			isUpdated = isEmployeeLeaveUpdated(leaveId);
		}
		return isUpdated;
	}

	@Override
	public boolean rejectLeave(int leaveId, Employee currentLoginEmployee) {
		boolean isRejected = false;
		if (currentLoginEmployee.getDesignation().equals(LmsConstants.EMPLOYEE_ADMIN)) {
			isRejected = leaveDao.isLeaveStatusUpdated(leaveId, LmsConstants.REVOKE);
		}
		return isRejected;
	}

	public boolean updateEmployee(Employee employee, String designation) throws PermissionNotGrantedException {
		Employee employeeObject = new Employee();
		employeeObject.setId(employee.getId());
		employeeObject.setDesignation(employee.getDesignation());
		if (employeeObject.getDesignation().equals(LmsConstants.EXECUTIVE)) {
			employeeObject.setAssignLeadId(employee.getAssignLeadId());
			employeeObject.setAssignManagerId(employee.getAssignManagerId());
		} else if (employeeObject.getDesignation().equals(LmsConstants.LEAD)) {
			employeeObject.setAssignManagerId(employee.getAssignManagerId());
		}
		boolean isUpdate;
		if (designation.equals(LmsConstants.EMPLOYEE_ADMIN)) {
			isUpdate = employeeDao.isEmployeeUpdated(employeeObject);
		} else {
			throw new PermissionNotGrantedException(LmsConstants.PERMISSION_NOT_ALLOWED);
		}
		return isUpdate;
	}

	public boolean addEmployee(Employee employee, String designation)
			throws SQLException, PasswordSecurityException, PermissionNotGrantedException {
		String employeePassword = null;
		Employee employeeObject = new Employee();
		employeeObject.setId(employee.getId());
		employeeObject.setFirstname(employee.getFirstname());
		employeeObject.setLastname(employee.getLastname());
		employeeObject.setUsername(employee.getUsername());
		employeeObject.setDesignation(employee.getDesignation());
		employeeObject.setGender(employee.getGender());
		if (employee.getDesignation().equals(LmsConstants.EXECUTIVE)) {
			employeeObject.setAssignLeadId(employee.getAssignLeadId());
			employeeObject.setAssignManagerId(employee.getAssignManagerId());
		} else if (employee.getDesignation().equals(LmsConstants.LEAD)) {
			employeeObject.setAssignManagerId(employee.getAssignManagerId());
		}
		employeePassword = Encryption.encrypt(employeeObject.getUsername() + INSERT_TEXT);
		String employeeEmail = employee.getUsername() + DOMAIN;
		Employee employeeData = new Employee(employeeObject.getId(), employeeObject.getFirstname(),
				employeeObject.getLastname(), employeeObject.getUsername(), employeePassword, employeeEmail,
				employeeObject.getDesignation(), employeeObject.getGender(), employeeObject.getAssignLeadId(),
				employeeObject.getAssignManagerId());
		boolean isAdded;
		try {
			if (designation.equals(LmsConstants.EMPLOYEE_ADMIN)) {
				isAdded = employeeDao.insertEmployeeDetails(employeeData);
			} else {
				throw new PermissionNotGrantedException(LmsConstants.PERMISSION_NOT_ALLOWED);
			}

		} catch (DataAccessException exception) {
			throw new SQLException(exception.getMessage());
		}
		return isAdded;
	}

	public boolean deleteEmployee(String id, String designation) throws PermissionNotGrantedException {
		boolean isDeleted = false;
		if (designation.equals(LmsConstants.EMPLOYEE_ADMIN)) {
			isDeleted = employeeDao.isEmployeeDeleted(id);
		} else {
			throw new PermissionNotGrantedException(LmsConstants.PERMISSION_NOT_ALLOWED);
		}

		return isDeleted;
	}

	public ArrayList<Employee> getAllEmployeeDetails() {
		ArrayList<Employee> list = (ArrayList<Employee>) employeeDao.getAllEmployees();
		return list;

	}

	public Employee getEmployeeFromUserId(String id) {
		Employee employee = employeeDao.getEmployeeDetailsFromUserId(id);
		return employee;
	}

	public EmployeeLeaveType getEmployeeBalance(String id) {
		EmployeeLeaveType leaveBalance;
		leaveBalance = fetchLeave.getEmployeeBalance(id);
		return leaveBalance;
	}

	public boolean updateEmployeeBalance(EmployeeLeaveType employeeLeaveBalance, String designation) {
		boolean isUpdated = false;
		if (designation.equals(LmsConstants.EMPLOYEE_ADMIN)) {
			isUpdated = leaveDao.isLeaveBalanceUpdated(employeeLeaveBalance);
		}
		return isUpdated;
	}
}
