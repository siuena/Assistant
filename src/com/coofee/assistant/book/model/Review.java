package com.coofee.assistant.book.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {
	/**
	 * 书评的Api Url
	 */
	private String apiUrl;

	/**
	 * 书评的标题
	 */
	private String title;
	/**
	 * 书评的作者
	 */
	private People author;

	/**
	 * 书评发表时间
	 */
	private String publishDate;
	/**
	 * 最后修改时间
	 */
	private String updated;

	/**
	 * 书评对应的网络地址
	 */
	private String url;
	/**
	 * 书评简介
	 */
	private String summary;
	/**
	 * 无用
	 */
	private int useless;
	/**
	 * 有用
	 */
	private int votes;
	/**
	 * 给予所评价书籍的等级
	 */
	private Rate rating;

	public Review() {}
	public Review(Parcel source) {
		apiUrl = source.readString();
		title = source.readString();
		// 读取复杂类型
		author = source.readParcelable(People.class.getClassLoader());
		publishDate = source.readString();
		updated = source.readString();
		url = source.readString();
		summary = source.readString();
		useless = source.readInt();
		votes = source.readInt();
		// 读取复杂类型
		rating = source.readParcelable(Rate.class.getClassLoader());
	}
	
	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public People getAuthor() {
		return author;
	}

	public void setAuthor(People author) {
		this.author = author;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public int getUseless() {
		return useless;
	}

	public void setUseless(int useless) {
		this.useless = useless;
	}

	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}

	public Rate getRating() {
		return rating;
	}

	public void setRating(Rate rating) {
		this.rating = rating;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer info = new StringBuffer();
		info.append("{apiUri:").append(apiUrl).append("; title:").append(title)
				.append("; author:").append(author).append("; publishDate:")
				.append(publishDate).append("; updated:").append(updated)
				.append("; url:").append(url).append("; summary:")
				.append(summary).append("; useless:").append(useless)
				.append("; votes:").append(votes).append("; rating:")
				.append(rating).append("}");

		return info.toString();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(apiUrl);
		dest.writeString(title);
		// 写复杂对象
		dest.writeParcelable(author, flags);
		dest.writeString(publishDate);
		dest.writeString(updated);
		dest.writeString(url);
		dest.writeString(summary);
		dest.writeInt(useless);
		dest.writeInt(votes);
		// 写复杂对象
		dest.writeParcelable(rating, flags);
	}
	
	public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {

		@Override
		public Review createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new Review(source);
		}

		@Override
		public Review[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Review[size];
		}
	};
}