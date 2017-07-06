package com.jumeng.repairmanager.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import com.jumeng.repairmanager.view.LoadingDialog;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

public class Tools {
	private static String version;
	private static Toast toast;
	



	public static void toast(Context context, String msg) {
		if (toast == null) {
			toast = Toast.makeText(context,
					msg, 
					Toast.LENGTH_SHORT);
		} else {
			toast.setText(msg);
		}
		toast.show();
	}

	public static void showInfo(Context context,String info){
		/*LayoutInflater inflater=LayoutInflater.from(context);
		View view=inflater.inflate(R.layout.showinfo, null);
		Toast toast=new Toast(context);
		toast.setView(view);
		TextView tv=(TextView) view.findViewById(R.id.showInfo);
		tv.setText(info);
		toast.show();*/

	}



	public static <T> void StartActivitytoOther(Context context, Class<T> cla) {
		Intent intent=new Intent(context, cla);
		context.startActivity(intent);
	}

	
	 //版本名  
    public static String getVersionName(Context context) {  
        return getPackageInfo(context).versionName;  
    }  
      
    //版本号  
    public static int getVersionCode(Context context) {  
        return getPackageInfo(context).versionCode;  
    }  
      
    private static PackageInfo getPackageInfo(Context context) {  
        PackageInfo pi = null;  
      
        try {  
            PackageManager pm = context.getPackageManager();  
         // getPackageName()是你当前类的包名，0代表是获取版本信息
            pi = pm.getPackageInfo(context.getPackageName(),  
                    PackageManager.GET_CONFIGURATIONS);  
      
            return pi;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
      
        return pi;  
    }  
	
	/**
	 *判断当前应用程序处于前台还是后台
	 */
	public static boolean isApplicationBroughtToBackground(final Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;

	}


	/**
	 * 获取当前app的应用名字
	 */
	public static String getApplicationName(Context context) {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = context.getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(context.
					getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName = (String) packageManager
				.getApplicationLabel(applicationInfo);
		return applicationName;
	}

	/**
	 * 将map转为json
	 * @param map
	 * @return 返回json串
	 */
	public static  String getJsonStr(Map<String, Object> map) {
		String jsonStr = "{";  
		Set<?> keySet = map.keySet();  
		for (Object key : keySet) {  
			jsonStr += "\""+key+"\":\""+map.get(key)+"\",";       
		}  
		jsonStr = jsonStr.substring(0,jsonStr.length()-1);  
		jsonStr += "}";  
		return jsonStr;
	}


	/**
	 * 获取assets文件夹下的文件
	 * @param context
	 * @param fileName  如citylist.json
	 * @return
	 */
	public static String getJson(Context context, String fileName) {

		StringBuilder stringBuilder = new StringBuilder();
		try {
			AssetManager assetManager = context.getAssets();
			BufferedReader bf = new BufferedReader(new InputStreamReader(
					assetManager.open(fileName)));
			String line;
			while ((line = bf.readLine()) != null) {
				stringBuilder.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}

	/**
	 * 获取某年，某月的天数的集合
	 * @param year
	 * @param month
	 * @return
	 */
	public static  List<String> getDays(String y, String m) {
		List<String> days=new ArrayList<String>();
		boolean flag = false;
		int year = Integer.parseInt(y);
		int month = Integer.parseInt(m);
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			for(int i=1;i<32;i++){
				days.add(String.valueOf(i));
			}
			break;
		case 2:
			if(flag){
				for(int i=1;i<30;i++){
					days.add(String.valueOf(i));
				}
			}else{
				for(int i=1;i<29;i++){
					days.add(String.valueOf(i));
				}
			}
			break;
		default:
			for(int i=1;i<31;i++){
				days.add(String.valueOf(i));
			}
			break;
		}
		return days;
	}
	/**
	 * 获取某年，某月的天数
	 * @param year
	 * @param month
	 * @return
	 */
	public static  int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}

	/**
	 * 根据ListView的子项目重新计算ListView的高度，然后把高度再作为LayoutParams设置给ListView
	 */
	/*public static void setListViewHeightBasedOnChildren(ListView listView) { 
		CommentAdapter listAdapter =(CommentAdapter) listView.getAdapter(); 
		if (listAdapter == null) { 
			// pre-condition 
			return; 
		} 
		int totalHeight = 0; 
		for (int i = 0; i < listAdapter.getCount(); i++) { 
			View listItem = listAdapter.getView(i, null, listView); 
			listItem.measure(0, 0); 
			totalHeight += listItem.getMeasuredHeight(); 
		} 
		ViewGroup.LayoutParams params = listView.getLayoutParams(); 
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)); 
		listView.setLayoutParams(params); 
	} */



	/**
	 * 获取设备的唯一识别码
	 * @param context
	 * @return
	 */
	public static String getMyUUID(Context context){
		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		//(TelephonyManager)context.getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);   
		final String tmDevice, tmSerial, tmPhone, androidId;   
		tmDevice = "" + tm.getDeviceId();  
		tmSerial = "" + tm.getSimSerialNumber();   
		androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);   
		UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());   
		String uniqueId = deviceUuid.toString();
		Log.d("debug","uuid="+uniqueId);
		return uniqueId;
	}


