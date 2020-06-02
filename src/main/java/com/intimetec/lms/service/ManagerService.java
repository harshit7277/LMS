package com.intimetec.lms.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intimetec.lms.dao.EmployeeLeaveDetails;
import com.intimetec.lms.dao.LeaveManageDao;
import com.intimetec.lms.exception.PermissionNotGrantedException;
import com.intimetec.lms.model.Employee;
import com.intimetec.lms.model.Leave;
import com.intimetec.lms.utility.LmsConstants;
import com.intimetec.lms.utility.LmsDbQuery;

@Service("managerService")
public class ManagerService extends LeadService {
	boolean isUpdated = false;

	@Autowired
	private LeaveManageDao leaveDao;
	@Autowired
	private EmployeeLeaveDetails employeeLeaveDetails;

	@Override
	public ArrayList<Leave> employeeLeaveForApprove(String currentLoginUserId) {
		ArrayList<Leave> leaveList = new ArrayList<Leave>();
		String query = LmsDbQuery.GET_EMPLOYEELEAVE_FOR_MANAGER;
		leaveList = employeeLeaveDetails.employeeNotApprovedLeaveInfo(query, currentLoginUserId);
		return leaveList;
	}

	@Override
	public boolean approveLeave(int leaveId, Employee currentLoginEmployee) throws PermissionNotGrantedException {
		Leave leave = leaveDao.getLeaveFromLeaveId(leaveId);
		String managerId = leaveDao.getManagerIdFromRoleAssign(leave.getUserId());
		if (managerId.equals(currentLoginEmployee.getId())) {
			isUpdated = leaveDao.isLeaveStatusUpdated(leaveId, LmsConstants.APPROVAL);
			if (isUpdated) {
				isUpdated = isEmployeeLeaveUpdated(leaveId);
			}
		} else {
			throw new PermissionNotGrantedException(LmsConstants.APPROVE_PERMISSION_NOT_ALLOWED);
		}
		return isUpdated;
	}

	@Override
	public boolean rejectLeave(int leaveId, Employee currentLoginEmployee) throws PermissionNotGrantedException {
		Leave leave = leaveDao.getLeaveFromLeaveId(leaveId);
		String managerId = leaveDao.getManagerIdFromRoleAssign(leave.getUserId());
		if (managerId.equals(currentLoginEmployee.getId())) {
			isUpdated = leaveDao.isLeaveStatusUpdated(leaveId, LmsConstants.REVOKE);
		} else {
			throw new PermissionNotGrantedException(LmsConstants.REJECT_PERMISSION_NOT_ALLOWED);
		}
		return isUpdated;
	}

}
