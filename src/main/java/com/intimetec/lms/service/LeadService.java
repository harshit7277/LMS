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

@Service("leadService")
public class LeadService extends EmployeeService {

	@Autowired
	private LeaveManageDao leaveDao;
	@Autowired
	private EmployeeLeaveDetails employeeLeaveDetails;

	public ArrayList<Leave> employeeLeaveForApprove(String currentLoginUserId) {
		ArrayList<Leave> leaveList = new ArrayList<Leave>();
		String query = LmsDbQuery.GET_EMPLOYEELEAVE_FOR_LEAD;
		leaveList = employeeLeaveDetails.employeeNotApprovedLeaveInfo(query, currentLoginUserId);
		return leaveList;
	}

	public boolean approveLeave(int leaveId, Employee currentLoginEmployee) throws PermissionNotGrantedException {
		boolean isUpdated = false;
		Leave leave = leaveDao.getLeaveFromLeaveId(leaveId);
		String leadId = leaveDao.getLeadIdFromRoleAssign(leave.getUserId());
		if (leadId.equals(currentLoginEmployee.getId())) {
			isUpdated = leaveDao.isLeaveStatusUpdated(leaveId, LmsConstants.APPROVAL);
			if (isUpdated) {
				isUpdated = isEmployeeLeaveUpdated(leaveId);
			}
		} else {
			throw new PermissionNotGrantedException(LmsConstants.APPROVE_PERMISSION_NOT_ALLOWED);
		}
		return isUpdated;
	}

	public boolean rejectLeave(int leaveId,Employee currentLoginEmployee) throws PermissionNotGrantedException {
		boolean isUpdated= false;
		Leave leave = leaveDao.getLeaveFromLeaveId(leaveId);
		String leadId = leaveDao.getLeadIdFromRoleAssign(leave.getUserId());
		if (leadId.equals(currentLoginEmployee.getId())) {
			isUpdated = leaveDao.isLeaveStatusUpdated(leaveId, LmsConstants.REVOKE);
		} else {
			throw new PermissionNotGrantedException(LmsConstants.REJECT_PERMISSION_NOT_ALLOWED);
		}
		return isUpdated;
	}

	protected boolean isEmployeeLeaveUpdated(int leaveId) {
		Leave leave = leaveDao.getLeaveFromLeaveId(leaveId);
		int totalLeave = leaveDao.leaveBalance(leave.getUserId(), leave.getLeavetype());
		int remainingLeave = totalLeave - leave.getLeavenumber();
		boolean isUpdated = employeeLeaveDetails.updateEmployeeLeaveNumber(leave.getUserId(), leave.getLeavetype(),
				remainingLeave);
		return isUpdated;
	}

}
