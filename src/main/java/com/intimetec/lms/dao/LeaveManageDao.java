package com.intimetec.lms.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.intimetec.lms.model.Employee;
import com.intimetec.lms.model.EmployeeLeaveType;
import com.intimetec.lms.model.Leave;
import com.intimetec.lms.utility.LmsConstants;
import com.intimetec.lms.utility.LmsDbQuery;

@Repository("leaveDao")
public class LeaveManageDao {

	private JdbcTemplate template;

	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}

	public boolean insertLeave(Employee employee) {
		boolean isInserted = false;
		if (!template.execute(LmsDbQuery.INSERT_LEAVE, new PreparedStatementCallback<Boolean>() {

			@Override
			public Boolean doInPreparedStatement(PreparedStatement preparedStatementLeave)
					throws SQLException, DataAccessException {
				preparedStatementLeave.setString(1, employee.getId());
				preparedStatementLeave.setInt(2, employee.getEmployeeLeaveRecord().getCasualLeave());
				preparedStatementLeave.setInt(3, employee.getEmployeeLeaveRecord().getEarnLeave());
				preparedStatementLeave.setInt(4, employee.getEmployeeLeaveRecord().getDutyLeave());
				preparedStatementLeave.setInt(5, employee.getEmployeeLeaveRecord().getSickLeave());
				preparedStatementLeave.setInt(6, employee.getEmployeeLeaveRecord().getMaternityLeave());
				preparedStatementLeave.setInt(7, employee.getEmployeeLeaveRecord().getParentalLeave());
				preparedStatementLeave.setInt(8, employee.getEmployeeLeaveRecord().getLeavewithoutpay());
				return preparedStatementLeave.execute();
			}

		})) {
			isInserted = true;
		}
		return isInserted;
	}

	public boolean isLeaveRequested(Leave leaveDetails) {
		boolean isRequested = false;
		if (!template.execute(LmsDbQuery.LEAVE_REQUEST, new PreparedStatementCallback<Boolean>() {

			@Override
			public Boolean doInPreparedStatement(PreparedStatement preparedStatement)
					throws SQLException, DataAccessException {
				preparedStatement.setDate(1, leaveDetails.getLeaveFrom());
				preparedStatement.setString(2, leaveDetails.isApproval());
				preparedStatement.setString(3, leaveDetails.getUserId());
				preparedStatement.setString(4, leaveDetails.getLeavetype());
				preparedStatement.setInt(5, leaveDetails.getLeavenumber());
				preparedStatement.setDate(6, leaveDetails.getLeaveTill());

				return preparedStatement.execute();
			}

		})) {
			isRequested = true;
		}
		return isRequested;
	}

	public boolean isLeaveBalanceUpdated(EmployeeLeaveType employeeLeaveBalance) {
		boolean isUpdated = false;
		if (!template.execute(LmsDbQuery.UPDATE_EMPLOYEE_BALANCE, new PreparedStatementCallback<Boolean>() {

			@Override
			public Boolean doInPreparedStatement(PreparedStatement preparedStatement)
					throws SQLException, DataAccessException {
				preparedStatement.setInt(1, employeeLeaveBalance.getCasualLeave());
				preparedStatement.setInt(2, employeeLeaveBalance.getEarnLeave());
				preparedStatement.setInt(3, employeeLeaveBalance.getDutyLeave());
				preparedStatement.setInt(4, employeeLeaveBalance.getSickLeave());
				preparedStatement.setInt(5, employeeLeaveBalance.getMaternityLeave());
				preparedStatement.setInt(6, employeeLeaveBalance.getParentalLeave());
				preparedStatement.setInt(7, employeeLeaveBalance.getLeavewithoutpay());
				preparedStatement.setString(8, employeeLeaveBalance.getEmployeeid());

				return preparedStatement.execute();
			}
		})) {
			isUpdated = true;
		}

		return isUpdated;
	}

	public boolean deleteEmployeeLeave(int leaveId) {
		boolean isDeleted = false;
		if (!template.execute(LmsDbQuery.DELETE_LEAVE, new PreparedStatementCallback<Boolean>() {

			@Override
			public Boolean doInPreparedStatement(PreparedStatement preparedStatement)
					throws SQLException, DataAccessException {
				preparedStatement.setInt(1, leaveId);
				return preparedStatement.execute();
			}
		})) {
			isDeleted = true;
		}
		return isDeleted;
	}

	public boolean isLeaveStatusUpdated(int leaveId, String operation) {
		String query = null;
		if (operation.equals(LmsConstants.APPROVAL)) {
			query = LmsDbQuery.APPROVE_LEAVE;
		} else if (operation.equals(LmsConstants.REVOKE)) {
			query = LmsDbQuery.REVOKE_LEAVE;
		} else if (operation.equals(LmsConstants.CANCEL)) {
			query = LmsDbQuery.CANCEL_LEAVE;
		}
		boolean isUpdated = false;
		if (!template.execute(query, new PreparedStatementCallback<Boolean>() {

			@Override
			public Boolean doInPreparedStatement(PreparedStatement preparedStatement)
					throws SQLException, DataAccessException {
				preparedStatement.setInt(1, leaveId);
				return preparedStatement.execute();
			}
		})) {
			isUpdated = true;
		}
		return isUpdated;
	}

	public int leaveBalance(String employeeId, String leavetype) {
		String query = String.format(LmsDbQuery.GET_LEAVE_NUMBER, leavetype, employeeId);
		return template.query(query, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet result) throws SQLException, DataAccessException {
				int balance = 0;
				while (result.next()) {
					balance = result.getInt(leavetype);
				}
				return balance;
			}

		});
	}

	public Leave getLeaveFromLeaveId(int leaveId) {

		return template.query(LmsDbQuery.GET_LEAVE_FROM_ID, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setInt(1, leaveId);

			}

		}, new ResultSetExtractor<Leave>() {

			@Override
			public Leave extractData(ResultSet result) throws SQLException, DataAccessException {
				Leave leave = new Leave();
				while (result.next()) {
					leave.setLeavetype(result.getString(LmsConstants.LEAVE_LEAVETYPE));
					leave.setLeavenumber(result.getInt(LmsConstants.LEAVE_LEAVENUMBER));
					leave.setLeaveId(result.getInt(LmsConstants.LEAVE_LEAVEID));
					leave.setUserId(result.getString(LmsConstants.LEAVE_USERID));
					leave.setApproval(result.getString(LmsConstants.LEAVE_STATUS));
				}
				return leave;
			}
		});
	}

	public String getLeadIdFromRoleAssign(String userId) {

		return template.query(LmsDbQuery.GET_LEADID_FROM_ROLE_ASSIGN, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setString(1, userId);

			}

		}, new ResultSetExtractor<String>() {

			@Override
			public String extractData(ResultSet result) throws SQLException, DataAccessException {
				String id = null;
				while (result.next()) {
					id = result.getString(LmsConstants.EMPLOYEE_ASSIGN_LEAD);
				}
				return id;
			}
		});
	}
	
	public String getManagerIdFromRoleAssign(String userId) {

		return template.query(LmsDbQuery.GET_MANAGERID_FROM_ROLE_ASSIGN, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setString(1, userId);

			}

		}, new ResultSetExtractor<String>() {

			@Override
			public String extractData(ResultSet result) throws SQLException, DataAccessException {
				String id = null;
				while (result.next()) {
					id = result.getString(LmsConstants.EMPLOYEE_ASSIGN_MANAGER);
				}
				return id;
			}
		});
	}

	public String getEmployeeDesignationFromLeaveId(int leaveId) {

		return template.query(LmsDbQuery.GET_DESIGNATION_FROM_LEAVEID, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setInt(1, leaveId);

			}

		}, new ResultSetExtractor<String>() {
			String designation;

			@Override
			public String extractData(ResultSet result) throws SQLException, DataAccessException {
				while (result.next()) {
					designation = result.getString(LmsConstants.EMPLOYEE_DESIGNATION);
				}
				return designation;
			}
		});
	}

	public ArrayList<Leave> getEmployeeLeaveDate(String userid) {

		return template.query(LmsDbQuery.GET_LEAVE_DATE, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setString(1, userid);
			}

		}, new ResultSetExtractor<ArrayList<Leave>>() {

			@Override
			public ArrayList<Leave> extractData(ResultSet result) throws SQLException, DataAccessException {
				ArrayList<Leave> employeeLeaveDate = new ArrayList<Leave>();
				while (result.next()) {
					Leave employeeLeave = new Leave();
					employeeLeave.setLeaveFrom(result.getDate(LmsConstants.LEAVE_DATE));
					employeeLeave.setLeaveTill(result.getDate(LmsConstants.LEAVE_TILL));
					employeeLeaveDate.add(employeeLeave);
				}
				return employeeLeaveDate;
			}
		});
	}
}
