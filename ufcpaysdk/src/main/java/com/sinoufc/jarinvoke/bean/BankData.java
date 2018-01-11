package com.sinoufc.jarinvoke.bean;

import java.util.List;

/**
 * 添加快捷银行卡中获取银行卡列表的Javabean
 */

public class BankData {
	public List<BankList> bankNameArray;
	public String result;
	
	
	public class BankList {
		private String VAL;
		private String TEXT;
		
		public String getVAL() {
			return VAL;
		}
		public String getTEXT() {
			return TEXT;
		}
		
	}
	
	public List<BankList> getBankNameArray() {
		return bankNameArray;
	}
	public String getResult() {
		return result;
	}
}
