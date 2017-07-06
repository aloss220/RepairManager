package com.jumeng.repairmanager.util;

public class Consts {

	/**基础接口地址 */
	public static final String BASE="http://139.129.110.219:805/index.php";//最新接口(php)
	public static final String UJIANG_PROTOCOL=BASE+"/home/public/article";//U匠协议


	//public static final String BASE="http://139.129.110.219/";//基础接口地址（远程）
	//public static final String BASE = "http://139.129.110.219:8088/"; //基础接口地址（测试）
	//public static final String BASE="http://zt.jumenging.com/";//基础接口地址（本地）
	//public static final String BASE="http://192.168.1.99/";//基础接口地址（本地）
	/*public static final String WORKER=BASE+"Webservice/Workerwebservice.asmx/";//工人端接口地址
	public static final String PUBLIC=BASE+"Webservice/Publicwebservice.asmx/";//公共接口地址*/

	public static final String QINIUYUN ="http://os18w14e3.bkt.clouddn.com/";//七牛云图片域名
	//public static final String QINIUYUN ="http://oscakuwph.bkt.clouddn.com/";//七牛云图片域名(测试)
	//public static final String QINIUYUN ="osenv7cs9.bkt.clouddn.com/";//七牛云图片域名(测试)

	public static final String GETCODE ="GetTestIngacp";//获取验证码
	public static final String GETALLTYPE ="getalltype";//工种列表
	public static final String GETINFO ="getusermodel";//获取工人信息
	public static final String GETUSER ="getuser";//获取用户信息
	public static final String GETPROVINCE ="getprovince";//获取省级列表
	public static final String GETCITY ="getcity";//获取市级列表
	public static final String GETDISTRICT ="getdistrict";//获取区级列表
	public static final String GETLOGIN ="getlogin";//工人登录
	public static final String LOGGEDOUT ="Loggedout";//退出登录
	public static final String GETISJIEDAN ="getisjiedan";//判断工人能否抢单
	public static final String UODATENAME ="uodatename";//修改姓名
	public static final String UPDATEJG ="updatejg";//修改籍贯
	public static final String UODATETIMELONG ="uodatetimelong";//修改从业时间
	public static final String UPDATETEL ="updatetel";//修改手机号码
	public static final String UODATESEX ="uodatesex";//修改性别
	public static final String UODATEICON ="uodateicon";//修改图像
	public static final String UPDATESMRZ ="updatesmrz";//完善实名验证
	public static final String GETISORDER ="getisorder";//是否接单
	public static final String GETBANGDING ="getbangding";//绑定银行卡
	public static final String GETUPDATESERVICE ="getupdateservice";//修改工种
	public static final String GOODS ="goods";//配件
	public static final String ANOTHER ="another";//异地登录
	public static final String UPLOAD_TOKEN ="upload_token";//获取七牛云上传凭证




	public static final String GETKOUCHUCAILIAO ="getkouchucailiao";//最大可提现金额
	public static final String ADDWITHDRAWAL ="Addwithdrawal";//提现申请
	public static final String GETLEIJI ="getleiji";
	public static final String GETSHOURU ="getshouru";//累计收入
	public static final String GETCZJL ="getczjl";//收支记录
	public static final String GETPJLIST ="getpjlist";//评价列表
	public static final String TUISONGORDERLIST ="tuisongorderlist";//推送订单列表
	public static final String GETORDERXIANGXI ="getorderxiangxi";//订单详情
	public static final String WORKERGOTO ="workergoto";//工人到达
	public static final String WORKERFUWU ="workerfuwu";//开始服务
	public static final String WORKERFUWUEND ="workerfuwuend";//结束服务
	public static final String REASON ="reason";//取消订单理由
	public static final String CANCELHOUORDER ="getquxiaoshenqing";//取消订单申请
	public static final String WORKERJIEDAN ="workerjiedan";//抢单
	public static final String GETBAOJIA ="getbaojia";//提交报价单









	public static final String IMAGE_URL=BASE+"Upload/images/";//图片路径
	public static final String REGISTER_URL=BASE+"WebAPP/Appweb.aspx?MenuID=23&type=2";//注册协议
	public static final String GUIDE_URL=BASE+"WebAPP/Appweb.aspx?MenuID=20&type=2";//用户指导
	public static final String VERSION_URL=BASE+"WebAPP/Appweb.aspx?MenuID=22&type=2";//版本信息


	public static final String WELCOME_PAGE=BASE+"Upload/index/yindao.jpg";//欢迎界面
	public static final String GETTESTING ="GetTestIngacp";//获取验证码（加密）