	/**
	 * 根据当前时间生成文件名前缀
	 * @return cutnameString
	 */
	public static  String GetcutnameString() {
		Date date = new Date(System.currentTimeMillis());  
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");  
		String cutnameString = dateFormat.format(date);
		return cutnameString;
	}

	/**
	 * 将bitmap转化为字符串
	 * */
	public static String BitmapToString(Bitmap bitmap) {
		// 将Bitmap转换成字符串
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 50, bStream);
		byte[] bytes = bStream.toByteArray();
		// Base64.encodeToString(bytes, Base64.DEFAULT);
		return Base64.encodeToString(bytes, Base64.DEFAULT);
	}

	/**  
	 * string转成bitmap  
	 *   
	 * @param 传入的 String 是Base64 格式 不然是会为空的！
	 */  
	public static Bitmap StringToBitmap(String st)  
	{  
		// OutputStream out;  
		Bitmap bitmap = null;  
		try  
		{  
			// out = new FileOutputStream("/sdcard/aa.jpg");  
			byte[] bitmapArray;  
			bitmapArray = Base64.decode(st, Base64.DEFAULT);  
			bitmap =  
					BitmapFactory.decodeByteArray(bitmapArray, 0,  
							bitmapArray.length);  
			// bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);  
			return bitmap;  
		}  
		catch (Exception e)  
		{  
			return null;  
		}  
	}  



	/**
	 * 将字符串类型的数字转为double并保留两位小数
	 * */
	public static double StringToDouble(String s) {
		DecimalFormat df = new DecimalFormat("######0.00");
		double d=Double.parseDouble(s);
		/*  String ss=df.format(d);
	   double result = (Double) df.parse(ss);*/
		return d;
	}

	/**
	 * 按照需要分割字符串
	 * @param s 字符串
	 * @param l 每几个字符后插入分割符
	 * @return
	 */
	public static String Subs(String s,int l){
		String ss="";
		for(int i=0;i<=s.length()/l;i++){
			if(i*l+l<s.length()){
				ss=ss+s.substring(i*l, Math.min(i*l+l, s.length()))+"-";
			}else{
				ss=ss+s.substring(i*l, Math.min(i*l+l, s.length()));
			}
		}
		return ss;
	}



	public static void CancelDialog(Context context,String message,String positive,String negative) {
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setPositiveButton( positive,new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		} );
		builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();

	}
	/**
	 * 
	 * @param 传入周几
	 * @return 获取星期几
	 */
	public static String getWeek(int week){
		String weekDay="";
		switch (week) {
		case 1:weekDay="周一";break;
		case 2:weekDay="周二";break;
		case 3:weekDay="周三";break;
		case 4:weekDay="周四";break;
		case 5:weekDay="周五";break;
		case 6:weekDay="周六";break;
		case 7:weekDay="周日";break;
		}
		return weekDay;

	}
	/**
	 * 
	 * @param 传入一周的第几天
	 * @return 获取星期几
	 */

	public static String getWeek2(int dayOfWeek){
		String week="";
		switch (dayOfWeek) {
		case 1:week="周日";break;
		case 2:week="周一";break;
		case 3:week="周二";break;
		case 4:week="周三";break;
		case 5:week="周四";break;
		case 6:week="周五";break;
		case 7:week="周六";break;

		}
		return week;

	}

	/**
	 * 选择日期滚轮
	 * @param textView
	 * @param context
	 */
	public static void showDateDialog(final TextView textView,Context context) {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int monthOfYear = c.get(Calendar.MONTH);
		int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
		DatePickerDialog dialog = new DatePickerDialog(context, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				Calendar c = Calendar.getInstance();
				c.set(Calendar.YEAR, year);
				c.set(Calendar.MONTH, monthOfYear);
				c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
				String date = f.format(c.getTime());
				textView.setText(date);
				//	int startId = getResources().getIdentifier("tv_start_time", "id", "com.jumeng.user");
				try {
					long start = f.parse(date).getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

		}, year, monthOfYear, dayOfMonth);
		dialog.show();
	}
	/**
	 * 
	 * java 用递归获取一个目录下的所有文件路径
	 */

	public static  List<String> ergodic(File file,List<String> resultFileName){
		File[] files = file.listFiles();
		if(files==null)return resultFileName;// 判断目录下是不是空的
		for (File f : files) {
			if(f.isDirectory()){// 判断是否文件夹
				resultFileName.add(f.getPath());
				ergodic(f,resultFileName);// 调用自身,查找子目录
			}else
				resultFileName.add(f.getPath());
		}
		return resultFileName;
	}

	/**
	 * 
	 * String[] ext={".mp3", ".awv",".png",".jpg"};//定义我们要查找的文件格式  
		File file=Environment.getExternalStorageDirectory();//获得SD卡的路径  
		File file=new File("/mnt/shell/emulated/0");
	 * java 用递归获取一个目录下的所有文件路径
	 */
	public List<String> Search(File file ,String[] ext){  
		List<String> list=new ArrayList<String>();
		if(file!=null){  
			if(file.isDirectory()){//如果是文件夹  
				File[] listFile=file.listFiles();//列出所有的文件放在listFile这个File类型数组中  
				if(listFile!=null){  
					for(int i=0;i<listFile.length;i++)
						Search(listFile[i], ext);//递归，直到把所有文件遍历完  
				}  
			}  
		}else{//否则就是文件  
			String fileName=file.getAbsolutePath();//返回抽象路径名的绝对路径名字符串  
			String name=file.getName();//获得文件的名称  
			for(int i=0;i<ext.length;i++)
				if(fileName.endsWith(ext[i])){//判断文件后缀名是否包含我们定义的格式  
					list.add(name);  
				}  
		}
		return list;  
	}  

	/**
	 * 递归获取所有文件名
	 * @param dir   File root = new File("F:\\迅雷下载");
	 * @throws Exception
	 */
	public static void showAllFiles(File dir) throws Exception{
		File[] fs = dir.listFiles();
		for(int i=0; i<fs.length; i++){
			System.out.println(fs[i].getName());
			if(fs[i].isDirectory()){
				try{
					showAllFiles(fs[i]);
				}catch(Exception e){}
			}
		}
	}


	public static void Dial(Context context,String phone){
		Intent intent=new Intent();
		intent.setAction(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:"+phone));
		context.startActivity(intent);


	}
	public static void Dial2(Context context,String phone, LoadingDialog loadingDialog){
		
		Intent intent=new Intent();
		intent.setAction(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:"+phone));
		context.startActivity(intent);
		if(loadingDialog!=null){
			loadingDialog.dismiss();
		}
		
	}


	/**
	 * 验证手机号码正则表达式
	 */
	public static boolean isMobileNO(String mobiles) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）、177
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		String telRegex = "[1][34578]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);
	}




	/**
	 * 通过Uri得到压缩以后的图片
	 */
	public static Bitmap getBitmapFromBigImagByUri(Uri uri, Context context,
			int width, int heigh) {
		Bitmap result = null;
		InputStream is1 = null;
		InputStream is2 = null;
		try {
			// uri转换为流
			is1 = context.getContentResolver().openInputStream(uri);
			is2 = context.getContentResolver().openInputStream(uri);

			Options opts1 = new Options();
			opts1.inJustDecodeBounds = true;
			opts1.inPreferredConfig = Config.ARGB_4444;
			// 解析流
			BitmapFactory.decodeStream(is1, null, opts1);
			int bmpWidth = opts1.outWidth;
			int bmpHeight = opts1.outHeight;
			// 设置缩放比例
			int scale = Math.max(bmpWidth / width, bmpHeight / heigh);
			Options opts2 = new Options();
			opts2.inSampleSize = scale;
			// 内存不足时可被回收
			opts2.inPurgeable = true;
			// 设置为false,表示不仅Bitmap的属性，也要加载bitmap
			opts2.inJustDecodeBounds = false;
			result = BitmapFactory.decodeStream(is2, null, opts2);

		} catch (Exception ex) {
		} finally {
			if (is1 != null) {
				try {
					is1.close();
				} catch (IOException e1) {
				}
			}
			if (is2 != null) {
				try {
					is2.close();
				} catch (IOException e2) {
				}
			}
		}
		return result;
	}
	/**
	 * RSA加密
	 */
	public static String RSASign (LinkedHashMap<String, String> map){

		/*for (Iterator i = map.keySet().iterator(); i.hasNext();) {
			Object obj = i.next();
			System.out.println(obj);// 循环输出key
			System.out.println("key=" + obj + " value=" + map.get(obj));
		}*/

		String jsonStr ="";  
		Set<String> keySet = map.keySet();  
		for (String key : keySet) {  
			if(map.get(key)!=null&&!map.get(key).isEmpty()){
				jsonStr += key+"="+map.get(key)+"&";       
			}
		}  
		jsonStr = jsonStr.substring(0,jsonStr.length()-1);  
		//return jsonStr;
		return SignUtils.sign(jsonStr, Sign.RSA_PRIVATE);
	}




	/**
	 * 根据图片的url路径获得Bitmap对象
	 * @param url
	 * @return
	 */
	public static Bitmap Url2Bitmap(String url) {
		URL fileUrl = null;
		Bitmap bitmap = null;

		try {
			fileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		try {
			HttpURLConnection conn = (HttpURLConnection) fileUrl
					.openConnection();
			conn.setDoInput(true);//设置是否开启输入  如果在请求网络的时候需要传参这个是必须开启的
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;

	}

	public static Bitmap getBitmap(String imgPath) {  
		// Get bitmap through image path  
		Options newOpts = new Options();
		newOpts.inJustDecodeBounds = false;  
		newOpts.inPurgeable = true;  
		newOpts.inInputShareable = true;  
		// Do not compress  
		newOpts.inSampleSize = 1;  
		newOpts.inPreferredConfig = Config.RGB_565;  
		return BitmapFactory.decodeFile(imgPath, newOpts);  
	}  

	/**
	 * 根据图片的url路径获得Bitmap对象
	 * @param url
	 * @return
	 */
	public static Bitmap Url2Bitmap2(String path) throws IOException{
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if(conn.getResponseCode() == 200){
			InputStream inputStream = conn.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			return bitmap;
		}
		return null;

	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public static String getStrBase64(String str) {
		String strBase64=new String (Base64.encode(str.getBytes(), Base64.DEFAULT));
		return strBase64;

	}
	/**
	 * 将字符串以逗号分割，放入集合
	 * @param url
	 * @return
	 */
	public static List<String > getSplitAssemble(String str) {
		List<String> list=new ArrayList<String>();
		String [] split=str.split(",");
		for(int i=0;i<split.length;i++){
			list.add(split[i]);
		}
		return list;
	}



	/**
	 * 将小于10的数字前面加0  变成双数
	 * @param url
	 * @return
	 */
	public static String getDoubleDate(int n){
		String s=n<10?"0"+n:n+"";

		return s;
	}



	/**
	 * 判断字符串是否为空或null
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
        return str.isEmpty() || str.equals("null") || str.equals("(null)") || str == null;
    }
	/**
	 * 判断字符串不为空或null
	 * @param str
	 * @return
	 */
	public static boolean isNoEmpty(String str) {
        return !str.isEmpty() && !str.equals("null") && !str.equals("(null)") && str != null;
    }
	/**
	 * 判断是否有安装某软件
	 * @param str
	 * @return
	 */

	public static boolean isInstall(String packageName){

		return new File("/data/data/"+packageName).exists();
	}


	public static void lightUpScreen(Context context){
		PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
		WakeLock mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.SCREEN_DIM_WAKE_LOCK, "SimpleTimer");
		mWakelock.acquire();
		mWakelock.release();


		KeyguardManager keyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
		KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");
		keyguardLock.disableKeyguard();


	}
	
	
	   public static boolean isAppOnForeground(Context context) {  
           // Returns a list of application processes that are running on the  
           // device  
              
           ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);  
           String packageName = context.getApplicationContext().getPackageName();  

           List<RunningAppProcessInfo> appProcesses = activityManager  
                           .getRunningAppProcesses();  
           if (appProcesses == null)  
                   return false;  

           for (RunningAppProcessInfo appProcess : appProcesses) {  
                   // The name of the process that this object is associated with.  
                   if (appProcess.processName.equals(packageName)  
                                   && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {  
                           return true;  
                   }  
           }  

           return false;  
   }

	/**
	 * 十进制转其他进制（36进制以内）
	 * @param num  十进制数
	 * @param base  目标进制
	 * @return
	 */
	public static String baseString(int num,int base) {
		String str = "", digit =
				"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		if(num == 0){
			return "";
		}else {
			str = baseString(num / base,base);
			return str + digit.charAt(num % base);
		}
	}

	/**
	 * 获取当天年月日格式 2017&5@11
	 * @return
	 */
	public static String  getNowTime(){
		String result="";
		Calendar now=Calendar.getInstance();
		int year=now.get(Calendar.YEAR);
		int month=now.get(Calendar.MONTH)+1;
		int day=now.get(Calendar.DAY_OF_MONTH);
		result=year+"&"+month+"@"+day;
		return result;
	}

	/**
	 * 对字符串md5加密
	 *
	 * @param str
	 * @return
	 */
	public static String getMD5(String str) {
		try {
			// 生成一个MD5加密计算摘要
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 计算md5函数
			md.update(str.getBytes());
			// digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
			// BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值

			return new BigInteger(1, md.digest()).toString(16);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取短信加密参数   (6位随机数)*转36进制+*+当前日期2017&5@11）*MD5小写加
	 密
	 *  @param digital  6位可重复随机数
	 * @return
	 */

	public static String getTargetStr(int digital){
		String result="";
		String digital36=baseString(digital, 36);
		String dayTime=getNowTime();
		String str=digital36+"*"+dayTime;
		return getMD5(str);

	}


	public static String formatString(String s) {
		if (s != null) {
			s = s.replaceAll("\ufeff", "");
		}
		return s;
	}

	/**
	 * 生成随机字符串
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) { //length表示生成字符串的长度
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString().toUpperCase();
	}


	/**
	 * 获取时间戳
	 * @return
     */
	public static String getTimeStamp(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sdf.format(new Date());
	}

	public static byte[] bitmap2Byte(Bitmap bitmap){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}

}
