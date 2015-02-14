package com.genuinevm.data;

public class DataException extends Exception {

	public DataException(final String message, final Data data) {
		super("Data exception: " + message + " Data: " + data.getClass().getSimpleName());
	}
}
