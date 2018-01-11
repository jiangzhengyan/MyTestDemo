package com.sinoufc.jarinvoke.bean;


/**
 * 下单后到支付界面的快捷银行卡列表
 */

public class QuickInfo {
	
	private String ID;
	
	private String BANKNAME;
	
	private String BANKCODE;
	
	private String TYPENAME;
	
	public void setID(String iD) {
		ID = iD;
	}

	public void setBANKNAME(String bANKNAME) {
		BANKNAME = bANKNAME;
	}

	public void setBANKCODE(String bANKCODE) {
		BANKCODE = bANKCODE;
	}

	public void setTYPENAME(String tYPENAME) {
		TYPENAME = tYPENAME;
	}

	public void setCARDNO(String cARDNO) {
		CARDNO = cARDNO;
	}

	public void setIS_DEFAULT(String iS_DEFAULT) {
		IS_DEFAULT = iS_DEFAULT;
	}

	private String CARDNO;
	
	private String IS_DEFAULT;

	public String getID() {
		return ID;
	}

	public String getBANKNAME() {
		return BANKNAME;
	}

	public String getBANKCODE() {
		return BANKCODE;
	}

	public String getTYPENAME() {
		return TYPENAME;
	}

	public String getCARDNO() {
		return CARDNO;
	}

	public String getIS_DEFAULT() {
		return IS_DEFAULT;
	}

	

}
