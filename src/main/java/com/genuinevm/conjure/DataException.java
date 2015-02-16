package com.genuinevm.conjure;

public class DataException extends Exception {

	private static final long serialVersionUID = 5390413692398447202L;

	public DataException(final String message, final Data<?> data) {
		super("Data exception: " + message + " Data: " + data.getClass().getSimpleName());
	}
}
