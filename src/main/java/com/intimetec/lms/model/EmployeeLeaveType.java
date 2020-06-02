package com.intimetec.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "employeeleave")
public class EmployeeLeaveType {

	@Id
	@Column(name = "id")
	private String employeeid;

	@Column(name = "casual")
	private int casualLeave;
	@Column(name = "earn")
	private int earnLeave;
	@Column(name = "duty")
	private int dutyLeave;
	@Column(name = "sick")
	private int sickLeave;
	@Column(name = "maternity")
	private int maternityLeave;
	@Column(name = "parental")
	private int parentalLeave;

	private int leavewithoutpay;

	public EmployeeLeaveType() {
	}

	public EmployeeLeaveType(String employeeid, int casualLeave, int earnLeave, int dutyLeave, int sickLeave,
			int maternityLeave, int parentalLeave, int leavewithoutpay) {
		this.employeeid = employeeid;
		this.casualLeave = casualLeave;
		this.earnLeave = earnLeave;
		this.dutyLeave = dutyLeave;
		this.sickLeave = sickLeave;
		this.maternityLeave = maternityLeave;
		this.parentalLeave = parentalLeave;
		this.leavewithoutpay = leavewithoutpay;
	}

	public void setEmployeeid(String employeeid) {
		this.employeeid = employeeid;
	}

	public void setCasualLeave(int casualLeave) {
		this.casualLeave = casualLeave;
	}

	public void setEarnLeave(int earnLeave) {
		this.earnLeave = earnLeave;
	}

	public void setDutyLeave(int dutyLeave) {
		this.dutyLeave = dutyLeave;
	}

	public void setSickLeave(int sickLeave) {
		this.sickLeave = sickLeave;
	}

	public void setMaternityLeave(int maternityLeave) {
		this.maternityLeave = maternityLeave;
	}

	public void setParentalLeave(int parentalLeave) {
		this.parentalLeave = parentalLeave;
	}

	public void setLeavewithoutpay(int leavewithoutpay) {
		this.leavewithoutpay = leavewithoutpay;
	}

	public String getEmployeeid() {
		return employeeid;
	}

	public int getCasualLeave() {
		return casualLeave;
	}

	public int getEarnLeave() {
		return earnLeave;
	}

	public int getDutyLeave() {
		return dutyLeave;
	}

	public int getSickLeave() {
		return sickLeave;
	}

	public int getMaternityLeave() {
		return maternityLeave;
	}

	public int getParentalLeave() {
		return parentalLeave;
	}

	public int getLeavewithoutpay() {
		return leavewithoutpay;
	}

}
