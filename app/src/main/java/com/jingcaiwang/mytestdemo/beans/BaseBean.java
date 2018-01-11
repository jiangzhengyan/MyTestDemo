package com.jingcaiwang.mytestdemo.beans;

import java.io.Serializable;

/**
 * 描述：公用
 * 
 * @author wangjian
 * 
 */
public class BaseBean implements Serializable
{
	private static final long serialVersionUID = 1876345352L;

	private boolean status;
	private String msg;
	private String data;
	private String count;
	private String success;
	private String errorMsg;
	private String message;

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getErrorMsg()
	{
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg)
	{
		this.errorMsg = errorMsg;
	}

	public boolean isStatus()
	{
		return status;
	}

	public void setStatus(boolean status)
	{
		this.status = status;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public String getData()
	{
		return data;
	}

	public void setData(String data)
	{
		this.data = data;
	}

	public String getCount()
	{
		return count;
	}

	public void setCount(String count)
	{
		this.count = count;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	 

	@Override
	public String toString() {
		return "BaseBean [status=" + status + ", msg=" + msg + ", data=" + data
				+ ", count=" + count + ", success=" + success + ", errorMsg="
				+ errorMsg + ", message=" + message + "]";
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
}
