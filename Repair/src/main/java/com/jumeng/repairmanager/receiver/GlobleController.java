package com.jumeng.repairmanager.receiver;

import java.util.ArrayList;
import java.util.List;

public class GlobleController {

	
	private List<MyListener> listeners=new ArrayList<>();

	private GlobleController() {
	}

	private static class Inner{
		private static final GlobleController INSTANCE=new GlobleController();
	}

	public static final GlobleController getInstance() {
		return Inner.INSTANCE;
	}

	
	public void addMyListener(MyListener listener){
		if(listeners.contains(listener)){
			return;
		}
		listeners.add(listener);
	}
	
	public void removeMyListener(MyListener listener){
		if(!listeners.contains(listener)){
			return;
		}
		listeners.add(listener);
	}
	
	/**
	 * 新訂單
	 */
	public void notifyNewOrder(){
		for(MyListener l:listeners){
			l.newOrder();
		}
		
	}
	public void notifyRobOrder(){
		for(MyListener l:listeners){
			l.robOrder();
		}
		
	}
	/**
	 * 支付订单
	 */
	public void notifyPayOrder(){
		for(MyListener l:listeners){
			l.payOrder();
		}
		
	}
	/**
	 * 取消服務
	 */
	public void notifycancelOrder(){
		for(MyListener l:listeners){
			l.cancelOrder();
		}
		
	}
	/**
	 * 結束服務
	 */
	public void notifyEndService(){
		for(MyListener l:listeners){
			l.endService();
		}
		
	}
	/**
	 * 開始服務
	 */
	public void notifytartService(){
		for(MyListener l:listeners){
			l.startService();
		}
		
	}
	/**
	 * 工人已到達
	 */
	public void notifyArrive(){
		for(MyListener l:listeners){
			l.arrive();
		}
		
	}
	/**
	 * 開始服務
	 */
	public void notifyServiceForm(){
		for(MyListener l:listeners){
			l.serviceForm();
		}
		
	}
	 
	/**
	 * 系統指派
	 */
	public void notifysystemForm(){
		for(MyListener l:listeners){
			l.systemForm();
		}
		
	}
	/**
	 * 审核通过
	 */
	public void notifyPass(){
		for(MyListener l:listeners){
			l.pass();
		}
		
	}
	/**
	 * 异地登录
	 */
	public void notifyUnusualLogin(){
		for(MyListener l:listeners){
			l.unusualLogin();
		}
		
	}
	public interface MyListener{
		void newOrder();
		void robOrder();
		void payOrder();
		void cancelOrder();
		void arrive();
		void serviceForm();
		void systemForm();
		void startService();
		void endService();
		void pass();
		void unusualLogin();
		
	}
}
