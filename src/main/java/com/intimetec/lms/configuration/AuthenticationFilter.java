package com.intimetec.lms.configuration;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.intimetec.lms.utility.LmsConstants;

public class AuthenticationFilter implements Filter {
	private ServletContext context;
	public static final String FILTER = "AuthenticationFilter initialized";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.context = filterConfig.getServletContext();
		this.context.log(FILTER);
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest servletRequest = (HttpServletRequest) request;
		HttpServletResponse servletResponse = (HttpServletResponse) response;

		HttpSession session = servletRequest.getSession(false);

		String loginUrlUser = servletRequest.getContextPath() + "/index.html";

		boolean userLoggenIn = session != null && session.getAttribute(LmsConstants.EMPLOYEE_NAME) != null;
		boolean loginRequestUser = loginUrlUser.equals(servletRequest.getRequestURI());

		if (userLoggenIn || loginRequestUser) {
			chain.doFilter(request, response);
		} else {
			servletResponse.sendRedirect(loginUrlUser);
		}
	}

	@Override
	public void destroy() {
	}
}
