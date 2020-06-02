package com.intimetec.lms.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.intimetec.lms.model.Employee;
import com.intimetec.lms.utility.LmsConstants;
import com.intimetec.lms.utility.LmsDbQuery;

@Repository("employeeDao")
public class EmployeeDao {
	@Autowired
	private LeaveManageDao leaveDao;
	private JdbcTemplate template;

	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}

	public List<Employee> getAllEmployees() {

		return template.query(LmsDbQuery.GET_EMPLOYEE, new ResultSetExtractor<ArrayList<Employee>>() {

			@Override
			public ArrayList<Employee> extractData(ResultSet results) throws SQLException, DataAccessException {
				ArrayList<Employee> employeeList = new ArrayList<Employee>();
				while (results.next()) {
					Employee employee = new Employee();
					employee.setId(results.getString(LmsConstants.EMPLOYEE_ID));
					employee.setFirstname(results.getString(LmsConstants.EMPLOYEE_FIRSTNAME));
					employee.setLastname(results.getString(LmsConstants.EMPLOYEE_LASTNAME));
					employee.setUsername(results.getString(LmsConstants.EMPLOYEE_NAME));
					employee.setDesignation(results.getString(LmsConstants.EMPLOYEE_DESIGNATION));
					employee.setEmail(results.getString(LmsConstants.EMPLOYEE_MAIL));
					employee.setGender(results.getString(LmsConstants.EMPLOYEE_GENDER));
					if (!results.getString(LmsConstants.EMPLOYEE_DESIGNATION).equals(LmsConstants.EMPLOYEE_ADMIN)) {
						employeeList.add(employee);
					}
				}
				return employeeList;
			}
		});
	}

	public ArrayList<Employee> getEmployeeCredentials() {
		return (ArrayList<Employee>) template.query(LmsDbQuery.GET_EMPLOYEE_CREDENTIAL,
				new ResultSetExtractor<ArrayList<Employee>>() {

					@Override
					public ArrayList<Employee> extractData(ResultSet result) throws SQLException, DataAccessException {
						ArrayList<Employee> employeeCredentialList = new ArrayList<Employee>();
						while (result.next()) {
							Employee employee = new Employee();
							employee.setUsername(result.getString(LmsConstants.EMPLOYEE_NAME));
							employee.setPassword(result.getString(LmsConstants.EMPLOYEE_PASSWORD));
							employeeCredentialList.add(employee);
						}
						return employeeCredentialList;

					}
				});
	}

	public Employee getEmployeeDetailsFromName(String name) {

		return (Employee) template.query(LmsDbQuery.GET_EMPLOYEE_FROM_NAME, new PreparedStatementSetter() {
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setString(1, name);
			}
		}, new ResultSetExtractor<Employee>() {
			@Override
			public Employee extractData(ResultSet result) throws SQLException, DataAccessException {
				Employee employee = new Employee();
				while (result.next()) {
					employee.setId(result.getString(LmsConstants.EMPLOYEE_ID));
					employee.setUsername(result.getString(LmsConstants.EMPLOYEE_NAME));
					employee.setEmail(result.getString(LmsConstants.EMPLOYEE_MAIL));
					employee.setDesignation(result.getString(LmsConstants.EMPLOYEE_DESIGNATION));
					employee.setGender(result.getString(LmsConstants.EMPLOYEE_GENDER));
				}
				return employee;
			}
		});
	}

	public List<String> getEmployeeRolesFromName(String name) {
		return (List<String>) template.query(LmsDbQuery.GET_EMPLOYEE_ROLE_FROM_NAME, new PreparedStatementSetter() {
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setString(1, name);
			}
		}, new ResultSetExtractor<List<String>>() {
			@Override
			public List<String> extractData(ResultSet result) throws SQLException, DataAccessException {
				List<String> role = new ArrayList<String>();
				while (result.next()) {
					role.add(result.getString(LmsConstants.EMPLOYEE_DESIGNATION));
				}
				return role;
			}
		});
	}

	public boolean isDeleted = false;

	public boolean isEmployeeDeleted(String id) {
		if (template.update(LmsDbQuery.DELETE_ROLE_ASSIGN, id) > 0) {
			if (template.update(LmsDbQuery.DELETE_EMPLOYEE, id) > 0) {
				isDeleted = true;
			}
		}
		return isDeleted;
	}

	public Employee getEmployeeProfile(String userid) {
		return (Employee) template.query(LmsDbQuery.GET_PROFILE, new PreparedStatementSetter() {
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setString(1, userid);
			}
		}, new ResultSetExtractor<Employee>() {
			@Override
			public Employee extractData(ResultSet result) throws SQLException, DataAccessException {
				Employee employee = new Employee();
				while (result.next()) {
					employee.setId(result.getString(LmsConstants.EMPLOYEE_ID));
					employee.setUsername(result.getString(LmsConstants.EMPLOYEE_NAME));
					employee.setEmail(result.getString(LmsConstants.EMPLOYEE_MAIL));
					employee.setDesignation(result.getString(LmsConstants.EMPLOYEE_DESIGNATION));
					employee.setGender(result.getString(LmsConstants.EMPLOYEE_GENDER));
					employee.setAssignLeadId(result.getString(LmsConstants.EMPLOYEE_ASSIGN_LEAD));
					employee.setAssignManagerId(result.getString(LmsConstants.EMPLOYEE_ASSIGN_MANAGER));
				}
				return employee;
			}
		});
	}

	public Employee getEmployeeDetailsFromUserId(String userid) {
		return (Employee) template.query(LmsDbQuery.GET_EMPLOYEE_FROM_ID, new PreparedStatementSetter() {
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setString(1, userid);
			}
		}, new ResultSetExtractor<Employee>() {
			@Override
			public Employee extractData(ResultSet result) throws SQLException, DataAccessException {
				Employee employee = new Employee();
				while (result.next()) {
					employee.setId(result.getString(LmsConstants.EMPLOYEE_ID));
					employee.setFirstname(result.getString(LmsConstants.EMPLOYEE_FIRSTNAME));
					employee.setLastname(result.getString(LmsConstants.EMPLOYEE_LASTNAME));
					employee.setUsername(result.getString(LmsConstants.EMPLOYEE_NAME));
					employee.setEmail(result.getString(LmsConstants.EMPLOYEE_MAIL));
					employee.setDesignation(result.getString(LmsConstants.EMPLOYEE_DESIGNATION));
					employee.setGender(result.getString(LmsConstants.EMPLOYEE_GENDER));
				}
				return employee;
			}
		});
	}

	public Boolean isEmployeeUpdated(Employee employee) {
		boolean isUpdated = false;
		if (!template.execute(LmsDbQuery.UPDATE_EMPLOYEE, new PreparedStatementCallback<Boolean>() {

			@Override
			public Boolean doInPreparedStatement(PreparedStatement preparedStatement)
					throws SQLException, DataAccessException {
				preparedStatement.setString(1, employee.getDesignation());
				preparedStatement.setString(2, employee.getId());
				return preparedStatement.execute();
			}
		})) {
			isUpdated = updateRoleAssign(employee);
		}
		return isUpdated;
	}

	public boolean isPasswordUpdated(String newPassword, String currentLoginUserID) {
		boolean isUpdated = false;
		if (!template.execute(LmsDbQuery.UPDATE_PASSWORD, new PreparedStatementCallback<Boolean>() {

			@Override
			public Boolean doInPreparedStatement(PreparedStatement preparedStatement)
					throws SQLException, DataAccessException {
				preparedStatement.setString(1, newPassword);
				preparedStatement.setString(2, currentLoginUserID);
				return preparedStatement.execute();
			}
		})) {
			isUpdated = true;
		}
		return isUpdated;
	}

	public boolean insertEmployeeDetails(Employee employee) throws DataAccessException {
		boolean isInserted = false;
		if (!template.execute(LmsDbQuery.INSERT_EMPLOYEE, new PreparedStatementCallback<Boolean>() {

			@Override
			public Boolean doInPreparedStatement(PreparedStatement preparedStatement)
					throws SQLException, DataAccessException {
				preparedStatement.setString(1, employee.getId());
				preparedStatement.setString(2, employee.getFirstname());
				preparedStatement.setString(3, employee.getLastname());
				preparedStatement.setString(4, employee.getPassword());
				preparedStatement.setString(5, employee.getUsername());
				preparedStatement.setString(6, employee.getEmail());
				preparedStatement.setString(7, employee.getDesignation());
				preparedStatement.setString(8, employee.getGender());
				return preparedStatement.execute();
			}
		})) {
			if (leaveDao.insertLeave(employee)) {
				isInserted = insertRoleAssign(employee);
			}
		}
		return isInserted;
	}

	public boolean isRoleInserted = false;

	public boolean insertRoleAssign(Employee employee) {

		if (employee.getDesignation().equals(LmsConstants.MANAGER)) {
			template.execute(LmsDbQuery.ROLE_ASSIGN_MANAGER, new PreparedStatementCallback<Boolean>() {

				@Override
				public Boolean doInPreparedStatement(PreparedStatement preparedStatement)
						throws SQLException, DataAccessException {
					preparedStatement.setString(1, employee.getId());
					isRoleInserted = !preparedStatement.execute();
					return isRoleInserted;
				}
			});
			isRoleInserted = true;
		} else if (employee.getDesignation().equals(LmsConstants.EXECUTIVE)) {
			template.execute(LmsDbQuery.ROLE_ASSIGN_EXECUTIVE, new PreparedStatementCallback<Boolean>() {

				@Override
				public Boolean doInPreparedStatement(PreparedStatement preparedStatement)
						throws SQLException, DataAccessException {
					preparedStatement.setString(1, employee.getId());
					preparedStatement.setString(2, employee.getAssignLeadId());
					preparedStatement.setString(3, employee.getAssignManagerId());
					isRoleInserted = !preparedStatement.execute();
					return isRoleInserted;
				}
			});
		} else if (employee.getDesignation().equals(LmsConstants.LEAD)) {
			template.execute(LmsDbQuery.ROLE_ASSIGN_LEAD, new PreparedStatementCallback<Boolean>() {

				@Override
				public Boolean doInPreparedStatement(PreparedStatement preparedStatement)
						throws SQLException, DataAccessException {
					preparedStatement.setString(1, employee.getId());
					preparedStatement.setString(2, employee.getAssignManagerId());
					isRoleInserted = !preparedStatement.execute();
					return isRoleInserted;
				}

			});
		}
		return isRoleInserted;
	}

	boolean isRoleUpdated = false;

	public boolean updateRoleAssign(Employee employee) {
		if (employee.getDesignation().equals(LmsConstants.EXECUTIVE)) {
			template.execute(LmsDbQuery.UPDATE_ROLEASSIGN_EXECUTIVE, new PreparedStatementCallback<Boolean>() {

				@Override
				public Boolean doInPreparedStatement(PreparedStatement preparedStatement)
						throws SQLException, DataAccessException {
					preparedStatement.setString(1, employee.getAssignLeadId());
					preparedStatement.setString(2, employee.getAssignManagerId());
					preparedStatement.setString(3, employee.getId());
					return isRoleInserted = !preparedStatement.execute();

				}
			});

		} else if (employee.getDesignation().equals(LmsConstants.LEAD)) {
			template.execute(LmsDbQuery.UPDATE_ROLEASSIGN_LEAD, new PreparedStatementCallback<Boolean>() {

				@Override
				public Boolean doInPreparedStatement(PreparedStatement preparedStatement)
						throws SQLException, DataAccessException {
					preparedStatement.setString(1, employee.getAssignManagerId());
					preparedStatement.setString(2, employee.getId());
					return isRoleInserted = !preparedStatement.execute();
				}
			});

		} else if (employee.getDesignation().equals(LmsConstants.MANAGER)) {
			template.execute(LmsDbQuery.UPDATE_ROLEASSIGN_MANAGER, new PreparedStatementCallback<Boolean>() {

				@Override
				public Boolean doInPreparedStatement(PreparedStatement preparedStatement)
						throws SQLException, DataAccessException {
					preparedStatement.setString(1, employee.getId());
					return isRoleInserted = !preparedStatement.execute();
				}
			});
		}
		return isRoleInserted;
	}

}
