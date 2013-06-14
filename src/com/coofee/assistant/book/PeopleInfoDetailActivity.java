package com.coofee.assistant.book;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coofee.assistant.R;
import com.coofee.assistant.book.model.People;
import com.coofee.assistant.util.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
/**
 * 显示用户信息
 * 
 * @author zhao
 */
public class PeopleInfoDetailActivity extends Activity {

	private static final String TAG = "PeopleInfoDetailActivity";
	public static final String AUTHOR = "author";

	private People mAuthor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_info_detail_activity);
		check();
		displayPeopleInfo();
	}

	/**
	 * 检测传递过来的数据是否正确
	 */
	private void check() {
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			finish();
		}

		mAuthor = (People) extras.get(AUTHOR);
		Log.d("Intent value Author", mAuthor.toString());
		if (mAuthor == null) {
			finish();
		}

		Log.d(TAG, mAuthor.toString());
	}

	private void displayPeopleInfo() {
		displayPeopleIcon(mAuthor.getIconUrl());
		requestPeopleInformation(mAuthor.getApiUri());
	}

	/**
	 * 更新界面显示的数据
	 */
	private void updateUi() {
		((TextView) findViewById(R.id.people_info_detail_author)).setText(mAuthor.getTitle());
		((TextView) findViewById(R.id.people_info_detail_uid)).setText(mAuthor.getUid());
		((TextView) findViewById(R.id.people_info_detail_location)).setText(mAuthor.getLocation());
		((TextView) findViewById(R.id.people_info_detail_signature)).setText(mAuthor.getSignature());
		((TextView) findViewById(R.id.people_info_detail_content)).setText(mAuthor.getContent());
		((TextView) findViewById(R.id.people_info_detail_homePage)).setText(mAuthor.getHomePageUrl());
		Log.d(TAG, "update people information successed.");
	}
	
	private void displayPeopleIcon(String peopleIconUrl) {
		ImageView peopleIcon = (ImageView) findViewById(R.id.people_info_detail_peopleIcon);
		ImageLoaderUtil.displayPeopleIcon(peopleIcon, peopleIconUrl);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		ImageLoader.getInstance().stop();
		super.onDestroy();
	}
	
	private ProgressDialog mProgressDialog;

	private static final int REQUEST_SUCCESS = 0x0001;
	private static final int REQUEST_FAILED = 0x0002;

	private void requestPeopleInformation(final String peopleApiUri) {
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage("正在获取用户信息...");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCancelable(true);
		mProgressDialog.show();

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Log.d(TAG, "peopleApiUri=" + peopleApiUri);
					People people = getPeopleInfo(peopleApiUri);
					if (people != null) {
						Log.d(TAG, people.toString());
						mAuthor = people;
						peopleHandler.sendEmptyMessage(REQUEST_SUCCESS);
					} else {
						peopleHandler.sendEmptyMessage(REQUEST_FAILED);
					}
				} catch (Exception e) {
					e.printStackTrace();
					peopleHandler.sendEmptyMessage(REQUEST_FAILED);
				}
			}
		}).start();
	}

	/**
	 * 获取用户信息
	 */
	public static People getPeopleInfo(String userUri) {
		return BookHelper.getPeopleInfo(userUri);
	}

	private PeopleHandler peopleHandler = new PeopleHandler(this);
	private static class PeopleHandler extends Handler {

		private WeakReference<PeopleInfoDetailActivity> peopleRef;

		public PeopleHandler(PeopleInfoDetailActivity peopleActivity) {
			peopleRef = new WeakReference<PeopleInfoDetailActivity>(peopleActivity);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			PeopleInfoDetailActivity peopleActivity = peopleRef.get();
			peopleActivity.mProgressDialog.dismiss();
			switch (msg.what) {
			case REQUEST_SUCCESS:
				Log.d("Test", "Request Successed!");
				peopleActivity.updateUi();
				break;
			case REQUEST_FAILED:
				Log.d("Test", "Request Failed!");
				Toast.makeText(peopleActivity, "获取用户信息失败!", Toast.LENGTH_SHORT)
						.show();
				peopleActivity.finish();
				break;
			}
			super.handleMessage(msg);
		}
	}

}
