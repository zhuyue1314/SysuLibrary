package com.kanyuan;

import android.app.Activity;
import android.os.Bundle;

import com.kanyuan.monindicator.MonIndicator;
import com.kanyuan.monindicator.R;

public class MainActivity extends Activity {
    private MonIndicator monIndicator;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);
		
		monIndicator = (MonIndicator)this.findViewById(R.id.monIndicator);
		monIndicator.setColors(new int[]{0xFF942909, 0xFF577B8C, 0xFF201289, 0xFF000000, 0xFF38B549});
	}

}
