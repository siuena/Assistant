package com.coofee.assistant.book.model;

public class Tag {
	private int count;
	private String name;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer info = new StringBuffer();
		info.append("{count: ").append(count)
		.append("; name: ").append(name).append("}");
		return info.toString();
	}
}
