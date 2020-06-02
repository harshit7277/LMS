package com.intimetec.lms.validation;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.intimetec.lms.dao.EmployeeDao;
import com.intimetec.lms.model.Employee;

@Component("validateEmployeeLogin")
public class ValidateEmployeeLogin {

	@Autowired
	private EmployeeDao employeeDao;

	public boolean validateEmployeeCredentials(String username, String password) {
		boolean loginSuccess = false;

		ArrayList<Employee> employeeCredentialList = employeeDao.getEmployeeCredentials();
		for (int index = 0; index < employeeCredentialList.size(); index++) {
			if (username.equals(employeeCredentialList.get(index).getUsername())
					&& password.equals(employeeCredentialList.get(index).getPassword())) {
				loginSuccess = true;
				break;
			} else {
				loginSuccess = false;
			}
		}
		return loginSuccess;
	}
}
