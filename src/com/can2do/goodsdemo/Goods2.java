package com.can2do.goodsdemo;

import android.app.Activity;
import android.os.Bundle;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.can2do.goodsdemo.Goods2;
import com.can2do.goodsdemo.PartnerConfig;
import com.can2do.goodsdemo.GoodsListAdapter;
import com.can2do.goodsdemo.Goods;
import com.can2do.goodsdemo.ResultChecker;
import com.can2do.goodsdemo.Rsa;
import com.can2do.goodsdemo.Goods2;
import com.can2do.goodsdemo.AlixId;
import com.can2do.goodsdemo.BaseHelper;
import com.can2do.goodsdemo.MobileSecurePayer;
import com.can2do.goodsdemo.MobileSecurePayHelper;
import com.can2do.goodsdemo.R;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class Goods2 extends Activity implements OnItemClickListener,
OnItemLongClickListener {
	
	static String TAG = "GoodsDemo";
	// 模拟商户商品列表
	ListView mproductListView = null;
	GoodsListAdapter m_listViewAdapter = null;
	ArrayList<Goods.GoodsDetail> mproductlist;
	
	private ProgressDialog mProgress = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate");
		//
		// check to see if the MobileSecurePay is already installed.
		// 检测安全支付服务是否被安装
		MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(this);
		//mspHelper.detectMobile_sp();
		// 设置界面标题
		setContentView(R.layout.goods2);
		//
		// retrieve and show the product list.
		// 显示商品列表
		initGoodsList();
	}
	/**
	 * retrieve the product list. 设置商品列表
	 */
	void initGoodsList() {
		Goods goods = new Goods();
		this.mproductlist = goods.retrieveGoodsInfo();

		mproductListView = (ListView) findViewById(R.id.GoodsListView);
		m_listViewAdapter = new GoodsListAdapter(this, this.mproductlist);
		mproductListView.setAdapter(m_listViewAdapter);
		mproductListView.setOnItemClickListener(this);
		mproductListView.setOnItemLongClickListener(this);
	}

	/**
	 * get the selected order info for pay. 获取商品订单信息
	 * 
	 * @param position
	 *            商品在列表中的位置
	 * @return
	 */
