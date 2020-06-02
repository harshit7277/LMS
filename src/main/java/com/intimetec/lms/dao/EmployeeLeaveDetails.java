package com.intimetec.lms.dao;

import java.sql.Date;
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

import com.intimetec.lms.model.Leave;
import com.intimetec.lms.utility.LmsConstants;
import com.intimetec.lms.utility.LmsDbQuery;

@Repository("employeeLeaveDetails")
public class EmployeeLeaveDetails {
	private JdbcTemplate template;

	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}

	public ArrayList<Leave> employeeNotApprovedLeaveInfo(String query, String currentLoginUserId) {

		return template.query(query, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setString(1, currentLoginUserId);
				preparedStatement.setString(2, LmsConstants.LEAVE_APPLIED);
			}

		}, new ResultSetExtractor<ArrayList<Leave>>() {

			@Override
			public ArrayList<Leave> extractData(ResultSet result) throws SQLException, DataAccessException {
				ArrayList<Leave> leaveDetails = new ArrayList<Leave>();
				while (result.next()) {
					Leave leave = new Leave();
					leave.setLeaveId(Integer.parseInt(result.getString(LmsConstants.LEAVE_LEAVEID)));
					leave.setUsername(result.getString(LmsConstants.EMPLOYEE_NAME));
					leave.setLeavetype(result.getString(LmsConstants.LEAVE_LEAVETYPE));
					leave.setLeaveTill(result.getDate(LmsConstants.LEAVETILL));
					leave.setLeaveFrom(result.getDate(LmsConstants.LEAVE_DATE));
					leave.setLeavenumber(Integer.parseInt(result.getString(LmsConstants.LEAVE_LEAVENUMBER)));
					leaveDetails.add(leave);
				}
				return leaveDetails;
			}
		});
	}

	public ArrayList<Leave> employeeNotApprovedLeaveInfo(String designation) {

		return template.query(LmsDbQuery.GET_EMPLOYEE_APPROVED_LEAVE, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setString(1, designation);
				preparedStatement.setString(2, LmsConstants.LEAVE_APPLIED);
			}

		}, new ResultSetExtractor<ArrayList<Leave>>() {

			@Override
			public ArrayList<Leave> extractData(ResultSet result) throws SQLException, DataAccessException {
				ArrayList<Leave> leaveDetails = new ArrayList<Leave>();
				while (result.next()) {
					Leave leave = new Leave();
					leave.setLeaveId(Integer.parseInt(result.getString(LmsConstants.LEAVE_LEAVEID)));
					leave.setUsername(result.getString(LmsConstants.EMPLOYEE_NAME));
					leave.setLeavetype(result.getString(LmsConstants.LEAVE_LEAVETYPE));
					leave.setLeaveTill(Date.valueOf(result.getString(LmsConstants.LEAVETILL)));
					leave.setLeaveFrom(Date.valueOf(result.getString(LmsConstants.LEAVE_DATE)));
					leave.setLeavenumber(Integer.parseInt(result.getString(LmsConstants.LEAVE_LEAVENUMBER)));
					leaveDetails.add(leave);
				}
				return leaveDetails;
			}
		});
	}

	public ArrayList<Leave> employeeApprovedLeaveInfo(String designation) {

		return template.query(LmsDbQuery.GET_EMPLOYEE_NOT_APPROVED_LEAVE, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setString(1, designation);
				preparedStatement.setString(2, LmsConstants.APPROVED_LEAVE);
			}

		}, new ResultSetExtractor<ArrayList<Leave>>() {

			@Override
			public ArrayList<Leave> extractData(ResultSet result) throws SQLException, DataAccessException {
				ArrayList<Leave> leaveDetails = new ArrayList<Leave>();
				while (result.next()) {
					Leave leave = new Leave();
					leave.setUserId(result.getString(LmsConstants.EMPLOYEE_ID));
					leave.setLeaveId(Integer.parseInt(result.getString(LmsConstants.LEAVE_LEAVEID)));
					leave.setUsername(result.getString(LmsConstants.EMPLOYEE_NAME));
					leave.setLeavetype(result.getString(LmsConstants.LEAVE_LEAVETYPE));
					leave.setLeaveFrom(Date.valueOf(result.getString(LmsConstants.LEAVE_DATE)));
					leave.setLeavenumber(Integer.parseInt(result.getString(LmsConstants.LEAVE_LEAVENUMBER)));
					leaveDetails.add(leave);
				}
				return leaveDetails;
			}
		});
	}

	public boolean updateEmployeeLeaveNumber(String employeeId, String leaveType, int updateLeaveNumber) {
		String query = String.format(LmsDbQuery.UPDATE_EMPLOYEE_LEAVE, leaveType, updateLeaveNumber, employeeId);
		boolean isUpdated = false;
		if (!template.execute(query, new PreparedStatementCallback<Boolean>() {

			@Override
			public Boolean doInPreparedStatement(PreparedStatement preparedStatement)
					throws SQLException, DataAccessException {
				return preparedStatement.execute();
			}

		})) {
			isUpdated = true;
		}
		return isUpdated;
	}

}
