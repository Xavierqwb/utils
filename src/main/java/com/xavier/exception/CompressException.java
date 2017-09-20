package com.xavier.exception;

/**
 * Created by qiuwenbin on 2017/9/20.
 */
public class CompressException extends RuntimeException{

	private String message;

	public CompressException(String message, Throwable e){
		super(message, e);
		this.message = message;
	}

	public CompressException(String message){
		this.message = message;
	}
}
