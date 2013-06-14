package com.coofee.assistant.book;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.coofee.assistant.R;
import com.coofee.assistant.book.model.Book;
import com.coofee.assistant.book.model.Rating;
import com.coofee.assistant.book.model.Review;
import com.coofee.assistant.book.model.Reviews;
import com.coofee.assistant.provider.AssistantSQLiteHelper;
import com.coofee.assistant.util.ImageLoaderUtil;
import com.coofee.assistant.util.StringUtil;
import com.coofee.assistant.util.UiUtil;

public class BookSearchResultActivity extends Activity {

	// 以下是handler处理消息时的消息ID.
	private static final int REQUEST_BOOK_BASIC_INFO_SUCCESSED = 0x0001;
	private static final int REQUEST_BOOK_BASIC_INFO_FAILED = 0x0002;
	private static final int REQUEST_BOOK_PRICES_SUCCESSED = 0x0003;
	private static final int REQUEST_BOOK_PRICES_FAILED = 0x0004;
	private static final int REQUEST_BOOK_REVIEWS_SUCCESSED = 0x0005;
	private static final int REQUEST_BOOK_REVIEWS_FAILED = 0x0006;
	
	private final int BOOK_BASIC_INFO_PAGE_INDEX = 0;
	private final int BOOK_PRICES_PAGE_INDEX = 1;
	private final int BOOK_REVIEWS_PAGE_INDEX = 2;
	
