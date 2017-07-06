package com.jumeng.repairmanager.bean;

public class ProductTypeTwo {


	private int id;
	private String name;
	private String img;
	private int imgId;
	public ProductTypeTwo() {
		super();
	}
	
	public ProductTypeTwo(String name, int imgId) {
		super();
		this.name = name;
		this.imgId = imgId;
	}
	
	

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}


}





