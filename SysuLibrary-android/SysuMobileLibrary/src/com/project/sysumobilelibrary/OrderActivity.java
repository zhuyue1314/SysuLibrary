package com.project.sysumobilelibrary;

import java.util.ArrayList;
import java.util.HashMap;










import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.project.sysumobilelibrary.adapter.MyOrderListViewAdapter;
import com.project.sysumobilelibrary.entity.OrderBook;
import com.project.sysumobilelibrary.global.MyApplication;
import com.project.sysumobilelibrary.global.MyURL;

import dmax.dialog.SpotsDialog;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class OrderActivity extends Activity {
	private static final String TAG = "OrderActivity";
	private ImageView ivBack;
	private TextView tvTitle;
	private static TextView tvEmpty;
	private static ListView listView;
	private TextView tvFlush;
	
	private static AlertDialog loading;
	private static ArrayList<OrderBook> OrderBooks = new ArrayList<OrderBook>();
	private static MyOrderListViewAdapter adapter=null;
	
	private final static int CODE_TOAST_MSG = 0;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CODE_TOAST_MSG:
				Toast.makeText(OrderActivity.this, msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};

	public void myToast(String msg) {
		handler.obtainMessage(CODE_TOAST_MSG, msg).sendToTarget();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.activity_order);

		initView();
//		initListView();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initListView();
		if (adapter!=null){
			adapter.notifyDataSetChanged();
		}
	}
	private void initView() {
		loading = new SpotsDialog(this);
//		context  = OrderActivity.this;
		ivBack = (ImageView) findViewById(R.id.iv_back);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText("我的预约");
		tvEmpty = (TextView)findViewById(R.id.empty);
		tvFlush = (TextView)findViewById(R.id.tv_flush);
		tvFlush.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				loading.show();
				updateOrderBooks();
			}
		});
		tvEmpty.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				updateOrderBooks();
			}
		});
		ivBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				OrderActivity.this.finish();
			}
		});
	}

	private void initListView() {
		listView = (ListView)findViewById(R.id.listview);
		listView.setEmptyView(tvEmpty);

			adapter = new MyOrderListViewAdapter(OrderActivity.this, OrderBooks);
		
		listView.setAdapter(adapter);
		if (OrderBooks.size()==0){
			loading.show();
			updateOrderBooks();
		}else if (OrderBooks.size() != Integer.parseInt(MyApplication.getUser().getOrder_num())){
			loading.show();
			updateOrderBooks();
		}
	}
//	public void delOrder(){
//		updateOrderBooks();
//		int n = Integer.parseInt(MyApplication.getUser().getOrder_num());
//		MyApplication.getUser().setOrder_num((n-1)+"");
//		
//	}
//	public static void addOrder(){
//		updateOrderBooks();
//		int n = Integer.parseInt(MyApplication.getUser().getOrder_num());
//		MyApplication.getUser().setOrder_num((n+1)+"");
//		
//	}
	private  void updateOrderBooks() {
		Listener<String> listener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					Log.e(TAG, jsonObject.getString("msg"));
					int code = jsonObject.getInt("code");
					//Log.e(TAG, response);
					if (code == 1){
						updateOrderBooksFromJSONObject(jsonObject);
						Log.e(TAG, "获取预约成功");
						tvEmpty.setText("没有预约呐...");
					}else{
						Log.e(TAG, "获取预约失败");
						tvEmpty.setText("点击重新加载");
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e(TAG, "get order books json error");
					myToast("是不是网络出问题了呢？");
					tvEmpty.setText("点击重新加载");
				}
				loading.dismiss();
			}
		};
		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(TAG, "get order books error; "+error.toString());
				myToast("是不是网络出问题了呢？");
				tvEmpty.setText("点击重新加载");
				loading.dismiss();
			}
		};
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("token", MyApplication.getUser().getToken());
		MyApplication.getMyVolley().addPostStringRequest(MyURL.GET_MY_BOR_HOLD_URL, listener, errorListener, map, TAG);
	}

	protected static void updateOrderBooksFromJSONObject(JSONObject jsonObject) throws JSONException {
		OrderBooks.clear();
		JSONArray jsonArray= jsonObject.getJSONArray("loans");
		for (int i=0; i<jsonArray.length(); ++i){
			OrderBook book = new OrderBook();
			book.getFromJSONObject(jsonArray.getJSONObject(i));
			OrderBooks.add(book);
		}
		MyApplication.getUser().setOrder_num(jsonArray.length()+"");
		if (adapter!=null){
			adapter.notifyDataSetChanged();
		}
	}
}
