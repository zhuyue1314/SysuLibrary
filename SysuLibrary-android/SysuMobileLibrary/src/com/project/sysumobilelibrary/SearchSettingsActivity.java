package com.project.sysumobilelibrary;

import java.util.ArrayList;

import com.project.sysumobilelibrary.entity.SearchSetting;
import com.weidongjian.meitu.wheelviewdemo.view.LoopView;
import com.weidongjian.meitu.wheelviewdemo.view.OnItemSelectedListener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

public class SearchSettingsActivity extends Activity {

	private RelativeLayout rlOp;
	private RelativeLayout rlNum;
	private RelativeLayout rlFlush;
	private RelativeLayout rlStartYear;
	private RelativeLayout rlEndYear;
	private RelativeLayout rlKeyType;
	private RelativeLayout rlSubLibrary;
    private RelativeLayout.LayoutParams layoutParams;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏  
		setContentView(R.layout.activity_search_settings);
		
		initView();
	}

	private void initView() {
		layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        
        rlOp = (RelativeLayout) findViewById(R.id.rl_op);
        addLoopView(rlOp, SearchSetting.getAllops(), SearchSetting.getAllops().indexOf(SearchFragment.searchSetting.getOp()), new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(int index) {
				SearchFragment.searchSetting.setOp(SearchSetting.getAllops().get(index));
			}
		});
        
        rlNum = (RelativeLayout) findViewById(R.id.rl_num);
        addLoopView(rlNum, SearchSetting.getAllNums(), SearchSetting.getAllNums().indexOf(SearchFragment.searchSetting.getNum()), new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(int index) {
				SearchFragment.searchSetting.setNum(SearchSetting.getAllNums().get(index));
				
			}
		});
        
        rlFlush = (RelativeLayout) findViewById(R.id.rl_flush);
        addLoopView(rlFlush, SearchSetting.getAllflushs(), SearchSetting.getAllflushs().indexOf(SearchFragment.searchSetting.getFlush()), new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(int index) {
				SearchFragment.searchSetting.setFlush(SearchSetting.getAllflushs().get(index));
			}
		});

        rlStartYear = (RelativeLayout) findViewById(R.id.rl_start_year);
        addLoopView(rlStartYear, SearchSetting.getYearList(), SearchSetting.getYearList().indexOf(SearchFragment.searchSetting.getStart_year()), new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(int index) {
				SearchFragment.searchSetting.setStart_year(SearchSetting.getYearList().get(index));
				
			}
		});
        rlEndYear = (RelativeLayout) findViewById(R.id.rl_end_year);
        addLoopView(rlEndYear, SearchSetting.getYearList(), SearchSetting.getYearList().indexOf(SearchFragment.searchSetting.getEnd_year()), new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(int index) {
				SearchFragment.searchSetting.setEnd_year(SearchSetting.getYearList().get(index));
				
			}
		});
        rlKeyType = (RelativeLayout) findViewById(R.id.rl_keytype);
        addLoopView(rlKeyType, SearchSetting.getAllkeytypes(), SearchSetting.getAllkeytypes().indexOf(SearchFragment.searchSetting.getKeytype()), new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(int index) {
				SearchFragment.searchSetting.setKeytype(SearchSetting.getAllkeytypes().get(index));
				
			}
		});
        rlSubLibrary = (RelativeLayout) findViewById(R.id.rl_sub_library);
        addLoopView(rlSubLibrary, SearchSetting.getAllsublibrarys(), SearchSetting.getAllsublibrarys().indexOf(SearchFragment.searchSetting.getSub_library()), new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(int index) {
				SearchFragment.searchSetting.setSub_library(SearchSetting.getAllsublibrarys().get(index));
				
			}
		});
        
		
	}
	
	
	void addLoopView(RelativeLayout rootView, ArrayList<String> list, int pos, OnItemSelectedListener listener){
		LoopView loopView = new LoopView(SearchSettingsActivity.this);
		//设置是否循环播放
        //loopView.setNotLoop();
        //滚动监听
		loopView.setListener(listener);
        //设置原始数据
        loopView.setItems(list);
        //设置初始位置
        loopView.setInitPosition(pos);
        //设置字体大小
        loopView.setTextSize(15);
      
        rootView.addView(loopView, layoutParams);
	}


}
