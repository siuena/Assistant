package com.coofee.assistant.bus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.coofee.assistant.R;

public class BusTransferDetailActivity extends Activity {
	private TextView mTitle, mSolutionDesc;
	private BusTransferPlan mPlan;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bus_line_detail_activity);
		
		initViews();
		getIntentData();
	}
	
	private void initViews() {
		mTitle = (TextView) findViewById(R.id.bus_line_detail_title);
		mSolutionDesc = (TextView) findViewById(R.id.bus_line_detail_info);
		findViewById(R.id.bus_line_detail_stations).setVisibility(View.GONE);
	}
	
	private void getIntentData() {
		Intent data = getIntent();
		if (data != null) {
			String busPlanJson = data.getStringExtra(Bus.EXTRA_BUS_TRANSFER_JSON);

			if (TextUtils.isEmpty(busPlanJson)) {
				BusTransferDetailActivity.this.finish();
			} else {
				try {
					mPlan = JSON.parseObject(busPlanJson, BusTransferPlan.class);
					mTitle.setText("换乘方案");
					mSolutionDesc.setText(mPlan.solutionDesc());
				} catch (Exception e) {
					e.printStackTrace();
					BusTransferDetailActivity.this.finish();
				}
			}
		}
	}
}