	public static final String GETUPDATE = "getupdate";// 版本更新
	public static final String WORKERORDERLIST ="workerorderlist";//我的订单列表







	public static final String GETTX ="gettx";//查看累计提现 

	public static final String GETFWUSERLIST ="getfwuserlist";//服务客户
	public static final String GETFULI ="getfuli";//权益福利
	public static final String GETINFOLIST ="getinfolist";//系统消息




	public static final String GETQUXIAOSHENQING ="getquxiaoshenqing";//取消订单申请



	public static final String GETWORKER ="getworker";//获取个人信息









	public static final String GETS_CITY ="getS_City";//获取城市列表
	public static final String GETS_DISTRICT ="getS_District";//获取区县列表
	public static final String GETMERCHANTSLIST ="getmerchantslist";//店铺列表
	public static final String GETMERCHANTS ="getmerchants";//店铺详情
	public static final String GETMERCHANTSPRODUCT ="getmerchantsproduct";//主营商品
	
	public static final String GETHXLOGINNAME ="gethxloginname";//获取环信账号
	
	public static final String JUDGEISANOTHERLOGIN = "judgeisanotherlogin";//检查异地登录



	public static final String GETPRODUCTTYPEONE ="getproducttypeone";//商品一级分类
	public static final String GETPRODUCTTYPETWO ="getproducttypetwo";//商品二级分类
	public static final String GETPRODUCTLIST ="getproductlist";//商品列表
	public static final String GETPRODUCTDETAILED ="getproductdetailed";//商品详情
	public static final String ADDSHOP ="Addshop";//加入购物车
	public static final String GETPLACEORDER ="getPlaceorder";//确认下单
	public static final String GETSHOPLIST ="getshoplist";//获取购物车信息
	public static final String GETPRODUCTORDERLIST ="GetProductOrderList";//商城订单
	public static final String CONFIRMGOODS ="Confirmgoods";//确认收货
	public static final String DELETESHOPCAR ="deleteshopcar";//删除购物车
	public static final String GETUPDATESHOPNUM ="getupdateshopnum";//增减购物车数量


	public static final int GUIDEPAGE=123456;//用户指导
	public static final int VERSIONINFO=123457;//版本信息
	public static final int REGISTERPROTOCAL=123458;//注册协议

	/**sharedpreferences中保存的数据 */
	public static final String USER_FILE_NAME = "user_file_name"; // 用户信息保存文件名
	public static final String UPDATE_FILE_NAME = "UPDATE_FILE_NAME"; // 用户信息保存文件名
	public static final String GUIDE_FILE_NAME = "guide"; // 
	public static final String CLIENT_ID = "client_id"; // 个推ID
	public static final String USER_ID = "user_id"; // 用户ID
	public static final String USER_PSW = "user_psw"; // 用户密码
	public static final String USER_PHONE = "user_phone"; // 用户手机
	public static final String USER_PHOTO = "user_photo"; // 用户头像
	public static final String USER_NAME = "nick_name"; // 用户昵称
	public static final String USER_LEVEL_ICON = "user_level_icon"; // 用户等级图片
	public static final String IM = "im"; //获取环信用户



	public static final String HX_USER_NAME = "hx_user_name"; // 环信用户名
	public static final String GETHXICON = "gethxicon";//通过环信账号查看资料


	/**广播 */
	public static final String UPDATEPERSONINFO = "change.info.broadcast.action"; //修改昵称
	public static final String LOGOUT = "logout.broadcast.action"; //退出登录
	public static final String CHANGE_MY_INFO = "change.my.info.broadcast.action"; //修改个人信息
	public static final String CHANGE_POHNE = "change.phone.broadcast.action"; //修改手机
	public static final String ORDER_STATUS = "order.status.broadcast.action"; //订单状态改变
	public static final String ROB_ORDER = "order.rob.broadcast.action"; //订单状态改变
	public static final String CREATE_ORDER = "order.create.broadcast.action"; //商品下单成功
	public static final String REFESH = "refesh.broadcast.action";//广播通知
	public static final String START_WORK = "start.work.broadcast.action"; //上班
	public static final String END_WORK = "end.work.broadcast.action"; //下班
	public static final String NO_PASS = "no.pass.broadcast.action"; //审核未通过
	public static final String PASS = "pass.broadcast.action"; //审核未通过
	public static final String SEARCH_TYPE = "search.type.broadcast.action"; //搜索条件
	public static final String PRODUCT_SEND = "product.send.broadcast.action"; //商品发货
	public static final String RESTART_SERVICE = "com.jumeng.repairmanager.receiver.destroy"; //重启serivce
	public static final String GET_GOODS_INFO = "get.goods.info.broadcast.action";


}
