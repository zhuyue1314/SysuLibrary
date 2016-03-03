package com.project.sysumobilelibrary.adapter;

import java.util.ArrayList;
import com.project.sysumobilelibrary.BookDetailActivity;
import com.project.sysumobilelibrary.R;
import com.project.sysumobilelibrary.entity.BorHistoryBook;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyBorHistoryListViewAdapter extends BaseAdapter{
	private static final String TAG = "MyBorHistoryListViewAdapter";
	private Context context;
	private ArrayList<BorHistoryBook> borHistoryBooks;

	public MyBorHistoryListViewAdapter(Context context,
			ArrayList<BorHistoryBook> borHistoryBooks) {
		this.context = context;
		this.borHistoryBooks = borHistoryBooks;
	}

	@Override
	public int getCount() {
		return borHistoryBooks.size();
	}

	@Override
	public Object getItem(int i) {
		return borHistoryBooks.get(i);
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
					R.layout.bor_history_list_item, null);
			viewHolder = new ViewHolder();
			initViewHolder(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final BorHistoryBook borHistoryBook = borHistoryBooks.get(position);
		
		viewHolder.tvIndex.setText(borHistoryBook.getNo());
		viewHolder.tvDueTime.setText(borHistoryBook.getDue_date()+" - "+borHistoryBook.getDue_time());
		viewHolder.tvName.setText(borHistoryBook.getName());
		viewHolder.tvRetTime.setText(borHistoryBook.getRet_date()+" - "+borHistoryBook.getRet_time());
		
		viewHolder.tvDetail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//getDetail();
				Intent intent = new Intent(context, BookDetailActivity.class);
				intent.putExtra("doc_number", borHistoryBook.getDoc_number());
				context.startActivity(intent);
			}
		});
		//Log.e(TAG, borrowBooks.size()+"");
		return convertView;
	}

	

	private void initViewHolder(ViewHolder viewHolder, View convertView) {
		viewHolder.tvIndex = (TextView) convertView
				.findViewById(R.id.tv_index);
		viewHolder.tvDueTime = (TextView) convertView
				.findViewById(R.id.tv_due_time);
		viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
		viewHolder.tvRetTime = (TextView) convertView
				.findViewById(R.id.tv_ret_time);
		viewHolder.tvDetail = (TextView) convertView
				.findViewById(R.id.tv_detail);

	}

	class ViewHolder {
		public TextView tvIndex;
		public TextView tvDetail;
		public TextView tvName;
		public TextView tvDueTime;
		public TextView tvRetTime;
	}

}
