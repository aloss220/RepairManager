package com.jumeng.repairmanager.bean;

import java.util.List;

public class StoreOrderOut {
	private int orderId;
	private String orderNum;
	private double price;
	private int status;
	private List<StoreOrderIn> storeOrderIn;
	public StoreOrderOut() {
		super();
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public List<StoreOrderIn> getStoreOrderIn() {
		return storeOrderIn;
	}
	public void setStoreOrderIn(List<StoreOrderIn> storeOrderIn) {
		this.storeOrderIn = storeOrderIn;
	}
	

}





