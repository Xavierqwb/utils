package com.xavier.exception;

import com.sun.xml.internal.ws.api.model.CheckedException;

/**
 * Created by qiuwenbin on 2017/9/2.
 *
 */
public class HttpException extends Exception{

	public HttpException(String message) {
		super(message);
	}

	public HttpException() {
		super();
	}

	public HttpException(Throwable e) {
		super(e);
	}
}
