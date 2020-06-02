package com.intimetec.lms.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "employee")
public class Employee {

	@Id
	@GeneratedValue
	private String id;
	private String firstname;
	private String lastname;
	private String username;
	private String password;
	private String email;
	private String designation;
	private String gender;

	@Transient
	private String assignLeadId;

	@Transient
	private String assignManagerId;

	@Transient
	private EmployeeLeaveType employeeLeaveRecord;

	public Employee() {
	};

	public Employee(String id, String firstName, String lastName, String username, String password, String email,
			String designation, String gender, String assignLeadId, String assignManagerId) {
		this.id = id;
		this.firstname = firstName;
		this.lastname = lastName;
		this.username = username;
		this.password = password;
		this.email = email;
		this.designation = designation;
		this.gender = gender;
		this.assignLeadId = assignLeadId;
		this.assignManagerId = assignManagerId;
		this.setEmployeeLeaveRecord(new EmployeeLeaveType(id, 10, 15, 365, 12, 120, 7, 180));
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getAssignLeadId() {
		return assignLeadId;
	}

	public void setAssignLeadId(String assignLeadId) {
		this.assignLeadId = assignLeadId;
	}

	public String getAssignManagerId() {
		return assignManagerId;
	}

	public void setAssignManagerId(String assignManagerId) {
		this.assignManagerId = assignManagerId;
	}

	public String getDesignation() {
		return designation;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public EmployeeLeaveType getEmployeeLeaveRecord() {
		return employeeLeaveRecord;
	}

	public void setEmployeeLeaveRecord(EmployeeLeaveType employeeLeaveRecord) {
		this.employeeLeaveRecord = employeeLeaveRecord;
	}

}
