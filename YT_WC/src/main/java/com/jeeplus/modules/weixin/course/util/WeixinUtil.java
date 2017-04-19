package com.jeeplus.modules.weixin.course.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import com.jeeplus.common.config.Global;
import com.jeeplus.modules.weixin.course.message.push.Message;
import com.jeeplus.modules.weixin.course.message.push.pojo.Articles;
import com.jeeplus.modules.weixin.course.message.response.pojo.Article;
import com.jeeplus.modules.weixin.pojo.AccessToken;
import com.jeeplus.modules.weixin.pojo.Menu;

/**
 * 公众平台通用接口工具类
 * 
 * @author jdq
 * @date 2013-08-09
 */
public class WeixinUtil {
	private static Logger log = LoggerFactory.getLogger(WeixinUtil.class);
	// 获取access_token的接口地址（GET） 限200（次/天）
	public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	// 菜单创建的接口地址（POST） 限100（次/天）
	public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	// 上传多媒体文件的接口地址（POST） 
	public static String upload_media_url = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	// 下载多媒体文件的接口地址（POST） 
	public static String get_media_url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
	// 上传图文消息素材的接口地址（POST） 
	public static String upload_article_url = "https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=ACCESS_TOKEN";
	// 根据分组进行群发的接口地址（POST） 
	public static String sendall_url = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=ACCESS_TOKEN";
	// 项目的url
	public static String project_url = Global.getConfig("project_url");   //Global.loPropertiesUtil.getResourceString("project_url");
	// 项目图片的url
	public static String img_url = Global.getConfig("img_url");//PropertiesUtil.getResourceString("img_url");

