package com.xavier.exception;

import lombok.Data;

/**
 * Created by qiuwenbin on 2017/8/12.
 */
@Data
public class JsonParseException extends RuntimeException {
	private String message;

	public JsonParseException(String message, Throwable e){
		super(message, e);
		this.message = message;
	}

	public JsonParseException(String message){
		this.message = message;
	}
}
