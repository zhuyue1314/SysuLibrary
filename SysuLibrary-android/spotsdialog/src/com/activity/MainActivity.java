package com.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import com.example.spotsdialog.R;

import dmax.dialog.SpotsDialog;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);
	}
    /**
     * 加载数据
     */
	public void loadData(View view){
		AlertDialog dialog = new SpotsDialog(this);
		dialog.show();
	}
}
