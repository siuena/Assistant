package com.coofee.assistant.book.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 实现Parcelable接口的样例
 */
public class People implements Parcelable {
	/**
	 * 昵称
	 */
	private String title = "";
	/**
	 * 注册时的id
	 */
	private String uid = "";
	/**
	 * 个人介绍
	 */
	private String content = "";
	/**
	 * 居住地
	 */
	private String location = "";
	/**
	 * 个人签名
	 */
	private String signature = "";
	/**
	 * 头像网络地址
	 */
	private String iconUrl = "";
	/**
	 * 个人信息的API地址
	 */
	private String apiUri = "";
	/**
	 * 个人信息的网页地址
	 */
	private String homePageUrl = "";

	public People() {}
	public People(Parcel source) {
		title = source.readString();
		uid = source.readString();
		content = source.readString();
		location = source.readString();
		signature = source.readString();
		iconUrl = source.readString();
		apiUri = source.readString();
		homePageUrl = source.readString();
	}
	
	/**
	 * 获取用户的昵称
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 设置用户的昵称
	 */
	public void setTitle(String title) {
		if (title == null) {
			this.title = "";
			return;
		}
		
		this.title = title;
	}

	/**
	 * 获取用户的Id
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * 设置用户的id
	 */
	public void setUid(String uid) {
		if (uid == null) {
			this.uid = "";
			return;
		}
			
		this.uid = uid;
	}

	/**
	 * 获取用户的自我介绍
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置用户的自我介绍
	 */
	public void setContent(String content) {
		if (content == null) {
			this.content = "";
			return;
		}
		
		this.content = content;
	}

	/**
	 * 获取用户的居住地
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * 设置用户的居住地
	 */
	public void setLocation(String location) {
		if (location == null) {
			location = "";
			return;
		}
		
		this.location = location;
	}

	/**
	 * 获取用户签名
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * 设置用户签名
	 */
	public void setSignature(String signature) {
		if (signature == null) {
			this.signature = "";
			return;
		}
		
		this.signature = signature;
	}

	/**
	 * 获取用户头像的URL
	 */
	public String getIconUrl() {
		return iconUrl;
	}

	/**
	 * 设置用户头像的URL
	 */
	public void setIconUrl(String iconUrl) {
		if (iconUrl == null) {
			this.iconUrl = "";
			return;
		}
		this.iconUrl = iconUrl;
	}

	/**
	 * 获取个人信息的API地址
	 */
	public String getApiUri() {
		return apiUri;
	}

	/**
	 * 设置个人信息的API地址
	 */
	public void setApiUri(String apiUri) {
		if (apiUri == null) {
			this.apiUri = "";
			return;
		}
		
		this.apiUri = apiUri;
	}

	/**
	 * 获取用户的主页的地址
	 */
	public String getHomePageUrl() {
		return homePageUrl;
	}

	/**
	 * 设置用户主页地址
	 */
	public void setHomePageUrl(String homePageUrl) {
		if (homePageUrl == null) {
			this.homePageUrl = "";
			return;
		}
		
		this.homePageUrl = homePageUrl;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer info = new StringBuffer();
		info.append("{uid: ").append(uid)
		.append("; 昵称: ").append(title)
		.append("; 签名: ").append(signature)
		.append("; 个人介绍: ").append(content)
		.append("; 头像地址: ").append(iconUrl)
		.append("; 居住地: ").append(location)
		.append("; Api Url: ").append(apiUri)
		.append("; 个人主页地址: ").append(homePageUrl).append("}");
		
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
		dest.writeString(title);
		dest.writeString(uid);
		dest.writeString(content);
		dest.writeString(location);
		dest.writeString(signature);
		dest.writeString(iconUrl);
		dest.writeString(apiUri);
		dest.writeString(homePageUrl);
	}

	public static final Parcelable.Creator<People> CREATOR  = new Parcelable.Creator<People>() {

		@Override
		public People createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new People(source);
		}

		@Override
		public People[] newArray(int size) {
			// TODO Auto-generated method stub
			return new People[size];
		}
		
	};
}
