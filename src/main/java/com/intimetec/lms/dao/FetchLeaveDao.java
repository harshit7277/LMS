package com.intimetec.lms.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.intimetec.lms.model.EmployeeLeaveType;
import com.intimetec.lms.model.Leave;
import com.intimetec.lms.utility.LmsConstants;
import com.intimetec.lms.utility.LmsDbQuery;

@Repository
@Transactional
public class FetchLeaveDao {

	@PersistenceContext
	private EntityManager manager;

	@SuppressWarnings("unchecked")
	public List<Leave> getAllLeaveById(String userid) {
		Query query = manager.createQuery(LmsDbQuery.GET_EMPLOYEE_LEAVE);
		query.setParameter(LmsConstants.LEAVE_USERID, userid);
		List<Leave> leaveList = (List<Leave>) query.getResultList();
		return leaveList;
	}

	public EmployeeLeaveType getEmployeeBalance(String userid) {
		Query query = manager.createQuery(LmsDbQuery.GET_EMPLOYEE_LEAVE_BALANCE);
		query.setParameter(LmsConstants.LEAVE_USERID, userid);
		return (EmployeeLeaveType) query.getSingleResult();
	}
}
