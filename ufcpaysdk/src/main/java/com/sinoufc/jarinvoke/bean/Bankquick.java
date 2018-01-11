package com.sinoufc.jarinvoke.bean;

import java.io.Serializable;

/*
 * 添加快捷银行卡界面中保存快捷卡返回的卡信息
 */
public class Bankquick implements Serializable{

		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		private String result;
	     
		private Info value;
	    
	    public void setResult(String result) {
			this.result = result;
		}

		public void setValue(Info value) {
			this.value = value;
		}

		public String getResult() {
	    	return result;
	    }
	    
	    public Info getValue() {
	    	return value;
	    }
	    
	   public static class  Info implements Serializable{
			/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
			private String ID;
			private String BANKNAME;
			private String BANKCODE;
			private String TYPENAME;
			private String CARDNO;
			
			public static long getSerialversionuid() {
				return serialVersionUID;
			}
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
			
	    }
}
