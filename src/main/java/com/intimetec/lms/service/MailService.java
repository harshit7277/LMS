package com.intimetec.lms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.intimetec.lms.dao.EmployeeDao;
import com.intimetec.lms.model.Employee;
import com.intimetec.lms.model.Leave;

@Service("emailService")
public class MailService {

	public static final String REQUESTED_LEAVE = "Requested Leave";
	public static final String LEAVE_REQUESTED_FOR = "leave requested for";
	@Autowired
	private EmployeeDao employeeDao;
	@Autowired
	private JavaMailSender mailSender;

	public void sendMail(Leave leaveDetails) {
		Employee employee = getEmployee(leaveDetails);
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(employee.getEmail());
		message.setSubject(REQUESTED_LEAVE);
		message.setText(LEAVE_REQUESTED_FOR + " " + leaveDetails.getLeaveFrom());
		mailSender.send(message);
	}

	private Employee getEmployee(Leave leaveDetails) {
		return employeeDao.getEmployeeProfile(leaveDetails.getUserId());
	}
}