	private ViewPager mViewPager;
	// 页头, 当点击下页头之后, 发起请求获取数据.
	private TextView mBookBasicInfoHeader, mBookPricesHeader, mBookReviewsHeader;
	private LinearLayout mBookBasicInfoView, mBookPricesView, mBookReviewsView;
	// 动画游标
	private ImageView mCursor;	
	// 动画游标偏移量
	private int mCursorOffset = 0;
	// 当前页卡编号
	private int mCurrentPageIndex = 0;
	// 动画游标宽度
	private int mBmpWidth = 0;
	// 用来显示进度对话框
	private ProgressDialog mProgressDialog;
	
	
	private String mIsbn;
	// 用来保存获取的书籍基本信息, 当mBook不为null时表示书籍基本信息请求成功.
	private Book mBook = null; 
	// 用来保存获取的书籍价格和网址信息, 下标从0开始, 存放的价格依次是当当, 亚马逊, 99书城.
	private String[][] mPriceUrls = null;
	// 用来保存获取的评论信息
	private Reviews mReviews = null;
	private BookReviewItemAdapter mReviewsAdapter = null;
	// 用来保存上一次请求评论信息成功时， 起始评论的索引值, 默认从1开始.
	private int mPreStartIndex = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_search_result_activity);
		
		check();
		
		initTabHeader();
		// initCursor方法必须放在initViewPager方法之前， 
		// 因为在initCursor方法中设置mCursorOffset和mBmpWidth的值， 
		// 在initViewPager中会使用到。
		initCursor();
		initViewPager();
		
		// 进入页面之后请求手机基本信息
		requestBookBasicInfoByIsbn();
	}
	
	/**
	 * 检测条件是否满足; 如果不满足条件, 则直接关闭页面.
	 */
	private void check() {
		mIsbn = getIntent().getStringExtra(SearchBookByIsbnActivity.ISBN);
		if (TextUtils.isEmpty(mIsbn)) {
			finish();
		}
	}
	
	/**
	 * 初始化页卡头
	 */
	private void initTabHeader() {
		mBookBasicInfoHeader = (TextView) findViewById(R.id.book_search_result_tab_bookBasicInfo);
		mBookPricesHeader = (TextView) findViewById(R.id.book_search_result_tab_bookPrices);
		mBookReviewsHeader = (TextView) findViewById(R.id.book_search_result_tab_bookReviews);
		
		mBookBasicInfoHeader.setOnClickListener(new TabHeaderOnClickListener(BOOK_BASIC_INFO_PAGE_INDEX));
		mBookPricesHeader.setOnClickListener(new TabHeaderOnClickListener(BOOK_PRICES_PAGE_INDEX));
		mBookReviewsHeader.setOnClickListener(new TabHeaderOnClickListener(BOOK_REVIEWS_PAGE_INDEX));
	}
	
	private void initViewPager() {
		mViewPager = (ViewPager) findViewById(R.id.book_search_result_viewPager);
		List<View> pageViews = new ArrayList<View>(); 
		LayoutInflater inflater = this.getLayoutInflater();
		mBookBasicInfoView = (LinearLayout) inflater.inflate(R.layout.book_basic_info_activity, null);
		mBookPricesView = (LinearLayout) inflater.inflate(R.layout.book_prices_activity, null);
		mBookReviewsView = (LinearLayout) inflater.inflate(R.layout.book_reviews_activity, null);
		pageViews.add(mBookBasicInfoView);
		pageViews.add(mBookPricesView);
		pageViews.add(mBookReviewsView);
		mViewPager.setAdapter(new TabPageAdapter(pageViews));
		mViewPager.setCurrentItem(BOOK_BASIC_INFO_PAGE_INDEX);
		mViewPager.setOnPageChangeListener(new TabPageChangeListener());
	}
	
	/**
	 * 初始化页面游标
	 */
	private void initCursor() {
		mCursor = (ImageView) findViewById(R.id.book_search_result_cursor);
		// 获取图片宽度
		mBmpWidth = BitmapFactory.decodeResource(getResources(), R.drawable.cursor).getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		// 获取分辨率宽度
		int screenWidth = dm.widthPixels;
		// 计算偏移量
		mCursorOffset = (screenWidth / 3 - mBmpWidth) / 2;
		Log.d("screenWidth", "screenWidth=" + screenWidth 
				+ "; mCursorOffset=" + mCursorOffset);
		Matrix matrix = new Matrix();
		matrix.postTranslate(mCursorOffset, 0);
		mCursor.setImageMatrix(matrix);
	}
	
	/**
	 * 通过ISBN获取书籍的基本信息
	 */
	private void requestBookBasicInfoByIsbn() {
		// 首先通过ISBN从数据库获取书籍的基本信息; 如果在本地数据库获取不到的话, 则通过网络进行请求.
		mBook = AssistantSQLiteHelper.getBookBasicInfoByIsbn(BookSearchResultActivity.this, mIsbn);
		if (mBook != null) {
			onRequestBookBasicInfoOk();
			return;
		}
		
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setMessage("正在获取书籍基本信息...");
		mProgressDialog.setCancelable(true);
		mProgressDialog.show();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String jsonBookBasicInfo = BookHelper.getBookInfoByIsbn(mIsbn);
					if (!TextUtils.isEmpty(jsonBookBasicInfo)) {
						Log.d("Request Book Basic information", jsonBookBasicInfo);
						mBook = JSON.parseObject(jsonBookBasicInfo, Book.class);
						if (mBook != null)
							mBookHander.sendEmptyMessage(REQUEST_BOOK_BASIC_INFO_SUCCESSED);
						else
							mBookHander.sendEmptyMessage(REQUEST_BOOK_BASIC_INFO_FAILED);
					} else {
						mBookHander.sendEmptyMessage(REQUEST_BOOK_BASIC_INFO_FAILED);
					}
				} catch (Exception e) {
					mBookHander.sendEmptyMessage(REQUEST_BOOK_BASIC_INFO_FAILED);
				}
			}
		}).start();
	}
	
	/**
	 * 获取书籍价格
	 */
	private void requestBookPrices() {
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setMessage("正在获取书籍价格信息...");
		mProgressDialog.setCancelable(true);
		mProgressDialog.show();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String[][] priceUrls = BookHelper.getBookPricesByIsbn(mIsbn);
					if (priceUrls != null) {
						StringUtil.println(priceUrls);
						mPriceUrls = priceUrls;
						mBookHander.sendEmptyMessage(REQUEST_BOOK_PRICES_SUCCESSED);
					} else {
						mBookHander.sendEmptyMessage(REQUEST_BOOK_PRICES_FAILED);
					}
				} catch (Exception e) {
					mBookHander.sendEmptyMessage(REQUEST_BOOK_PRICES_FAILED);
				}
			}
		}).start();
	}
	
	/**
	 * 获取书籍评论信息
	 */
	private void requestBookReviews(final int startIndex) {
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setMessage("正在获取书籍评论信息...");
		mProgressDialog.setCancelable(true);
		mProgressDialog.show();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Reviews reviews = BookHelper.getBookReviewsByIsbn(mIsbn, startIndex);
					if (reviews != null) {
						Log.d("Request Reviews", reviews.toString());
						mReviews = reviews;
						mBookHander.sendEmptyMessage(REQUEST_BOOK_REVIEWS_SUCCESSED);
					} else {
						mBookHander.sendEmptyMessage(REQUEST_BOOK_REVIEWS_FAILED);
					}
				} catch (Exception e) {
					mBookHander.sendEmptyMessage(REQUEST_BOOK_REVIEWS_FAILED);
				}
			}
		}).start();
	}
	
	/**
	 * 当请求书籍基本信息成功后, 使用该方法设置页面显示书籍基本信息.
	 */
	private void onRequestBookBasicInfoOk() {
		Log.d("onRequestBookBasicInfoOk", mBook.toShortString());
		((TextView) mBookBasicInfoView.findViewById(R.id.book_basic_info_bookName)).setText(mBook.getTitle());
		((TextView) mBookBasicInfoView.findViewById(R.id.book_basic_info_author)).setText(mBook.getAuthors());
		((TextView) mBookBasicInfoView.findViewById(R.id.book_basic_info_price)).setText(mBook.getPrice());
		((TextView) mBookBasicInfoView.findViewById(R.id.book_basic_info_pages)).setText("" + mBook.getPages());
		((TextView) mBookBasicInfoView.findViewById(R.id.book_basic_info_binding)).setText(mBook.getBinding());
		((TextView) mBookBasicInfoView.findViewById(R.id.book_basic_info_isbn)).setText(mBook.getIsbn());
		((TextView) mBookBasicInfoView.findViewById(R.id.book_basic_info_publisher)).setText(mBook.getPublisher());
		((TextView) mBookBasicInfoView.findViewById(R.id.book_basic_info_pubDate)).setText(mBook.getPubdate());
		((TextView) mBookBasicInfoView.findViewById(R.id.book_basic_info_bookSummary)).setText(mBook.getSummary());
		((TextView) mBookBasicInfoView.findViewById(R.id.book_basic_info_authorSummary)).setText(mBook.getAuthor_intro());
		
		// 显示书籍评价
		Rating rating = mBook.getRating();
		if (rating != null) {
			mBookBasicInfoView.findViewById(R.id.book_basic_info_rating_layout).setVisibility(View.VISIBLE);
			RatingBar ratingBar = (RatingBar) mBookBasicInfoView.findViewById(R.id.book_basic_info_rating);
			// 为了显示小数, 所以需要都乘以10;
			ratingBar.setMax(rating.getMax() * 10);
			int average = (int) (rating.getAverage() * 10);
			ratingBar.setProgress(average);
			TextView ratingScore = (TextView) mBookBasicInfoView.findViewById(R.id.book_basic_info_ratingScore);
			ratingScore.setText(String.valueOf(rating.getAverage()));
			TextView numRaters = (TextView) mBookBasicInfoView.findViewById(R.id.book_basic_info_numRaters);
			numRaters.setText(String.valueOf(rating.getNumRaters()));
		}
		
		// 显示书籍封面
		ImageView bookCover = (ImageView) mBookBasicInfoView.findViewById(R.id.book_basic_info_imageview_bookCover);
		ImageLoaderUtil.displayBookCover(bookCover, mBook.getImage());
		mBook.getOrigin_title();
		if (!TextUtils.isEmpty(mBook.getOrigin_title())) {
			mBookBasicInfoView.findViewById(R.id.book_basic_info_tableRow_bookOriginName).setVisibility(View.VISIBLE);
			((TextView) mBookBasicInfoView.findViewById(R.id.book_basic_info_bookOriginName)).setText(mBook.getOrigin_title());
		}
		// 显示译者
		if (!TextUtils.isEmpty(mBook.getTranslators())) {
			mBookBasicInfoView.findViewById(R.id.book_basic_info_tableRow_translator).setVisibility(View.VISIBLE);
			((TextView) mBookBasicInfoView.findViewById(R.id.book_basic_info_translator)).setText(mBook.getTranslators());
		}
	}
	
	/**
	 * 当请求书籍价格成功时, 使用该方法设置书籍价格.
	 */
	private void onRequestBookPricesOk() {
		Log.d("onRequestBookPricesOk", "request book price ok.");
		if (mBook != null)
			((TextView) mBookPricesView.findViewById(R.id.book_prices_originPrice)).setText(mBook.getPrice());
		
		TextView dangdang = (TextView) mBookPricesView.findViewById(R.id.book_prices_dangdang);
		dangdang.setText(getPriceText(mPriceUrls[0][0]));
		dangdang.setOnClickListener(new OpenUrlClick(mPriceUrls[0][1]));
		TextView amazon = (TextView) mBookPricesView.findViewById(R.id.book_prices_amazon);
		amazon.setText(getPriceText(mPriceUrls[1][0]));
		amazon.setOnClickListener(new OpenUrlClick(mPriceUrls[1][1]));
		
		TextView shucheng = (TextView) mBookPricesView.findViewById(R.id.book_prices_99Shucheng);
		shucheng.setText(getPriceText(mPriceUrls[2][0]));
		shucheng.setOnClickListener(new OpenUrlClick(mPriceUrls[2][1]));
		
		// 将最低价格的文字颜色设置为红色.
		int minPriceIndex = getMinPriceIndex();
		switch (minPriceIndex) {
		case 0:
			dangdang.setTextColor(Color.RED);
			break;
		case 1:
			amazon.setTextColor(Color.RED);
			break;
		case 2:
			shucheng.setTextColor(Color.RED);
			break;
		default:
			Log.d("onRequestBookPricesOk", "未能获取最小价格的索引.");
		}
	}
	
	/**
	 * 当请求书籍评论成功时, 使用该方法设置书籍评论.
	 */
	private void onRequestBookReviewsOk() {
		Log.d("onRequestBookReviewsOk", "startIndex=" 
				+ mReviews.getStartIndex()  
				+ "; totalResults=" + mReviews.getTotalResults()
				+ "; itemsPerPage=" + mReviews.getItemsPerPage());
		
		mPreStartIndex = mReviews.getStartIndex();
		ListView reviewList = (ListView) mBookReviewsView.findViewById(R.id.book_reviews_reviewList);
		if (mReviewsAdapter == null) {
			mReviewsAdapter = new BookReviewItemAdapter(this, mReviews.getReviewList());
			reviewList.setAdapter(mReviewsAdapter);
		} else {
			mReviewsAdapter.setReviewList(mReviews.getReviewList());
		}
	}
	
	/**
	 * 获取书籍价格最低的索引值.
	 * -1: 不可知
	 * 0: 当当;
	 * 1: 亚马逊;
	 * 2: 99书城
	 */
	private int getMinPriceIndex() {
		if (mPriceUrls != null) {
			int minPriceIndex = -1;
			float minPrice = Float.MAX_VALUE;
			for (int i = 0; i < mPriceUrls.length; i++) {
				if (!TextUtils.isEmpty(mPriceUrls[i][0]) && mPriceUrls[i][0].length() >= 2) {
					// 为了去除"￥"符号。
					String strPrice = mPriceUrls[i][0].substring(1);
					Log.d("getMinPriceIndex", "strPrice=" + strPrice);
					try {
						float price = Float.parseFloat(strPrice);
						Log.d("getMinPriceIndex", "price=" + price + "; minPrice=" + minPrice);
						if (price < minPrice) {
							minPrice = price;
							minPriceIndex = i;
							Log.d("getMinPriceIndex", "minPrice=" + minPrice + "; minPriceIndex=" + minPriceIndex);
						}
					} catch (NumberFormatException e) {
						Log.d("NumberFormatException", "不能转换为数字.");
					}
				}
			}
			
			Log.d("getMinPriceIndex", "minPriceIndex=" + minPriceIndex);
			return minPriceIndex;
		}
		return -1;
	}
	
	/**
	 * 获取价格文本如何显示.
	 */
	private String getPriceText(String price) {
		if (TextUtils.isEmpty(price)) 
			return "";
		else
			return price;
	}
	
	/**
	 * 用于点击价格TextView时, 跳转到浏览器.
	 * @author zhao
	 *
	 */
	private class OpenUrlClick implements View.OnClickListener {
		private String _url;
		
		public OpenUrlClick(String url) {
			_url = url;
		}
		
		@Override
		public void onClick(View v) {
			if (!TextUtils.isEmpty(_url)) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(_url));
				startActivity(intent);
			}
		}
	}
	
	/**
	 * 获取书籍评论的第一页
	 */
	private void requestBookReviewFirstPage() {
		Log.d("start", "startIndex=" + mPreStartIndex);
		if (mPreStartIndex == 1) {
			UiUtil.toastShort(this, "已经是第一页了。");
			return;
		}
		
		// 获取第一页中的评论.
		final int firstPageIndex = 1;
		requestBookReviews(firstPageIndex);
	}
	
	/**
	 * 获取后一页的书籍评论
	 */
	private void requestBookReviewNextPage() {
		Log.d("next", "startIndex=" + mPreStartIndex);
		
		int tempStartIndex = mPreStartIndex;
		if ((tempStartIndex + mReviews.getItemsPerPage()) < mReviews.getTotalResults()) {
			tempStartIndex += mReviews.getItemsPerPage();
			Log.d("next", "tempStartIndex=" + tempStartIndex);
			requestBookReviews(tempStartIndex);
		} else {
			UiUtil.toastShort(this, "已经是最后一页了。");
		}
	}
	
	/**
	 * 获取前一页的书籍评论
	 */
	private void requestBookReviewPreviousPage() {
		Log.d("pre", "startIndex=" + mPreStartIndex);
		if (mPreStartIndex == 1) {
			UiUtil.toastShort(this, "已经是第一页了。");
			return;
		}
		
		int tempStartIndex = mPreStartIndex;
		if (tempStartIndex > mReviews.getItemsPerPage()) {
			tempStartIndex -= mReviews.getItemsPerPage();
		} else {
			tempStartIndex = 1;
		}
		
		Log.d("pre", "tempStartIndex=" + tempStartIndex);
		requestBookReviews(tempStartIndex);
	}
	
	/**
	 * 获取最后一页的评论
	 */
	private void requestBookReviewEndPage() {
		// 最后一页的评论数.
		int endPageReviewCounut = mReviews.getTotalResults() % mReviews.getItemsPerPage();
		// 最后一页评论的起始索引.
		int endPageStartIndex = mReviews.getTotalResults() - endPageReviewCounut + 1;
		if (endPageStartIndex != mPreStartIndex) {
			Log.d("next", "endPageStartIndex=" + endPageStartIndex);
			requestBookReviews(endPageStartIndex);
		} else {
			UiUtil.toastShort(this, "已经是最后一页了。");
		}
	}
	
	private class ReviewPageClick implements View.OnClickListener {
		public static final int TYPE_UNKNOWN = -1;
		public static final int TYPE_FIRST_PAGE = 0;
		public static final int TYPE_PRE_PAGE = 1;
		public static final int TYPE_NEXT_PAGE = 2;
		public static final int TYPE_END_PAGE = 3;
		
		private int _type = TYPE_UNKNOWN;
		public ReviewPageClick(int type) {
			_type = type;
		}
		
		@Override
		public void onClick(View v) {
			switch (_type) {
			case TYPE_FIRST_PAGE:
				requestBookReviewFirstPage();
				break;
			case TYPE_PRE_PAGE:
				requestBookReviewPreviousPage();
				break;
			case TYPE_NEXT_PAGE:
				requestBookReviewNextPage();
				break;
			case TYPE_END_PAGE:
				requestBookReviewEndPage();
				break;
			}
		}
		
	}
	
	/**
	 * 给页头设置单击监听, 当点击时, 如果之前没有成功请求到数据, 则发起请求.
	 * @author zhao
	 *
	 */
	private class TabHeaderOnClickListener implements View.OnClickListener {
		private int _tabPageIndex = 0;
		TabHeaderOnClickListener(int tabPageIndex) {
			_tabPageIndex = tabPageIndex;
		}
		
		@Override
		public void onClick(View v) {
			mViewPager.setCurrentItem(_tabPageIndex);
		}
		
	}
	
	private class TabPageAdapter extends PagerAdapter {

		private List<View> _pageViews;
		
		TabPageAdapter(List<View> pageViews) {
			_pageViews = pageViews;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView(_pageViews.get(position));
		}
		
		@Override
		public void finishUpdate(ViewGroup container) {
			super.finishUpdate(container);
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			container.addView(_pageViews.get(position), 0);
			// 这个switch用于处理各个页面中的事件。
			switch (position) {
			case BOOK_REVIEWS_PAGE_INDEX:
				((ListView) mBookReviewsView.findViewById(R.id.book_reviews_reviewList)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View v, int position,
							long id) {
						Review review = (Review) mReviewsAdapter.getItem(position);
						Intent intent = new Intent(BookSearchResultActivity.this, 
								BookReviewContentActivity.class);
						Log.d("onItemClick", review.toString());
						intent.putExtra(BookReviewContentActivity.REVIEW, review);
						startActivity(intent);
					}
				});
				mBookReviewsView.findViewById(R.id.book_reviews_textview_start)
					.setOnClickListener(new ReviewPageClick(ReviewPageClick.TYPE_FIRST_PAGE));
				mBookReviewsView.findViewById(R.id.book_reviews_textview_pre)
					.setOnClickListener(new ReviewPageClick(ReviewPageClick.TYPE_PRE_PAGE));
				mBookReviewsView.findViewById(R.id.book_reviews_textview_next)
					.setOnClickListener(new ReviewPageClick(ReviewPageClick.TYPE_NEXT_PAGE));
				mBookReviewsView.findViewById(R.id.book_reviews_textview_end)
					.setOnClickListener(new ReviewPageClick(ReviewPageClick.TYPE_END_PAGE));
				break;
			}
			return _pageViews.get(position);
		}
		
		@Override
		public int getCount() {
			return _pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		
		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}
		
		@Override
		public Parcelable saveState() {
			return null;
		}
		
		@Override
		public void startUpdate(ViewGroup container) {
		}
		
	}
	
	private class TabPageChangeListener implements ViewPager.OnPageChangeListener {

		final int one = mCursorOffset * 2 + mBmpWidth;
		final int two = one * 2;

		@Override
		public void onPageSelected(int pageIndex) {
			// TODO Auto-generated method stub
			Animation animation = null;
			Log.d("onPageSelected", "mCursorOffset=" + mCursorOffset 
					+ "; mBmpWidth=" + mBmpWidth + "; one=" + one 
					+ "; two=" + two);
			Log.d("onPageSelected", "mCursorOffset=" + mCursorOffset + "; one=" + one + "; two=" + two);
			switch (pageIndex) {
			case BOOK_BASIC_INFO_PAGE_INDEX:
				if (mCurrentPageIndex == 1) {
					animation = new TranslateAnimation(one, mCursorOffset, 0, 0);
				} else if (mCurrentPageIndex == 2) {
					animation = new TranslateAnimation(two, mCursorOffset, 0, 0);
				}
				// mBook为null时说明没有成功获取书籍的基本信息.
				if (mBook == null)
					requestBookBasicInfoByIsbn();
				break;
			case BOOK_PRICES_PAGE_INDEX:
				if (mCurrentPageIndex == 0) {
					animation = new TranslateAnimation(mCursorOffset, one, 0, 0);
				} else if (mCurrentPageIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				}
				// mPrices为null时说明没能成功获取书籍价格信息
				if (mPriceUrls == null)
					requestBookPrices();
				break;
			case BOOK_REVIEWS_PAGE_INDEX:
				if (mCurrentPageIndex == 0) {
					animation = new TranslateAnimation(mCursorOffset, two, 0, 0);
				} else if (mCurrentPageIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}
				// mReviews为null时说明没能成功获取书籍评论信息
				if (mReviews == null)
					requestBookReviews(mPreStartIndex);
				break;
			}
			
			mCurrentPageIndex = pageIndex;
			// True:图片停在动画结束位置
			animation.setFillAfter(true);
			animation.setDuration(300);
			mCursor.startAnimation(animation);
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}
	}
	
	
	private BookHandler mBookHander = new BookHandler(this);
	private static class BookHandler extends Handler {
		private static final String TAG = "BookHandler";
		private WeakReference<BookSearchResultActivity> _activityRef;
		
		BookHandler(BookSearchResultActivity activity) {
			_activityRef = new WeakReference<BookSearchResultActivity>(activity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			BookSearchResultActivity activity = _activityRef.get();
			// 关闭进度对话框.
			activity.mProgressDialog.dismiss();
			switch (msg.what) {
			case REQUEST_BOOK_BASIC_INFO_SUCCESSED:
				Log.d(TAG, "REQUEST_BOOK_BASIC_INFO_SUCCESSED");
				AssistantSQLiteHelper.save(activity, activity.mBook);
				
				activity.onRequestBookBasicInfoOk();
				UiUtil.toastShort(activity, "获取书籍信息成功!");
				break;
			case REQUEST_BOOK_BASIC_INFO_FAILED:
				Log.d(TAG, "REQUEST_BOOK_BASIC_INFO_FAILED");
				// 如果书籍基本信息请求失败, 则关闭页面.
				activity.finish();
				break;
			case REQUEST_BOOK_PRICES_SUCCESSED:
				Log.d(TAG, "REQUEST_BOOK_PRICES_SUCCESSED");
				activity.onRequestBookPricesOk();
				UiUtil.toastShort(activity, "获取书籍价格信息成功!");
				break;
			case REQUEST_BOOK_PRICES_FAILED:
				Log.d(TAG, "REQUEST_BOOK_PRICES_FAILED");
				UiUtil.toastShort(activity, "获取书籍价格信息失败!");
				break;
			case REQUEST_BOOK_REVIEWS_SUCCESSED:
				Log.d(TAG, "REQUEST_BOOK_REVIEWS_SUCCESSED");
				activity.onRequestBookReviewsOk();
				UiUtil.toastShort(activity, "获取书籍评论信息成功!");
				break;
			case REQUEST_BOOK_REVIEWS_FAILED:
				Log.d(TAG, "REQUEST_BOOK_REVIEWS_FAILED");
				UiUtil.toastShort(activity, "获取书籍评论信息失败!");
				break;
			}
			super.handleMessage(msg);
		}
	}
}
