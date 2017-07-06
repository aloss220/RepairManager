package com.jumeng.repairmanager.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;

import com.jumeng.repairmanager.activity.OrderDetailActivity;
import com.jumeng.repairmanager.adapter.PhotoListAdapter;
import com.jumeng.repairmanager.bean.BalanceList;
import com.jumeng.repairmanager.bean.BusinessList;
import com.jumeng.repairmanager.bean.CancelReason;
import com.jumeng.repairmanager.bean.CityList;
import com.jumeng.repairmanager.bean.CommentList;
import com.jumeng.repairmanager.bean.CustomerList;
import com.jumeng.repairmanager.bean.DistrictList;
import com.jumeng.repairmanager.bean.GoodsList;
import com.jumeng.repairmanager.bean.JobType;
import com.jumeng.repairmanager.bean.MessageList;
import com.jumeng.repairmanager.bean.OrderList;
import com.jumeng.repairmanager.bean.ProductList;
import com.jumeng.repairmanager.bean.ProductTypeOne;
import com.jumeng.repairmanager.bean.ProductTypeTwo;
import com.jumeng.repairmanager.bean.RightList;
import com.jumeng.repairmanager.bean.ShopCarList;
import com.jumeng.repairmanager.bean.StoreOrderIn;
import com.jumeng.repairmanager.bean.StoreOrderOut;

import static com.jumeng.repairmanager.R.id.gridView;

public class JsonParser {

