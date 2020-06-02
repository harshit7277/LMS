package com.intimetec.lms.exception;

public class PermissionNotGrantedException extends Exception {
	private static final long serialVersionUID = 1L;

	public PermissionNotGrantedException(String message) {
		super(message);
	}
}
