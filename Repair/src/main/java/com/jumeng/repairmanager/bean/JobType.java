package com.jumeng.repairmanager.bean;

public class JobType {


	private int jobId;
	private String icon;
	private String name;
	private boolean isChoose;
	
	
	public JobType() {
		super();
	}


	public int getJobId() {
		return jobId;
	}


	public void setJobId(int jobId) {
		this.jobId = jobId;
	}


	public String getIcon() {
		return icon;
	}


	public void setIcon(String icon) {
		this.icon = icon;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public boolean isChoose() {
		return isChoose;
	}


	public void setChoose(boolean isChoose) {
		this.isChoose = isChoose;
	}



}





