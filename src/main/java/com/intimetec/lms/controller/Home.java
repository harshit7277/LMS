package com.intimetec.lms.controller;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.intimetec.lms.utility.LmsConstants;

@Controller()
public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	protected void getHome(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		String currentLoginUserDesignation = (String) session.getAttribute(LmsConstants.EMPLOYEE_DESIGNATION);
		String servletResponse = null;

		if (currentLoginUserDesignation.equals(LmsConstants.EXECUTIVE)) {
			servletResponse = LmsConstants.EXECUTIVE_SCREEN;
		} else if (currentLoginUserDesignation.equals(LmsConstants.LEAD)) {
			servletResponse = LmsConstants.LEAD_SCREEN;
		} else if (currentLoginUserDesignation.equals(LmsConstants.MANAGER)) {
			servletResponse = LmsConstants.MANAGER_SCREEN;
		} else if (currentLoginUserDesignation.equals(LmsConstants.EMPLOYEE_ADMIN)) {
			servletResponse = LmsConstants.ADMIN_SCREEN;
		} else {
			servletResponse = LmsConstants.INDEX_SCREEN;
		}

		response.setContentType(LmsConstants.TEXT);
		response.sendRedirect(servletResponse);
	}

}
