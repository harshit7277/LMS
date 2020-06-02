package com.intimetec.lms.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "leaveinfo")
public class Leave implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;

	@Id
	@GeneratedValue
	@Column(name = "leaveId")
	private int leaveId;

	@Transient
	private String username;

	@Column(name = "leaveDate")
	private Date leaveFrom;
	private Date leaveTill;

	@Column(name = "leavetype")
	private String leavetype;
	private int leavenumber;
	private String approval;

	public Leave() {
	}

	public void setLeaveTill(Date leaveTill) {
		this.leaveTill = leaveTill;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setLeaveId(int leaveId) {
		this.leaveId = leaveId;
	}

	public String getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLeavetype() {
		return leavetype;
	}

	public int getLeaveId() {
		return leaveId;
	}

	public String getApproval() {
		return approval;
	}

	public int getLeavenumber() {
		return leavenumber;
	}

	public String isApproval() {
		return approval;
	}

	public Date getLeaveFrom() {
		return leaveFrom;
	}

	public void setLeaveFrom(Date leaveFrom) {
		this.leaveFrom = leaveFrom;
	}

	public Date getLeaveTill() {
		return leaveTill;
	}

	public void setLeavetype(String leavetype) {
		this.leavetype = leavetype;
	}

	public void setLeavenumber(int leavenumber) {
		this.leavenumber = leavenumber;
	}

	public void setApproval(String approval) {
		this.approval = approval;
	}

}
