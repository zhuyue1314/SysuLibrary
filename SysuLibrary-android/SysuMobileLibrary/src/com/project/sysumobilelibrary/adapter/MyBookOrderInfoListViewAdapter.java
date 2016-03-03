package com.project.sysumobilelibrary.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import me.drakeet.materialdialog.MaterialDialog;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.project.sysumobilelibrary.OrderActivity;
import com.project.sysumobilelibrary.R;
import com.project.sysumobilelibrary.entity.OrderInfo;
import com.project.sysumobilelibrary.global.MyApplication;
import com.project.sysumobilelibrary.global.MyURL;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MyBookOrderInfoListViewAdapter extends BaseAdapter {
	private static final String TAG = "MyBookOrderInfoListViewAdapter";
	private Context context;
	private ArrayList<OrderInfo> orderInfos;
	private String doc_number;
	private MaterialDialog mMaterialDialog;

	public MyBookOrderInfoListViewAdapter(Context context,
			ArrayList<OrderInfo> orderInfos, String doc_number) {
		this.context = context;
		this.orderInfos = orderInfos;
		this.doc_number = doc_number;

		mMaterialDialog = new MaterialDialog(context);
		View view1 = LayoutInflater.from(context).inflate(
				R.layout.progressbar_item, null);
		mMaterialDialog.setView(view1);
	}

	@Override
	public int getCount() {
		return orderInfos.size();
	}

	@Override
	public Object getItem(int i) {
		return orderInfos.get(i);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.book_order_info_list_item, null);
			viewHolder = new ViewHolder();
			initViewHolder(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final OrderInfo orderInfo = orderInfos.get(position);

		viewHolder.tvIndex.setText((position + 1) + "");
		viewHolder.tvDueTime.setText(orderInfo.getDue_date() + " - "
				+ orderInfo.getDue_time());
		viewHolder.tvState.setText(orderInfo.getState());
		viewHolder.tvPlace.setText(orderInfo.getPlace());
		viewHolder.tvBookNum.setText(orderInfo.getBook_num());
		viewHolder.tvBarCode.setText(orderInfo.getBar_code());
		viewHolder.tvOrderNum.setText(orderInfo.getOrder_num());
		viewHolder.tvSubLibrary.setText(orderInfo.getSub_library());
		viewHolder.tvDescription.setText(orderInfo.getDescription());

		viewHolder.tvOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!orderInfo.getCan_order()) {
					Toast.makeText(context, "此书不能被预约了...", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				final AlertDialog.Builder builder = new AlertDialog.Builder(
						context, AlertDialog.THEME_HOLO_DARK).setTitle(
						"请选择取书地点：")
						.setSingleChoiceItems(
								(String[]) orderInfo.getPICKUP().toArray(
										new String[0]), 0,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										String pick_up = orderInfo.getPICKUP()
												.get(which);
										orderBook(orderInfo, pick_up);
										mMaterialDialog.show();
										dialog.dismiss();
									}
								});
				final AlertDialog dialog = builder.create();
				// 透明
				Window window = dialog.getWindow();
				WindowManager.LayoutParams lp = window.getAttributes();
				lp.alpha = 0.9f;
				window.setAttributes(lp);
				dialog.show();

			}
		});
		// Log.e(TAG, orderInfos.size()+"");
		return convertView;
	}

	protected void orderBook(final OrderInfo orderInfo, String pick_up) {
		Listener<String> listener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					Log.e(TAG, jsonObject.getString("msg"));
					int code = jsonObject.getInt("code");
					// Log.e(TAG, response);
					if (code == 1) {
						Log.e(TAG, "预约成功");
						orderInfo.setCan_order(false);
						Toast.makeText(context, jsonObject.getString("msg"),
								Toast.LENGTH_SHORT).show();
//						OrderActivity.addOrder();
						int n  = Integer.parseInt(MyApplication.getUser().getOrder_num());
						MyApplication.getUser().setOrder_num((n+1)+"");
					} else {
						Log.e(TAG, "预约失败");
						Toast.makeText(context, jsonObject.getString("msg"),
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e(TAG, "get borrow books json error");
					Toast.makeText(context, "预约失败，好像有bug!!!!",
							Toast.LENGTH_SHORT).show();
				}
				mMaterialDialog.dismiss();
			}

		};
		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(TAG, "get borrow books error; " + error.toString());
				Toast.makeText(context, "预约失败，是不是网络出问题了呢？", Toast.LENGTH_SHORT)
						.show();
				mMaterialDialog.dismiss();
			}
		};
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("token", MyApplication.getUser().getToken());
		map.put("doc_number", doc_number);
		map.put("sub_library", orderInfo.getSub_library());
		map.put("PICKUP", pick_up);
//		Log.e(TAG, map.get("token"));
//		Log.e(TAG, map.get("doc_number"));
//		Log.e(TAG, map.get("sub_library"));
//		Log.e(TAG, map.get("PICKUP"));
		MyApplication.getMyVolley().addPostStringRequest(MyURL.ORDER_BOOK_URL,
				listener, errorListener, map, TAG);
	}

	private void initViewHolder(ViewHolder viewHolder, View convertView) {
		viewHolder.tvIndex = (TextView) convertView.findViewById(R.id.tv_index);
		viewHolder.tvDueTime = (TextView) convertView
				.findViewById(R.id.tv_due_time);
		viewHolder.tvState = (TextView) convertView.findViewById(R.id.tv_state);
		viewHolder.tvPlace = (TextView) convertView.findViewById(R.id.tv_place);
		viewHolder.tvOrder = (TextView) convertView.findViewById(R.id.tv_order);
		viewHolder.tvBookNum = (TextView) convertView
				.findViewById(R.id.tv_book_num);
		viewHolder.tvBarCode = (TextView) convertView
				.findViewById(R.id.tv_bar_code);
		viewHolder.tvOrderNum = (TextView) convertView
				.findViewById(R.id.tv_order_num);
		viewHolder.tvSubLibrary = (TextView) convertView
				.findViewById(R.id.tv_sub_library);
		viewHolder.tvDescription = (TextView) convertView
				.findViewById(R.id.tv_description);

	}

	class ViewHolder {
		public TextView tvIndex;
		public TextView tvState;
		public TextView tvOrder;
		public TextView tvDueTime;
		public TextView tvPlace;
		public TextView tvBookNum;
		public TextView tvBarCode;
		public TextView tvOrderNum;
		public TextView tvSubLibrary;
		public TextView tvDescription;

	}

}
