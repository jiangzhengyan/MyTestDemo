package com.sinoufc.jarinvoke.bean;

/*
 * 下单后到支付页面的数据信息
 */
public class PayInfo {
	
	public String wxappId;
	
	public String subject;
	
	public String body;
	
	public String result_code;

	public String showOrderNo;
	
	public String orderNo;
	
	public String userName;
	
	public String orderId;

	public String coreUserId;
	
	public String payMoney;
	
	public String dismoney;
	
	public String balance;
	
	public String userType;
	
	public String comId;
	
	public String quickArray;
	
	public String bankArray;
	
	public String orderType;
	
	public String realType;
	
	public String returnUrl;
	
	public String receiveUrl;
	
	public String needClose;
	
	
	public String getNeedClose() {
		return needClose;
	}

	public void setNeedClose(String needClose) {
		this.needClose = needClose;
	}

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}
	
	public String getQuickArray() {
		return quickArray;
	}

	public void setQuickArray(String quickArray) {
		this.quickArray = quickArray;
	}

	public String getBankArray() {
		return bankArray;
	}

	public void setBankArray(String bankArray) {
		this.bankArray = bankArray;
	}


	public String getWxappId() {
		return wxappId;
	}

	public void setWxappId(String wxappId) {
		this.wxappId = wxappId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getShowOrderNo() {
		return showOrderNo;
	}

	public void setShowOrderNo(String showOrderNo) {
		this.showOrderNo = showOrderNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCoreUserId() {
		return coreUserId;
	}

	public void setCoreUserId(String coreUserId) {
		this.coreUserId = coreUserId;
	}

	public String getDismoney() {
		return dismoney;
	}

	public void setDismoney(String dismoney) {
		this.dismoney = dismoney;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getComId() {
		return comId;
	}

	public void setComId(String comId) {
		this.comId = comId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getRealType() {
		return realType;
	}

	public void setRealType(String realType) {
		this.realType = realType;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getReceiveUrl() {
		return receiveUrl;
	}

	public void setReceiveUrl(String receiveUrl) {
		this.receiveUrl = receiveUrl;
	}

}
