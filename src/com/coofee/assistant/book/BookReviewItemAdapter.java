package com.coofee.assistant.book;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.coofee.assistant.R;
import com.coofee.assistant.book.model.Rate;
import com.coofee.assistant.book.model.Review;

public class BookReviewItemAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	
	private List<Review> mReviewList;
	
	public BookReviewItemAdapter(Context context, List<Review> reviewList) {
		mInflater = LayoutInflater.from(context);
		mReviewList = reviewList;
	}
	
	public void setReviewList(List<Review> reviewList) {
		mReviewList = reviewList;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mReviewList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mReviewList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			convertView = (View) mInflater.inflate(R.layout.book_reviews_list_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.book_reviews_list_item_title);
			holder.author = (TextView) convertView.findViewById(R.id.book_reviews_list_item_author);
			holder.rating = (RatingBar) convertView.findViewById(R.id.book_reviews_list_item_rating);
			holder.ratingScore = (TextView) convertView.findViewById(R.id.book_reviews_list_item_ratingScore);
			holder.summary = (TextView) convertView.findViewById(R.id.book_reviews_list_item_summary);
			holder.pubDate = (TextView) convertView.findViewById(R.id.book_reviews_list_item_pubDate);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Review review = mReviewList.get(position);
		holder.title.setText(review.getTitle());
		holder.author.setText(review.getAuthor().getTitle());
		// 因为获取到的评级是以5为最大值，以0~5之间的整数表示.
		Rate rate = review.getRating();
		if (rate != null) {
			holder.rating.setVisibility(View.VISIBLE);
			holder.ratingScore.setVisibility(View.VISIBLE);
			holder.rating.setMax(rate.getMax());
			holder.rating.setProgress(rate.getValue());
			holder.ratingScore.setText(String.valueOf(rate.getValue()));
		} else {
			holder.rating.setVisibility(View.GONE);
			holder.ratingScore.setVisibility(View.GONE);
		}
		holder.summary.setText(review.getSummary());
		holder.pubDate.setText(review.getPublishDate());
		return convertView;
	}
	
	class ViewHolder {
		TextView title;
		TextView author;
		RatingBar rating;
		TextView ratingScore;
		TextView summary;
		TextView pubDate;
	}

}
