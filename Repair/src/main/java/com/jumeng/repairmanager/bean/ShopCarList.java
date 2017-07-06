package com.jumeng.repairmanager.bean;

public class ShopCarList {
	private int shopCarId;
	private int productId;
	private int count;
	private String img;
	private String name;
	private double price;
	private double totalPrice;
	private boolean isChoosed;
	
	public ShopCarList() {
		super();
	}

	public int getShopCarId() {
		return shopCarId;
	}

	public void setShopCarId(int shopCarId) {
		this.shopCarId = shopCarId;
	}



	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public boolean isChoosed() {
		return isChoosed;
	}

	public void setChoosed(boolean isChoosed) {
		this.isChoosed = isChoosed;
	}


}





