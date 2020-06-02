package com.intimetec.lms.utility;

public class LmsDbQuery {

	public static final String INSERT_EMPLOYEE = " insert into employee (id,firstname,lastname,password,username,email,designation,gender) values (?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String ROLE_ASSIGN_EXECUTIVE = "insert into roleassign (userid,leadid,managerid) values (?,?,?)";
	public static final String ROLE_ASSIGN_MANAGER = "insert into roleassign (userid) values (?)";
	public static final String ROLE_ASSIGN_LEAD = "insert into roleassign (userid,managerid) values (?,?)";
	public static final String DELETE_EMPLOYEE = "delete from employee where id=?";
	public static final String UPDATE_EMPLOYEE = "update employee set designation=? where id=?";
	public static final String UPDATE_ROLEASSIGN_EXECUTIVE = "update roleassign set leadid=?,managerid=? where userid=?";
	public static final String UPDATE_ROLEASSIGN_LEAD = "update roleassign set leadid='NULL',managerid=? where userid=?";
	public static final String UPDATE_ROLEASSIGN_MANAGER = "update roleassign set leadid='NULL',managerid=NULL where userid=?";
	public static final String DELETE_ROLE_ASSIGN = "delete from roleassign where userid=?";
	public static final String INSERT_LEAVE = "insert into employeeLeave (id,casual,earn,duty,sick,maternity,parental,leavewithoutpay) values (?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String GET_EMPLOYEE = "select id,firstname,lastname,username,email,designation,gender from employee";
	public static final String GET_EMPLOYEE_CREDENTIAL = "select password,username from employee";
	public static final String UPDATE_PASSWORD = "update employee set password=? where id=?";
	public static final String GET_EMPLOYEE_FROM_NAME = "select id,password,username,email,designation,gender from employee where username=?";
	public static final String GET_EMPLOYEE_ROLE_FROM_NAME = "select designation from employee where username=?";
	public static final String GET_PROFILE = "select employee.id,employee.password,employee.username,employee.email,employee.designation,employee.gender,roleassign.leadid,roleassign.managerid from employee join roleassign on employee.id=roleassign.userid && userid=?";
	public static final String GET_EMPLOYEE_FROM_ID = "select id,firstname,lastname,password,username,email,designation,gender from employee where id=?";
	public static final String GET_EMPLOYEE_APPROVED_LEAVE = "select employee.id, employee.username, leaveinfo.leaveId , leaveinfo.leavetype ,leaveinfo.leaveDate,leaveinfo.leaveTill,leaveinfo.leavenumber from employee join leaveinfo on employee.id=leaveinfo.userid && employee.designation=? && leaveinfo.approval=? ";
	public static final String GET_EMPLOYEE_NOT_APPROVED_LEAVE = "select employee.id, employee.username, leaveinfo.leaveId , leaveinfo.leavetype ,leaveinfo.leaveDate,leaveinfo.leavenumber from employee join leaveinfo on employee.id=leaveinfo.userid && employee.designation=? && leaveinfo.approval=? ";
	public static final String LEAVE_REQUEST = "insert into leaveinfo (leaveDate,approval,userid,leavetype,leavenumber,leavetill) values (?, ?, ?, ?, ?,?)";
	public static final String DELETE_LEAVE = "delete from leaveinfo where leaveId=?";
	public static final String GET_LEAVE_STATUS = "select leaveId,leaveDate,leaveTill,leavetype,leavenumber,approval from leaveinfo where userid=?";
	public static final String APPROVE_LEAVE = "update leaveinfo set approval = 'APPROVED' where leaveid =?";
	public static final String REVOKE_LEAVE = "update leaveinfo set approval = 'REJECTED' where leaveid =?";
	public static final String CANCEL_LEAVE = "update leaveinfo set approval = 'CANCELLED' where leaveid =?";
	public static final String LEAVE_BALANCE = "select casual,earn,duty,sick,maternity,parental,leavewithoutpay from employeeleave where id=?";
	public static final String GET_LEAVE_FROM_ID = "select * from leaveinfo where leaveID=?";
	public static final String GET_DESIGNATION_FROM_LEAVEID = "select employee.designation from employee join leaveinfo on employee.id = leaveinfo.userid where leaveid=? ";
	public static final String GET_LEAVE_NUMBER = "select %s from employeeleave where id='%s'";
	public static final String UPDATE_EMPLOYEE_LEAVE = "update employeeleave set %s=%d where id='%s'";
	public static final String GET_LEAVE_DATE = "select leavedate,leavetill from leaveinfo where userid=?";
	public static final String GET_EMPLOYEELEAVE_FOR_MANAGER = "select leaveinfo.leaveId , employee.username , leaveinfo.leaveDate , leaveinfo.leaveTill, leaveinfo.leavetype,leaveinfo.leavenumber from leaveinfo join roleassign join employee on leaveinfo.userid=roleassign.userid && employee.id=leaveinfo.userid && roleassign.managerid=? && leaveinfo.approval=? ";
	public static final String GET_EMPLOYEELEAVE_FOR_LEAD = "select leaveinfo.leaveId , employee.username , leaveinfo.leaveDate , leaveinfo.leaveTill, leaveinfo.leavetype,leaveinfo.leavenumber from leaveinfo join roleassign join employee on leaveinfo.userid=roleassign.userid && employee.id=leaveinfo.userid && roleassign.leadid=? && leaveinfo.approval=? ";
	public static final String GET_EMPLOYEE_LEAVE = "SELECT l FROM Leave l WHERE l.userId = :userid";
	public static final String GET_EMPLOYEE_LEAVE_BALANCE = "SELECT l FROM EmployeeLeaveType l WHERE l.employeeid = :userid";
	public static final String UPDATE_EMPLOYEE_BALANCE = "update employeeleave set casual=?,earn=?,duty=?,sick=?,maternity=?,parental=?,leavewithoutpay=? where id=?";
	public static final String GET_LEADID_FROM_ROLE_ASSIGN = "select leadid from roleassign where userid=?";
	public static final String GET_MANAGERID_FROM_ROLE_ASSIGN = "select managerid from roleassign where userid=?";
}
