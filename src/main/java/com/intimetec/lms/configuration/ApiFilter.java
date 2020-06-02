package com.intimetec.lms.configuration;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.intimetec.lms.utility.LmsConstants;

public class ApiFilter implements Filter {

	final static Logger LOGGER = Logger.getLogger(ApiFilter.class.getName());

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest servletRequest = (HttpServletRequest) request;
		HttpServletResponse servletResponse = (HttpServletResponse) response;

		HttpSession session = servletRequest.getSession(false);
		String tokenUrl = servletRequest.getContextPath() + "/employees/token";
		String token = null;
		boolean userLoggedIn = session != null && session.getAttribute(LmsConstants.TOKEN) != null;
		boolean tokenRequestUser = tokenUrl.equals(servletRequest.getRequestURI());

		String header = servletRequest.getHeader(LmsConstants.AUTHORIZATION);

		if (!tokenRequestUser) {
			if (header.startsWith(LmsConstants.AUTHORIZATION_BEARER)) {
				token = header.toString().substring(LmsConstants.AUTHORIZATION_BEARER.length());
			}
		}

		if (userLoggedIn || tokenRequestUser || token.equals(session.getAttribute(LmsConstants.TOKEN))) {
			chain.doFilter(request, response);
		} else {
			servletResponse.getWriter().write(LmsConstants.URL_NOT_ACCESS);
		}

	}

	public void init(FilterConfig fConfig) throws ServletException {

	}

}