	/**
	 * 获取订单列表
	 * @param ary
	 * @return
	 * @throws JSONException
	 */
	public static List<OrderList> parseOrderList (JSONArray ary) throws JSONException{
		List<OrderList> list=new ArrayList<OrderList>();
		for(int i=0;i<ary.length();i++){
			JSONObject obj=ary.getJSONObject(i);
			OrderList orderList=new OrderList();
			orderList.setAddress(obj.getString("address"));
			orderList.setContact(obj.getString("user_phone"));
			orderList.setItem(obj.getString("name"));
			orderList.setNumber(obj.getString("order_no"));
			orderList.setIcon(obj.getString("avatar"));
			orderList.setOrderId(obj.getInt("id"));
			orderList.setOrderTime(obj.getString("create_time"));
			orderList.setPrice(obj.getString("total_amount"));
			orderList.setServiceTime(obj.getString("order_time"));
			orderList.setStatus(obj.getInt("order_status"));
			orderList.setStar(obj.getInt("star_level"));

			list.add(orderList);
		}
		return list;
	}
	/**
	 * 获取推送订单列表
	 * @param ary
	 * @return
	 * @throws JSONException
	 */
	public static List<OrderList> parsePushOrderList (JSONArray ary) throws JSONException{
		List<OrderList> list=new ArrayList<OrderList>();
		for(int i=0;i<ary.length();i++){
			JSONObject obj=ary.getJSONObject(i);
			OrderList orderList=new OrderList();
			orderList.setAddress(obj.getString("adress"));
			orderList.setItem(obj.getString("potion"));
			orderList.setNumber(obj.getString("order_no"));
			orderList.setOrderId(obj.getInt("id"));
			orderList.setOrderTime(obj.getString("create_time"));
			orderList.setPrice(obj.getString("hejimoney"));
			orderList.setServiceTime(obj.getString("statustime"));
			orderList.setStatus(obj.getInt("status"));

			list.add(orderList);
		}
		return list;
	}
	/**
	 * 获取工种列表
	 * @param ary
	 * @return
	 * @throws JSONException
	 */
	public static List<JobType> parseJobTypeList (JSONArray ary) throws JSONException{
		List<JobType> list=new ArrayList<JobType>();
		for(int i=0;i<ary.length();i++){
			JSONObject obj=ary.getJSONObject(i);
			JobType jobType=new JobType();
			jobType.setIcon(obj.getString("icon"));
			jobType.setJobId(obj.getInt("id"));
			jobType.setName(obj.getString("typename"));

			list.add(jobType);
		}
		return list;
	}
	/**
	 * 获取配件列表
	 * @param ary
	 * @return
	 * @throws JSONException
	 */
	public static List<GoodsList> parseGoodsList (JSONArray ary) throws JSONException{
		List<GoodsList> list=new ArrayList<GoodsList>();
		for(int i=0;i<ary.length();i++){
			JSONObject obj=ary.getJSONObject(i);
			GoodsList goods=new GoodsList();
			goods.setId(obj.getInt("id"));
			goods.setName(obj.getString("name"));
			goods.setPrice(obj.getDouble("price"));

			list.add(goods);
		}
		return list;
	}
	/**
	 * 获取城市列表
	 * @param ary
	 * @return
	 * @throws JSONException
	 */
	public static List<CityList> parsePCDList (JSONArray ary) throws JSONException{
		List<CityList> list=new ArrayList<CityList>();
		for(int i=0;i<ary.length();i++){
			JSONObject obj=ary.getJSONObject(i);
			CityList cityList=new CityList();
			cityList.setId(obj.getInt("id"));
			cityList.setName(obj.getString("name"));
			list.add(cityList);
		}
		int a=list.size();
		return list;
	}
	/**
	 * 获取收支列表
	 * @param ary
	 * @return
	 * @throws JSONException
	 */
	public static List<BalanceList> parseIOList (JSONArray ary) throws JSONException{
		List<BalanceList> list=new ArrayList<BalanceList>();
		for(int i=0;i<ary.length();i++){
			JSONObject obj=ary.getJSONObject(i);
			BalanceList balanceList=new BalanceList();
			balanceList.setItemId(obj.getInt("id"));
			balanceList.setMoney(obj.getString("money"));
			balanceList.setTime(obj.getString("createtime"));
			balanceList.setWorkerId(obj.getInt("nursing"));
			balanceList.setType(obj.getInt("type"));
			balanceList.setStatus(obj.getInt("status"));
			list.add(balanceList);
		}
		return list;
	}
	/**
	 * 获取服务过的客户
	 * @param ary
	 * @return
	 * @throws JSONException
	 */
	public static List<CustomerList> parseCustomerList (JSONArray ary) throws JSONException{
		List<CustomerList> list=new ArrayList<CustomerList>();
		for(int i=0;i<ary.length();i++){
			JSONObject obj=ary.getJSONObject(i);
			CustomerList customerList=new CustomerList();
			customerList.setAccount(obj.getString("phone_number"));
			customerList.setGender(obj.getInt("gender"));
			customerList.setIcon(obj.getString("icon"));
			customerList.setName(obj.getString("nickname"));
			customerList.setUserId(obj.getInt("id"));
			customerList.setHxloginname(obj.getString("hxloginname"));
			list.add(customerList);
		}
		return list;
	}
	/**
	 * 获取评价列表
	 * @param ary
	 * @return
	 * @throws JSONException
	 */
	public static List<CommentList> parseCommentList (JSONArray ary) throws JSONException{
		List<CommentList> list=new ArrayList<CommentList>();
		for(int i=0;i<ary.length();i++){
			JSONObject obj=ary.getJSONObject(i);
			CommentList commentList=new CommentList();
			commentList.setContent(obj.getString("nurscontent"));

		/*	JSONArray imageAry=new JSONArray(obj.getString("images"));
			if(imageAry.length()>0){
				String [] imgs=new String[imageAry.length()];
				for(int j=0;j<imageAry.length();j++){
					imgs[i]=(String)imageAry.get(i);
					commentList.setImage((String)imageAry.get(i));
				}
			}*/
			commentList.setImage(obj.getString("images"));
			//commentList.setItemId(obj.getInt("id"));
			commentList.setOrderNum(obj.getString("orderbianhao"));
			commentList.setStar(obj.getInt("grade"));
			commentList.setTime(obj.getString("create_time"));

			list.add(commentList);
		}
		return list;
	}
	/**
	 * 获取权益福利
	 * @param ary
	 * @return
	 * @throws JSONException
	 */
	public static List<RightList> parseRightList (JSONArray ary) throws JSONException{
		List<RightList> list=new ArrayList<RightList>();
		for(int i=0;i<ary.length();i++){
			JSONObject obj=ary.getJSONObject(i);
			RightList rightList=new RightList();
			rightList.setContent(obj.getString("InfoContent"));
			rightList.setItemId(obj.getInt("id"));
			rightList.setTime(obj.getString("InfoDate"));
			rightList.setTitle(obj.getString("Infotitle"));
			rightList.setIntro(obj.getString("jianjie"));
			list.add(rightList);
		}
		return list;
	}
	/**
	 * 获取消息列表
	 * @param ary
	 * @return
	 * @throws JSONException
	 */
	public static List<MessageList> parseMessageList (JSONArray ary) throws JSONException{
		List<MessageList> list=new ArrayList<MessageList>();
		for(int i=0;i<ary.length();i++){
			JSONObject obj=ary.getJSONObject(i);
			MessageList messageList=new MessageList();
			messageList.setContent(obj.getString("InfoContent"));
			messageList.setIntro(obj.getString("jianjie"));
			messageList.setItemId(obj.getInt("id"));
			messageList.setTime(obj.getString("InfoDate"));
			messageList.setTitle(obj.getString("Infotitle"));

			list.add(messageList);
		}
		return list;
	}

