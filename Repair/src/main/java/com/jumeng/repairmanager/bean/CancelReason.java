package com.jumeng.repairmanager.bean;

/**
 * 描述：价格明细</br>
 */
public class CancelReason {
	
	private int  id;
	private String reason;
	private boolean isChoosed;
	
	
	public CancelReason() {
		super();
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public CancelReason(String reason) {
		super();
		this.reason = reason;
	}


	public String getReason() {
		return reason;
	}


	public void setReason(String reason) {
		this.reason = reason;
	}

	public boolean isChoosed() {
		return isChoosed;
	}

	public void setChoosed(boolean choosed) {
		isChoosed = choosed;
	}

	@Override
	public String toString() {
		return "CancelReason [reason=" + reason + "]";
	}

	
	
	
}
