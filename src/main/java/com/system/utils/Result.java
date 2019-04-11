package com.system.utils;

public class Result {

	private Integer code ; 
	private String message ; 
	private Object obj  ;
	
	public Result() {}
	public Result(Integer code, String message, Object obj) {
		super();
		this.code = code;
		this.message = message;
		this.obj = obj;
	}
	public static Result success(Object obj) {
		return new Result(200,"",obj);
	}
	
	public static Result fail(String e ) {
		return new Result(500,e,"");
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	
}
