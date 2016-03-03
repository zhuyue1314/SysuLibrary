package com.project.sysumobilelibrary;

import java.util.ArrayList;
import java.util.HashMap;











import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.project.sysumobilelibrary.adapter.MyBorHistoryListViewAdapter;
import com.project.sysumobilelibrary.entity.BorHistoryBook;
import com.project.sysumobilelibrary.global.MyApplication;
import com.project.sysumobilelibrary.global.MyURL;

import dmax.dialog.SpotsDialog;
import android.app.Activity;
import android.app.AlertDialog;
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

public class BorHistoryActivity extends Activity {
	private static final String TAG = "BorHistoryActivity";
	
	private ImageView ivBack;
	private TextView tvTitle;
	private static AlertDialog loading;
	private TextView tvEmpty;
	private static ListView listView;
	private TextView tvFlush;
	
	private static ArrayList<BorHistoryBook> borHistoryBooks = new ArrayList<BorHistoryBook>();
	private static MyBorHistoryListViewAdapter adapter;
	
	private final static int CODE_TOAST_MSG = 0;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CODE_TOAST_MSG:
				Toast.makeText(BorHistoryActivity.this, msg.obj.toString(),
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
		setContentView(R.layout.activity_bor_history);

		initView();
		initListView();
	}

	private void initView() {
		loading = new SpotsDialog(this);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText("我的历史借阅");
		tvEmpty = (TextView)findViewById(R.id.empty);
		tvFlush = (TextView)findViewById(R.id.tv_flush);
		tvFlush.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				updateBorHistoryBooks();
			}
		});
		tvEmpty.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				updateBorHistoryBooks(); 
			}
		});
		ivBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				BorHistoryActivity.this.finish();
			}
		});
	}

	private void initListView() {
		listView = (ListView)findViewById(R.id.listview);
		listView.setEmptyView(tvEmpty);
		if (adapter==null){
			adapter = new MyBorHistoryListViewAdapter(BorHistoryActivity.this, borHistoryBooks);
		}
		listView.setAdapter(adapter);
		if (borHistoryBooks.size() == 0){
			updateBorHistoryBooks();
		}
	}

	private void updateBorHistoryBooks() {
		loading.show();
		Listener<String> listener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					Log.e(TAG, jsonObject.getString("msg"));
					int code = jsonObject.getInt("code");
					//Log.e(TAG, response);
					if (code == 1){
						updateBorHistoryBooksFromJSONObject(jsonObject);
						Log.e(TAG, "获取历史借阅成功");
						tvEmpty.setText("我在想你第一次借书是十年后呢，还是百年后...？");
					}else{
						Log.e(TAG, "获取历史借阅失败");
						tvEmpty.setText("点击重新加载");
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e(TAG, "get borrow history books json error");
					myToast("是不是网络出问题了呢？");
					tvEmpty.setText("点击重新加载");
				}
				loading.dismiss();
			}
		};
		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(TAG, "get borrow history books error; "+error.toString());
				myToast("是不是网络出问题了呢？");
				tvEmpty.setText("点击重新加载");
				loading.dismiss();
			}
		};
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("token", MyApplication.getUser().getToken());
		MyApplication.getMyVolley().addPostStringRequest(MyURL.GET_MY_BOR_HISTORY_LOAN_URL, listener, errorListener, map, TAG);
	}

	protected void updateBorHistoryBooksFromJSONObject(JSONObject jsonObject) throws JSONException {
		borHistoryBooks.clear();
		JSONArray jsonArray= jsonObject.getJSONArray("loans");
		for (int i=0; i<jsonArray.length(); ++i){
			BorHistoryBook book = new BorHistoryBook();
			book.getFromJSONObject(jsonArray.getJSONObject(i));
			borHistoryBooks.add(book);
		}
		if (adapter!=null){
			adapter.notifyDataSetChanged();
		}
	}
}