	/**
	 * 获取access_token
	 * 
	 * @param appid
	 *            凭证
	 * @param appsecret
	 *            密钥
	 * @return
	 */
	public static AccessToken getAccessToken(String appid, String appsecret) {
		AccessToken accessToken = null;

		String requestUrl = access_token_url.replace("APPID", appid).replace(
				"APPSECRET", appsecret);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		// 如果请求成功
		if (null != jsonObject) {
			try {
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getIntValue("expires_in"));
			} catch (JSONException e) {
				accessToken = null;
				// 获取token失败
				log.error("获取token失败 errcode:{} errmsg:{}",
						jsonObject.getIntValue("errcode"),
						jsonObject.getString("errmsg"));
			}
		}
		return accessToken;
	}

	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param outputStr
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequest(String requestUrl,
			String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
					.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.parseObject(buffer.toString());
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}
		return jsonObject;
	}

	/**
	 * 创建菜单
	 * 
	 * @param menu
	 *            菜单实例
	 * @param accessToken
	 *            有效的access_token
	 * @return 0表示成功，其他值表示失败
	 */
	public static int createMenu(Menu menu, String accessToken) {
		int result = 0;

		// 拼装创建菜单的url
		String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);
		// 将菜单对象转换成json字符串
		String jsonMenu = JSONObject.toJSONString(menu);
		System.out.println(jsonMenu);
		// 调用接口创建菜单
		JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);

		if (null != jsonObject) {
			if (0 != jsonObject.getIntValue("errcode")) {
				result = jsonObject.getIntValue("errcode");
			}
		}

		return result;
	}

	/**
	 * 当用户关注后回复关于我们页面的图文消息
	 * 
	 * @return 关于我们页面的图文消息
	 */
	public static List<Article> showAboutUs() {
		List<Article> lstArticle = new ArrayList<Article>();
		Article objArticle;

		objArticle = new Article();
		objArticle.setTitle("公司简介");
		objArticle.setDescription("感谢关注Veron欧派贝隆，『欧派贝隆』板材扎根于华北地区，是天津是欧派商贸有限公司2015年重点打造的项目，品牌自创建之初旨在代表对高品质矢志不渝的崇尚，代表对环保健康永不妥协的信仰，代表对伙伴关系亲密无间的珍视，代表对环保乃至于可持续发展孜孜不倦的追求。公司以天津生产工厂为主要生产基地，面向全国家具行业客户、木质板材经销商、和其他工程类商业组织提供产品和服务。今天，欧派贝隆的产品被广泛的应用于厨房、卫生间、起居室、卧室、办公室、书房、以及其他各种家用及商用领域之中。");
		objArticle.setPicUrl(WeixinUtil.img_url);
		objArticle.setUrl(WeixinUtil.project_url);
		lstArticle.add(objArticle);

		return lstArticle;
	}

	/**
	 * 计算出和用户发送的位置信息最近的位置，并返回BbUserModel
	 * 
	 * @param locationX
	 *            坐标x
	 * @param locationY
	 *            坐标y
	 * 
	 * @return 最近的销售联系信息
	 */
	/*public static BbSalesModel doMinLocation(double locationX, double locationY) {
		//销售联系人service
		BbSalesManager bbSalesManager = ContextUtils.getBeanOfType(BbSalesManager.class);
		List<BbSalesModel> lstSalesModel;
		BbSalesModel objSalesModel;
		BbSalesModel objMinSalesModel = null;
		double dLineA;
		double dLineB;
		double dLineC;
		double dMinLineC = 0;

		lstSalesModel = bbSalesManager.getAll();
		if (null == lstSalesModel) {
			return objMinSalesModel;
		}
		for (int n = 0; n < lstSalesModel.size(); n++) {
			objSalesModel = lstSalesModel.get(n);
			if (null == objSalesModel) {
				continue;
			}
			if (null == objSalesModel.getBbsaX()
					|| objSalesModel.getBbsaX().length() <= 0
					|| null == objSalesModel.getBbsaY()
					|| objSalesModel.getBbsaY().length() <= 0) {
				continue;
			}
			dLineA = Double.parseDouble(objSalesModel.getBbsaX()) - locationX;
			if (dLineA < 0) {
				dLineA = 0 - dLineA;
			}

			dLineB = Double.parseDouble(objSalesModel.getBbsaY()) - locationY;
			if (dLineB < 0) {
				dLineB = 0 - dLineB;
			}

			// 根据勾股定理运算直线
			dLineC = Math.pow(Math.pow(dLineA, 2) + Math.pow(dLineB, 2), 1.0 / 2);
			if (dMinLineC == 0 || dMinLineC > dLineC) {
				objMinSalesModel = objSalesModel;
				dMinLineC = dLineC;
			}
		}
		return objMinSalesModel;
	}*/

	/**
	 * 获取最新的图文消息，并返回要回复的图文消息
	 * 
	 * @return 最新的图文消息
	 */
	public static List<Article> getNewArticle() {
		//图文消息service
	    /*BbArticleManager bbArticleManager = ContextUtils.getBeanOfType(BbArticleManager.class);
		List<FindNewArticleAndAccQueryItem> lstItem;
		FindNewArticleAndAccQueryItem objItem;
		List<Article> lstArticle = new ArrayList<Article>();
		Article objArticle;

		lstItem = bbArticleManager.findNewArticleDetail();
		if (null == lstItem || lstItem.size() <= 0) {
			return lstArticle;
		}
		for (int n = 0; n < lstItem.size(); n++) {
			objItem = lstItem.get(n);
			if (null == objItem) {
				continue;
			}

			objArticle = new Article();
			objArticle.setDescription(objItem.getBbarDesc());
			objArticle.setTitle(objItem.getBbarTitle());
			objArticle.setPicUrl(WeixinUtil.project_url + objItem.getSyaccUrl());
			objArticle.setUrl(WeixinUtil.project_url + objItem.getBbarUrl());
			lstArticle.add(objArticle);
		}
*/
		return null;
	}

	/**
	 * 上传多媒体信息
	 * 
	 * @param filePath
	 *            文件路径
	 * @param type
	 *            多媒体类型
	 * 
	 * @return 上传的mediaId
	 */
	public static String uploadMedia(String filePath, String type) throws IOException {
		String result = null;
		String strMediaId = "";
		String line;
		File objFile;
		URL objUrl;
		HttpURLConnection objHttpUrlConn;
		StringBuilder sbufString;
		byte[] head;
		OutputStream out;
		DataInputStream in;
		int bytes = 0;
		byte[] foot;
		JSONObject jsonObject;

		objFile = new File(filePath);
		if (!objFile.exists() || !objFile.isFile()) {
			throw new IOException("文件不存在");
		}

		// 设置url
		objUrl = new URL(upload_media_url.replace("ACCESS_TOKEN",
				TokenThread.accessToken.getToken()).replace("TYPE", type));
		// 连接
		objHttpUrlConn = (HttpURLConnection) objUrl.openConnection();
		// 以Post方式提交表单，默认get方式
		objHttpUrlConn.setRequestMethod("POST");
		objHttpUrlConn.setDoInput(true);
		objHttpUrlConn.setDoOutput(true);
		// post方式不能使用缓存
		objHttpUrlConn.setUseCaches(false);
		// 设置请求头信息
		objHttpUrlConn.setRequestProperty("Connection", "Keep-Alive");
		objHttpUrlConn.setRequestProperty("Charset", "UTF-8");
		// 设置边界
		String BOUNDARY = "----------" + System.currentTimeMillis();
		objHttpUrlConn.setRequestProperty("Content-Type", "multipart/form-data; boundary="
				+ BOUNDARY);

		// 请求正文信息
		sbufString = new StringBuilder();
		sbufString.append("--"); // 必须多两道线
		sbufString.append(BOUNDARY);
		sbufString.append("\r\n");
		sbufString.append("Content-Disposition: form-data;name=\"file\";filename=\""
				+ objFile.getName() + "\"\r\n");
		sbufString.append("Content-Type:application/octet-stream\r\n\r\n");

		head = sbufString.toString().getBytes("utf-8");

		// 获得输出流
		out = new DataOutputStream(objHttpUrlConn.getOutputStream());
		// 输出表头
		out.write(head);

		// 文件正文部分
		// 把文件已流文件的方式 推入到url中
		in = new DataInputStream(new FileInputStream(objFile));
		byte[] bufferOut = new byte[1024];
		while ((bytes = in.read(bufferOut)) != -1) {
			out.write(bufferOut, 0, bytes);
		}
		in.close();

		// 结尾部分
		foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
		out.write(foot);
		out.flush();
		out.close();

		sbufString.delete(0, sbufString.length());
		BufferedReader reader = null;
		try {
			// 定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(
					objHttpUrlConn.getInputStream()));
			while ((line = reader.readLine()) != null) {
				sbufString.append(line);
			}
			if (result == null) {
				result = sbufString.toString();
			}
		} catch (IOException e) {
			System.out.println("发送POST请求出现异常！" + e);
			e.printStackTrace();
			throw new IOException("数据读取异常");
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

		jsonObject = JSONObject.parseObject(result);
		if (null != jsonObject) {
			if (0 != jsonObject.getIntValue("errcode")) {
				log.error("上传多媒体信息失败 errcode:{} errmsg:{}",
						jsonObject.getIntValue("errcode"),
						jsonObject.getString("errmsg"));
			} else {
				strMediaId = jsonObject.getString("media_id");
			}
		}
		return strMediaId;
	}

	/**
	 * 上传图文消息素材
	 * 
	 * @param articles
	 *            图文素材对象
	 * 
	 * @return 图文素材的mediaId
	 */
	public static String uploadNews(Articles articles) throws IOException {
		String strMediaId = "";
		JSONObject jsonObject;
		String json;

		// 将素材转换成json字符串
		json = JSONObject.toJSONString(articles);
		// 调用接口创建菜单
		jsonObject = httpRequest(upload_article_url.replace("ACCESS_TOKEN",
				TokenThread.accessToken.getToken()), "POST", json);
		if (null != jsonObject) {
			if (0 != jsonObject.getIntValue("errcode")) {
				log.error("上传图文消息素材失败 errcode:{} errmsg:{}",
						jsonObject.getIntValue("errcode"),
						jsonObject.getString("errmsg"));
			} else {
				strMediaId = jsonObject.getString("media_id");
			}
		}
		return strMediaId;
	}

	/**
	 * 根据分组群发消息
	 * 
	 * @param message
	 *            群发消息对象
	 * 
	 * @return 群发消息的msgId
	 */
	public static String pushMessage(Message message) throws IOException {
		String strMsgId = "";
		JSONObject jsonObject;
		String json;

		// 将素材转换成json字符串
		json = JSONObject.toJSONString(message);
		// 调用接口创建菜单
		jsonObject = httpRequest(sendall_url.replace("ACCESS_TOKEN",
				TokenThread.accessToken.getToken()), "POST", json);
		if (null != jsonObject) {
			if (0 != jsonObject.getIntValue("errcode")) {
				log.error("推送消息失败 errcode:{} errmsg:{}",
						jsonObject.getIntValue("errcode"),
						jsonObject.getString("errmsg"));
			} else {
				strMsgId = jsonObject.getString("msg_id");
			}
		}
		return strMsgId;
	}
}