package com.intimetec.lms.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.intimetec.lms.utility.LmsConstants;

@Controller
public class EmployeeSignout {

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	protected void doSignOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		session.removeAttribute(LmsConstants.EMPLOYEE_NAME);
		session.removeAttribute(LmsConstants.EMPLOYEE_ID);
		session.removeAttribute(LmsConstants.EMPLOYEE_DESIGNATION);
		session.invalidate();
		response.sendRedirect(LmsConstants.INDEX_SCREEN);
	}

}
