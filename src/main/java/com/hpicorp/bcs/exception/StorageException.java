package com.hpicorp.bcs.exception;

public class StorageException extends RuntimeException {

	private static final long serialVersionUID = -1400123175644107767L;

	public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
