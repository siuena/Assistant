package com.coofee.assistant.book.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户发表评论时给予书籍的星级.
 * 
 */
public class Rate implements Parcelable {

	private int max = 5;
	private int min = 0;
	private int value = 0;

	public Rate() {}
	
	public Rate(Parcel source) {
		max = source.readInt();
		min = source.readInt();
		value = source.readInt();
	}
	
	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer info = new StringBuffer();
		info.append("{max: ").append(max).append("; min: ").append(min)
				.append("; value: ").append(value).append("}");
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
		dest.writeInt(max);
		dest.writeInt(min);
		dest.writeInt(value);
	}

	public static final Parcelable.Creator<Rate> CREATOR = new Creator<Rate>() {
		
		@Override
		public Rate[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Rate[size];
		}
		
		@Override
		public Rate createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new Rate(source);
		}
	};
}