//	String getOrderInfo(int position) {
//		String strOrderInfo = "partner=" + "\"" + PartnerConfig.PARTNER + "\"";
//		strOrderInfo += "&";
//		strOrderInfo += "seller=" + "\"" + PartnerConfig.SELLER + "\"";
//		strOrderInfo += "&";
//		strOrderInfo += "out_trade_no=" + "\"" + "20120614070627" + "\"";
//		strOrderInfo += "&";
//		strOrderInfo += "subject=" + "\"" + "10"
//				+ "\"";
//		strOrderInfo += "&";
//		strOrderInfo += "body=" + "\"" + "10"+ "\"";
//		strOrderInfo += "&";
//		strOrderInfo += "total_fee=" + "\""
//				+ "12" + "\"";
//		strOrderInfo += "&";
//		strOrderInfo += "notify_url=" + "\""
//				+ "" + "\"";
//
//		return strOrderInfo;
//	}
	String getOrderInfo(int position) {
		String strOrderInfo = "partner=" + "\"" + PartnerConfig.PARTNER + "\"";
		strOrderInfo += "&";
		strOrderInfo += "seller=" + "\"" + PartnerConfig.SELLER + "\"";
		strOrderInfo += "&";
		strOrderInfo += "out_trade_no=" + "\"" + getOutTradeNo() + "\"";
		strOrderInfo += "&";
		strOrderInfo += "subject=" + "\"" + mproductlist.get(position).name
				+ "\"";
		strOrderInfo += "&";
		strOrderInfo += "body=" + "\"" + mproductlist.get(position).body + "\"";
		strOrderInfo += "&";
		strOrderInfo += "total_fee=" + "\""
				+ mproductlist.get(position).price.replace("一口价:", "") + "\"";
		strOrderInfo += "&";
		strOrderInfo += "notify_url=" + "\""
				+ "http://notify.java.jpxx.org/index.jsp" + "\"";
		
		return strOrderInfo;
	}

	/**
	 * get the out_trade_no for an order. 获取外部订单号
	 * 
	 * @return
	 */
	String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
		Date date = new Date();
		String strKey = format.format(date);

		java.util.Random r = new java.util.Random();
		strKey = strKey + r.nextInt();
		strKey = strKey.substring(0, 15);
		return strKey;
	}

	//
	//
	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param signType
	 *            签名方式
	 * @param content
	 *            待签名订单信息
	 * @return
	 */
	String sign(String signType, String content) {
		return Rsa.sign(content, PartnerConfig.RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 * @return
	 */
	String getSignType() {
		String getSignType = "sign_type=" + "\"" + "RSA" + "\"";
		return getSignType;
	}

	/**
	 * get the char set we use. 获取字符集
	 * 
	 * @return
	 */
	String getCharset() {
		String charset = "charset=" + "\"" + "utf-8" + "\"";
		return charset;
	}

	/**
	 * the onItemClick for the list view of the products. 商品列表商品被点击事件
	 */
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//
		// check to see if the MobileSecurePay is already installed.
		// 检测安全支付服务是否安装
		MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(this);
		boolean isMobile_spExist = mspHelper.detectMobile_sp();
		if (!isMobile_spExist)
			return;

		// check some info.
		// 检测配置信息
		if (!checkInfo()) {
			BaseHelper
					.showDialog(
							Goods2.this,
							"提示",
							"缺少partner或者seller，请在src/com/alipay/android/appDemo4/PartnerConfig.java中增加。",
							R.drawable.infoicon);
			return;
		}

		// start pay for this order.
		// 根据订单信息开始进行支付
		try {
			// prepare the order info.
			// 准备订单信息
			String orderInfo = getOrderInfo(position);
			// 这里根据签名方式对订单信息进行签名
			String signType = getSignType();
			String strsign = sign(signType, orderInfo);
			Log.v("sign:", strsign);
			// 对签名进行编码
			strsign = URLEncoder.encode(strsign);
			// 组装好参数
			String info = orderInfo + "&sign=" + "\"" + strsign + "\"" + "&"
					+ getSignType();
			Log.v("orderInfo:", info);
			// start the pay.
			// 调用pay方法进行支付
			MobileSecurePayer msp = new MobileSecurePayer();
			boolean bRet = msp.pay(info, mHandler, AlixId.RQF_PAY, this);

			if (bRet) {
				// show the progress bar to indicate that we have started
				// paying.
				// 显示“正在支付”进度条
				closeProgress();
				mProgress = BaseHelper.showProgress(this, null, "正在支付", false,
						true);
			} else
				;
		} catch (Exception ex) {
			Toast.makeText(Goods2.this, R.string.remote_call_failed,
					Toast.LENGTH_SHORT).show();
		}
	}

	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		return false;
	}

	/**
	 * check some info.the partner,seller etc. 检测配置信息
	 * partnerid商户id，seller收款帐号不能为空
	 * 
	 * @return
	 */
	private boolean checkInfo() {
		String partner = PartnerConfig.PARTNER;
		String seller = PartnerConfig.SELLER;
		if (partner == null || partner.length() <= 0 || seller == null
				|| seller.length() <= 0)
			return false;

		return true;
	}

	//
	// the handler use to receive the pay result.
	// 这里接收支付结果，支付宝手机端同步通知
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				String strRet = (String) msg.obj;

				Log.e(TAG, strRet);	// strRet范例：resultStatus={9000};memo={};result={partner="2088201564809153"&seller="2088201564809153"&out_trade_no="050917083121576"&subject="123456"&body="2010新款NIKE 耐克902第三代板鞋 耐克男女鞋 386201 白红"&total_fee="0.01"&notify_url="http://notify.java.jpxx.org/index.jsp"&success="true"&sign_type="RSA"&sign="d9pdkfy75G997NiPS1yZoYNCmtRbdOP0usZIMmKCCMVqbSG1P44ohvqMYRztrB6ErgEecIiPj9UldV5nSy9CrBVjV54rBGoT6VSUF/ufjJeCSuL510JwaRpHtRPeURS1LXnSrbwtdkDOktXubQKnIMg2W0PreT1mRXDSaeEECzc="}
				switch (msg.what) {
				case AlixId.RQF_PAY: {
					//
					closeProgress();

					BaseHelper.log(TAG, strRet);

					// 处理交易结果
					try {
						// 获取交易状态码，具体状态代码请参看文档
						String tradeStatus = "resultStatus={";
						int imemoStart = strRet.indexOf("resultStatus=");
						imemoStart += tradeStatus.length();
						int imemoEnd = strRet.indexOf("};memo=");
						tradeStatus = strRet.substring(imemoStart, imemoEnd);
						
						//先验签通知
						ResultChecker resultChecker = new ResultChecker(strRet);
						int retVal = resultChecker.checkSign();
						// 验签失败
						if (retVal == ResultChecker.RESULT_CHECK_SIGN_FAILED) {
							BaseHelper.showDialog(
									Goods2.this,
									"提示",
									getResources().getString(
											R.string.check_sign_failed),
									android.R.drawable.ic_dialog_alert);
						} else {// 验签成功。验签成功后再判断交易状态码
							if(tradeStatus.equals("9000"))//判断交易状态码，只有9000表示交易成功
								BaseHelper.showDialog(Goods2.this, "提示","支付成功。交易状态码："+tradeStatus, R.drawable.infoicon);
							else
							BaseHelper.showDialog(Goods2.this, "提示", "支付失败。交易状态码:"
									+ tradeStatus, R.drawable.infoicon);
						}

					} catch (Exception e) {
						e.printStackTrace();
						BaseHelper.showDialog(Goods2.this, "提示", strRet,
								R.drawable.infoicon);
					}
				}
					break;
				}

				super.handleMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	//
	//
	/**
	 * the OnCancelListener for lephone platform. lephone系统使用到的取消dialog监听
	 */
	static class AlixOnCancelListener implements
			DialogInterface.OnCancelListener {
		Activity mcontext;

		AlixOnCancelListener(Activity context) {
			mcontext = context;
		}

		public void onCancel(DialogInterface dialog) {
			mcontext.onKeyDown(KeyEvent.KEYCODE_BACK, null);
		}
	}

	//
	// close the progress bar
	// 关闭进度框
	void closeProgress() {
		try {
			if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回键监听事件
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			BaseHelper.log(TAG, "onKeyDown back");

			this.finish();
			return true;
		}

		return false;
	}

	//
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v(TAG, "onDestroy");

		try {
			mProgress.dismiss();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