	/**
	 * 获取取消订单原因
	 * @param ary
	 * @return
	 * @throws JSONException
	 */
	public static List<CancelReason> parseCancelReason (JSONArray ary) throws JSONException{
		List<CancelReason> list=new ArrayList<CancelReason>();
		for(int i=0;i<ary.length();i++){
			JSONObject obj=ary.getJSONObject(i);
			CancelReason cancelReason=new CancelReason();
			cancelReason.setId(obj.getInt("id"));
			cancelReason.setReason(obj.getString("title"));
			list.add(cancelReason);
		}
		return list;
	}
	/**
	 * 商品一级分类
	 * @param ary
	 * @return
	 * @throws JSONException
	 */
	public static List<ProductTypeOne> parseProductOne (JSONArray ary) throws JSONException{
		List<ProductTypeOne> list=new ArrayList<ProductTypeOne>();
		for(int i=0;i<ary.length();i++){
			JSONObject obj=ary.getJSONObject(i);
			ProductTypeOne typeOne=new ProductTypeOne();
			typeOne.setId(obj.getInt("id"));
			typeOne.setName(obj.getString("Producttypename"));
			list.add(typeOne);
		}
		return list;
	}
	/**
	 * 商品一级分类
	 * @param ary
	 * @return
	 * @throws JSONException
	 */
	public static List<DistrictList> parseProductOne2 (JSONArray ary) throws JSONException{
		List<DistrictList> list=new ArrayList<DistrictList>();
		for(int i=0;i<ary.length();i++){
			JSONObject obj=ary.getJSONObject(i);
			DistrictList typeOne=new DistrictList();
			typeOne.setDistrictId(obj.getInt("id"));
			typeOne.setDistrict(obj.getString("Producttypename"));
			list.add(typeOne);
		}
		return list;
	}
	/**
	 * 商品二级分类
	 * @param ary
	 * @return
	 * @throws JSONException
	 */
	public static List<ProductTypeTwo> parseProductTwo (JSONArray ary) throws JSONException{
		List<ProductTypeTwo> list=new ArrayList<ProductTypeTwo>();
		for(int i=0;i<ary.length();i++){
			JSONObject obj=ary.getJSONObject(i);
			ProductTypeTwo typeTwo=new ProductTypeTwo();
			typeTwo.setId(obj.getInt("id"));
			typeTwo.setName(obj.getString("Producttypename"));
			typeTwo.setImg(obj.getString("images"));
			list.add(typeTwo);
		}
		return list;
	}
	/**
	 * 商品列表
	 * @param ary
	 * @return
	 * @throws JSONException
	 */
	public static List<ProductList> parseProductList (JSONArray ary) throws JSONException{
		List<ProductList> list=new ArrayList<ProductList>();
		for(int i=0;i<ary.length();i++){
			JSONObject obj=ary.getJSONObject(i);
			ProductList productList=new ProductList();
			productList.setId(obj.getInt("id"));
			productList.setName(obj.getString("Productname"));
			productList.setImg(obj.getString("image"));
			productList.setMoney(obj.getDouble("Price"));
			list.add(productList);
		}
		return list;
	}
	/**
	 * 购物车列表
	 * @param ary
	 * @return
	 * @throws JSONException
	 */
	public static List<ShopCarList> parseShopCarList (JSONArray ary) throws JSONException{
		List<ShopCarList> list=new ArrayList<ShopCarList>();
		for(int i=0;i<ary.length();i++){
			JSONObject obj=ary.getJSONObject(i);
			ShopCarList shopList=new ShopCarList();
			shopList.setCount(obj.getInt("shopnum"));
			shopList.setImg(obj.getString("image"));
			shopList.setName(obj.getString("Productname"));
			shopList.setPrice(obj.getDouble("Price"));
			shopList.setShopCarId(obj.getInt("id"));
			shopList.setProductId(obj.getInt("productid"));
			shopList.setTotalPrice(obj.getDouble("moneynum"));
			list.add(shopList);
		}
		return list;
	}
	/**
	 * 商品列表
	 * @param ary
	 * @return
	 * @throws JSONException
	 */
	public static List<StoreOrderOut> parseStoreOutList (JSONArray ary) throws JSONException{
		List<StoreOrderOut> list=new ArrayList<StoreOrderOut>();
		for(int i=0;i<ary.length();i++){
			JSONObject obj=ary.getJSONObject(i);
			StoreOrderOut storeList=new StoreOrderOut();
			storeList.setOrderId(obj.getInt("ID"));
			storeList.setOrderNum(obj.getString("bianhao"));
			storeList.setPrice(obj.getDouble("monery"));
			storeList.setStatus(obj.getInt("status"));
			storeList.setStoreOrderIn(parseStoreIntList(new JSONArray(obj.getString("productList"))));
			
			list.add(storeList);
		}
		return list;
	}
	/**
	 * 商品列表
	 * @param ary
	 * @return
	 * @throws JSONException
	 */
	public static List<StoreOrderIn> parseStoreIntList (JSONArray ary) throws JSONException{
		List<StoreOrderIn> list=new ArrayList<StoreOrderIn>();
		for(int i=0;i<ary.length();i++){
			JSONObject obj=ary.getJSONObject(i);
			StoreOrderIn storeList=new StoreOrderIn();
			storeList.setCount(obj.getInt("num"));
			storeList.setImg(obj.getString("Image"));
			storeList.setName(obj.getString("Productname"));
			storeList.setPrice(obj.getDouble("price"));
			storeList.setProductId(obj.getInt("productid"));
			list.add(storeList);
		}
		return list;
	}
	/**
	 * 区县城市列表
	 * @param ary
	 * @return
	 * @throws JSONException
	 */
	public static List<DistrictList> parseCityList (JSONArray ary) throws JSONException{
		List<DistrictList> list=new ArrayList<DistrictList>();
		for(int i=0;i<ary.length();i++){
			JSONObject obj=ary.getJSONObject(i);
			DistrictList district=new DistrictList();
			district.setDistrictId(obj.getInt("id"));
			district.setDistrict(obj.getString("CityName"));
			list.add(district);
		}
		return list;
	}
	/**
	 * 区县列表
	 * @param ary
	 * @return
	 * @throws JSONException
	 */
	public static List<DistrictList> parseDistrictList (JSONArray ary) throws JSONException{
		List<DistrictList> list=new ArrayList<DistrictList>();
		for(int i=0;i<ary.length();i++){
			JSONObject obj=ary.getJSONObject(i);
			DistrictList district=new DistrictList();
			district.setDistrictId(obj.getInt("id"));
			district.setDistrict(obj.getString("DistrictName"));
			list.add(district);
		}
		return list;
	}
	
	
	/**
	 * 商家列表
	 * @param ary
	 * @return
	 * @throws JSONException
	 */
	public static List<BusinessList> parseShopList (JSONArray ary) throws JSONException{
		List<BusinessList> list=new ArrayList<BusinessList>();
		for(int i=0;i<ary.length();i++){
			JSONObject obj=ary.getJSONObject(i);
			BusinessList shop=new BusinessList();
			shop.setAddress(obj.getString("adress"));
			shop.setDistance(obj.getString("distance"));
			shop.setIcon(obj.getString("icon"));
			shop.setShopId(obj.getInt("id"));
			shop.setShopName(obj.getString("merchantsName"));
			list.add(shop);
		}
		return list;
	}
	
	/**
	 * 主营商品
	 * @param ary
	 * @return
	 * @throws JSONException
	 */
	public static List<ProductTypeTwo> parseMainProductList (JSONArray ary) throws JSONException{
		List<ProductTypeTwo> list=new ArrayList<ProductTypeTwo>();
		for(int i=0;i<ary.length();i++){
			JSONObject obj=ary.getJSONObject(i);
			ProductTypeTwo product=new ProductTypeTwo();
			product.setId(obj.getInt("ID"));
			product.setName(obj.getString("Productname"));
			product.setImg(obj.getString("icon"));
			list.add(product);
		}
		return list;
	}
}
