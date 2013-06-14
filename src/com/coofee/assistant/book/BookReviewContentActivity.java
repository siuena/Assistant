package com.coofee.assistant.book;

import java.lang.ref.WeakReference;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.coofee.assistant.R;
import com.coofee.assistant.book.model.People;
import com.coofee.assistant.book.model.Rate;
import com.coofee.assistant.book.model.Review;
import com.coofee.assistant.util.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 从评论列表界面跳转过来, 需要传递一个Review对象.
 * 
 * @author zhao
 */
@SuppressLint("SetJavaScriptEnabled")
public class BookReviewContentActivity extends Activity {

	private static final int REQUEST_SUCCESS = 0x0001;
	private static final int REQUEST_FAILED = 0x0002;
	
	public static final String REVIEW = "review";
	public static final String TAG = BookReviewContentActivity.class.getCanonicalName();
	private Review mReview;
	
	/**
	 * 保存获取的评论正文
	 */
	private String mReviewContent;
	/**
	 * 显示获取的评论正文
	 */
	private WebView mContentWebView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_review_content_activity);

		 check();
		 init();
//		test();
	}

	/**
	 * 用于测试
	 */
//	private void test() {
//		displayPeopleIcon("http://img3.douban.com/icon/u4539940-7.jpg");
//		displayReviewContent("http://www.douban.com/review/2936844/");
//	}

	/**
	 * 检测传递来的数据是否正确, 如果不正确则关闭当前页面.
	 */
	private void check() {
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			finish();
		}

		mReview = (Review) extras.get(REVIEW);
		Log.d("Intent value Review", mReview.toString());
		if (mReview == null) {
			finish();
		}
	}

	/**
	 * 显示从评论列表界面传递过来的评论.
	 */
	private void init() {
		((TextView) findViewById(R.id.book_review_content_title))
				.setText(mReview.getTitle());
		((TextView) findViewById(R.id.book_review_content_author))
				.setText(mReview.getAuthor().getTitle());
		((TextView) findViewById(R.id.book_review_content_pubDate))
				.setText(mReview.getPublishDate());
		((TextView) findViewById(R.id.book_review_content_votes))
				.setText("" + mReview.getVotes());
		((TextView) findViewById(R.id.book_review_content_useless))
				.setText("" + mReview.getUseless());

		// 设置星星评级.
		RatingBar rating = (RatingBar) findViewById(R.id.book_review_content_rating);
		Rate rate = mReview.getRating();
		if (rate != null) {
			rating.setMax(rate.getMax());
			rating.setProgress(rate.getValue());
			((TextView) findViewById(R.id.book_review_content_ratingScore))
			.setText(String.valueOf(rate.getValue()));
		}
		displayPeopleIcon(mReview.getAuthor().getIconUrl());
		displayReviewContent(mReview.getUrl());
	}

	/**
	 * 显示用户头像, 同时添加点击用户头像时显示用户信息的事件.
	 */
	private void displayPeopleIcon(final String peopleIconUrl) {
		ImageView peopleIcon = (ImageView) findViewById(R.id.book_review_content_peopleIcon);
		ImageLoaderUtil.displayPeopleIcon(peopleIcon, peopleIconUrl);
		// 点击用户头像时, 跳转到用户信息界面.
		peopleIcon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				People author = mReview.getAuthor();
//				People author = new People();
//				author.setApiUri("http://api.douban.com/people/4539940");
//				author.setIconUrl(peopleIconUrl);
				Intent intent = new Intent(BookReviewContentActivity.this,
						PeopleInfoDetailActivity.class);
				intent.putExtra(PeopleInfoDetailActivity.AUTHOR, author);
				startActivity(intent);
			}
		});
	}

	/**
	 * 显示评论正文
	 */
	private void displayReviewContent(final String reviewContentUrl) {
		mContentWebView = (WebView) findViewById(R.id.book_review_content_content);
		WebSettings webSettings = mContentWebView.getSettings();
		// 设置支持javascript
		webSettings.setJavaScriptEnabled(true);
		// 设置支持缩放
		webSettings.setBuiltInZoomControls(true);
		// 设置WebView为全透明
		mContentWebView.setBackgroundColor(0);
//		mContentWebView.setBackgroundColor(Color.parseColor("#DCDCDC"));
		mContentWebView.setWebChromeClient(new WebChromeClient());
		// 设置以内嵌方式打开网页,加载页面时显示进度框.
		mContentWebView.setWebViewClient(new WebViewClient() {
			private ProgressDialog progressDialog = null;

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				progressDialog = new ProgressDialog(
						BookReviewContentActivity.this);
				progressDialog.setIndeterminate(true);
				progressDialog.setCancelable(true);
				progressDialog.setMessage("加载中，请稍候...");
				progressDialog.show();
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				progressDialog.dismiss();
				super.onPageFinished(view, url);
			}

		});

		requestReviewContent(reviewContentUrl);
	}

	private ProgressDialog mProgressDialog;
	/**
	 * 从网页抓取评论正文
	 */
	private void requestReviewContent(final String reviewContentUrl) {
		mProgressDialog = new ProgressDialog(BookReviewContentActivity.this);
		mProgressDialog.setMessage("正在获取评论全文...");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCancelable(true);
		mProgressDialog.show();
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					mReviewContent = BookHelper.getBookReviewContent(reviewContentUrl);
					if (mReviewContent != null) {
						Log.d("requestReviewContent", mReviewContent);
						mReviewContentHandelr.sendEmptyMessage(REQUEST_SUCCESS);
					} else {
						mReviewContentHandelr.sendEmptyMessage(REQUEST_FAILED);
					}
				}  catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					mReviewContentHandelr.sendEmptyMessage(REQUEST_FAILED);
				}
			}
		}).start();
	}
	
	private ReviewContentHandler mReviewContentHandelr = new ReviewContentHandler(this);
	private static class ReviewContentHandler extends Handler {
		private WeakReference<BookReviewContentActivity> reviewContentRef;
		
		public ReviewContentHandler(BookReviewContentActivity reviewContentActivity) {
			reviewContentRef = new WeakReference<BookReviewContentActivity>(reviewContentActivity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			BookReviewContentActivity activity = reviewContentRef.get();
			activity.mProgressDialog.dismiss();
			switch (msg.what) {
			case REQUEST_SUCCESS:
				Log.d("ReviewContentHandler", "Request Successed!");
				activity.mContentWebView.loadDataWithBaseURL(null, activity.mReviewContent, "text/html", "UTF-8", null);
				break;
			case REQUEST_FAILED:
				Log.d("ReviewContentHandler", "Request Failed!");
				Toast.makeText(activity, "获取评论全文失败!", Toast.LENGTH_SHORT).show();
				break;
			}
			super.handleMessage(msg);
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		ImageLoader.getInstance().stop();
		super.onDestroy();
	}
}