package com.project.sysumobilelibrary.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import me.drakeet.materialdialog.MaterialDialog;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.jauker.widget.BadgeView;
import com.project.sysumobilelibrary.BookDetailActivity;
import com.project.sysumobilelibrary.OrderActivity;
import com.project.sysumobilelibrary.R;
import com.project.sysumobilelibrary.entity.OrderBook;
import com.project.sysumobilelibrary.global.MyApplication;
import com.project.sysumobilelibrary.global.MyURL;
import com.project.sysumobilelibrary.utils.MyAlgorithm;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MyOrderListViewAdapter extends BaseAdapter {
	private static final String TAG = "MyOrderListViewAdapter";
	private Context context;
	private ArrayList<OrderBook> orderBooks;
	private MaterialDialog mMaterialDialog;

	public MyOrderListViewAdapter(Context context,
			ArrayList<OrderBook> orderBooks) {
		this.context = context;
		this.orderBooks = orderBooks;

	}

	@Override
	public int getCount() {
		return orderBooks.size();
	}

	@Override
	public Object getItem(int i) {
		return orderBooks.get(i);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.order_list_item, null);
			viewHolder = new ViewHolder();
			initViewHolder(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final OrderBook orderBook = orderBooks.get(position);

		viewHolder.tvIndex.setText(orderBook.getNo());
		viewHolder.tvOrderState.setText(orderBook.getOrder_state());
		if (!orderBook.getOrder_state().equals("处理中")) {
			viewHolder.tvOrderState.setTextColor(Color.parseColor("#b0120a"));
		} else {
			viewHolder.tvOrderState.setTextColor(Color.parseColor("#000000"));
		}
		viewHolder.tvName.setText(orderBook.getName());
		viewHolder.tvBookNum.setText(orderBook.getBook_num());
		viewHolder.tvBookState.setText(orderBook.getBook_state());
		viewHolder.tvGetPlace.setText(orderBook.getGet_place());
		viewHolder.tvMeetDate.setText(orderBook.getMeet_date());
		viewHolder.tvOrderValid.setText(orderBook.getOrder_valid());
		viewHolder.tvSubLibrary.setText(orderBook.getSub_library());

		if (!orderBook.getMeet_date().equals("0")) {
			viewHolder.badgeView.setVisibility(View.VISIBLE);
			viewHolder.badgeView.setTargetView(viewHolder.tvOrderState);
			viewHolder.badgeView.setTypeface(Typeface.create(
					Typeface.SANS_SERIF, Typeface.ITALIC));
			viewHolder.badgeView.setShadowLayer(2, -1, -1, Color.GREEN);
			viewHolder.badgeView.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
			viewHolder.badgeView.setBackgroundColor(Color.GREEN);
			
			String due_date_str = Pattern.compile("[^0-9]")
					.matcher(orderBook.getOrder_state()).replaceAll("");
			String pat = "yyyyMMdd";
			SimpleDateFormat sdf = new SimpleDateFormat(pat);
			int diff = 1;
			try {
				Date due_date = sdf.parse(due_date_str);
				Date now_date = new Date();// 获取当前时间
				String now_date_str = sdf.format(now_date);
				// Log.e("now_date", now_date_str);
				diff = MyAlgorithm.getGapCount(now_date, due_date);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			viewHolder.badgeView.setBadgeCount(diff);
		} else {
			viewHolder.badgeView.setVisibility(View.GONE);
		}

		viewHolder.tvDelOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				final MaterialDialog alert = new MaterialDialog(context)
						.setTitle("确定删除该预约吗？").setMessage("Yes or No?");
				alert.setPositiveButton("YES", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						alert.dismiss();
						mMaterialDialog = new MaterialDialog(context);
						View view1 = LayoutInflater.from(context).inflate(
								R.layout.progressbar_item, null);
						mMaterialDialog.setView(view1);
						mMaterialDialog.show();
						delOrder(position);
					}

				});
				alert.setNegativeButton("NO", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						alert.dismiss();
					}
				});
				alert.show();
			}
		});
		viewHolder.tvDetail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// getDetail();
				if (orderBook.getMeet_date().equals("0")) {
					Toast.makeText(context, "Sorry, 正在处理中的预约暂无法查看详细图书信息...", Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(context, BookDetailActivity.class);
					intent.putExtra("doc_number", orderBook.getDoc_number());
					context.startActivity(intent);
				}
				
				
			}
		});
		// Log.e(TAG, borrowBooks.size()+"");
		return convertView;
	}

	private void delOrder(final int position) {
		Listener<String> listener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					Log.e(TAG, jsonObject.getString("msg"));
					int code = jsonObject.getInt("code");
					// Log.e(TAG, response);
					if (code == 1) {
						Log.e(TAG, "取消预约成功");
						Toast.makeText(context, "取消预约成功", Toast.LENGTH_SHORT)
								.show();
						// OrderActivity.delOrder();
						int n = Integer.parseInt(MyApplication.getUser()
								.getOrder_num());
						MyApplication.getUser().setOrder_num((n - 1) + "");
						orderBooks.remove(position);
						MyOrderListViewAdapter.this.notifyDataSetChanged();
					} else {
						Log.e(TAG, "取消预约失败");
						Toast.makeText(context,
								"取消预约失败，" + jsonObject.getString("msg"),
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e(TAG, "del order json error");
					Toast.makeText(context, "取消预约失败，好像有bug!!!!",
							Toast.LENGTH_SHORT).show();
				}
				if (mMaterialDialog != null) {
					mMaterialDialog.dismiss();
				}
			}

		};
		ErrorListener errorListener = new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(TAG, "del order error; " + error.toString());
				Toast.makeText(context, "取消预约失败，是不是网络出问题了呢？",
						Toast.LENGTH_SHORT).show();
				if (mMaterialDialog != null) {
					mMaterialDialog.dismiss();
				}
			}
		};
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("token", MyApplication.getUser().getToken());
		map.put("book_num", orderBooks.get(position).getBook_num());
		MyApplication.getMyVolley().addPostStringRequest(MyURL.BOR_HOLD_DELETE,
				listener, errorListener, map, TAG);
	}

	private void initViewHolder(ViewHolder viewHolder, View convertView) {
		viewHolder.tvIndex = (TextView) convertView.findViewById(R.id.tv_index);
		viewHolder.tvOrderState = (TextView) convertView
				.findViewById(R.id.tv_order_state);
		viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
		viewHolder.tvBookNum = (TextView) convertView
				.findViewById(R.id.tv_book_num);
		viewHolder.tvBookState = (TextView) convertView
				.findViewById(R.id.tv_book_state);
		viewHolder.tvGetPlace = (TextView) convertView
				.findViewById(R.id.tv_get_place);
		viewHolder.tvMeetDate = (TextView) convertView
				.findViewById(R.id.tv_meet_date);
		viewHolder.tvOrderValid = (TextView) convertView
				.findViewById(R.id.tv_order_valid);
		viewHolder.tvDelOrder = (TextView) convertView
				.findViewById(R.id.tv_del_order);
		viewHolder.tvDetail = (TextView) convertView
				.findViewById(R.id.tv_detail);
		viewHolder.tvSubLibrary = (TextView) convertView
				.findViewById(R.id.tv_sub_library);
		viewHolder.badgeView = new BadgeView(context);
	}

	class ViewHolder {
		public TextView tvIndex;
		public TextView tvOrderState;
		public TextView tvName;
		public TextView tvBookNum;
		public TextView tvBookState;
		public TextView tvGetPlace;
		public TextView tvMeetDate;
		public TextView tvOrderValid;
		public TextView tvDelOrder;
		public TextView tvDetail;
		public TextView tvSubLibrary;
		public BadgeView badgeView;
	}

}
