package com.jumeng.repairmanager.bean;

public class CustomerList {

	private int userId;
	private String name;
	private String account;
	
	private String icon;
	private int gender;
	private String hxloginname;
	public CustomerList() {
		super();
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getHxloginname() {
		return hxloginname;
	}
	public void setHxloginname(String hxloginname) {
		this.hxloginname = hxloginname;
	}
	


}





