package com.coofee.assistant.book.model;

import android.text.TextUtils;

import com.coofee.assistant.util.StringUtil;

public class Book {

	/**
	 * 对该书的评级
	 */
	private Rating rating;
	/**
	 * 副标题
	 */
	private String subTitle;
	/**
	 * 出版日期
	 */
	private String pubdate;
	/**
	 * 书籍封面的网络地址
	 */
	private String image;
	/**
	 * 书的板式(精装/简装/平装等)
	 */
	private String binding;
	/**
	 * 不同大小封面的地址
	 */
	private Covers images;
	/**
	 * 本书的网络地址
	 */
	private String alt;
	/**
	 * 本书的id
	 */
	private String id;
	/**
	 * 书名
	 */
	private String title;
	/**
	 * 作者信息
	 */
	private String author_intro;
	/**
	 * 本书的标签
	 */
	private Tag[] tags;
	/**
	 * 书籍的原名
	 */
	private String origin_title;
	/**
	 * 定价
	 */
	private String price;
	/**
	 * 译者
	 */
	private String[] translator;
	/**
	 * 页数
	 */
	private int pages;
	/**
	 * 出版社
	 */
	private String publisher;

	private String isbn10;
	private String isbn13;

	private String alt_title;
	/**
	 * 书籍信息对应的Api地址
	 */
	private String url;
	/**
	 * 作者
	 */
	private String[] author;
	/**
	 * 内容简介
	 */
	private String summary;

	public Rating getRating() {
		return rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getPubdate() {
		return pubdate;
	}

	public void setPubdate(String pubdate) {
		this.pubdate = pubdate;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		if (!TextUtils.isEmpty(image)) {
			this.image = image.replace("\\", "");
			return; 
		}
		this.image = image;
	}

	public String getBinding() {
		return binding;
	}

	public void setBinding(String binding) {
		this.binding = binding;
	}

	public Covers getImages() {
		return images;
	}

	public void setImages(Covers images) {
		this.images = images;
	}

	public String getAlt() {
		return alt;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor_intro() {
		return author_intro;
	}

	public void setAuthor_intro(String author_intro) {
		this.author_intro = author_intro;
	}

	public Tag[] getTags() {
		return tags;
	}

	public void setTags(Tag[] tags) {
		this.tags = tags;
	}

	public String getOrigin_title() {
		return origin_title;
	}

	public void setOrigin_title(String origin_title) {
		this.origin_title = origin_title;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String[] getTranslator() {
		return translator;
	}

	public void setTranslator(String[] translator) {
		this.translator = translator;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getIsbn10() {
		return isbn10;
	}

	public void setIsbn10(String isbn10) {
		this.isbn10 = isbn10;
	}

	public String getIsbn13() {
		return isbn13;
	}

	public void setIsbn13(String isbn13) {
		this.isbn13 = isbn13;
	}

	public String getAlt_title() {
		return alt_title;
	}

	public void setAlt_title(String alt_title) {
		this.alt_title = alt_title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String[] getAuthor() {
		return author;
	}

	public void setAuthor(String[] author) {
		this.author = author;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getIsbn() {
		if (!TextUtils.isEmpty(isbn13)) {
			return isbn13;
		}
		
		return isbn10;
	}
	
	public String getAuthors() {
		return StringUtil.arrayToString(author);
	}
	
	public String getTranslators() {
		return StringUtil.arrayToString(translator);
	}
	
	public String toShortString() {
		StringBuffer info = new StringBuffer();
		info.append("{书名: ").append(title)
		.append("; 原名: ").append(origin_title)
		.append("; 作者: ").append(StringUtil.arrayToString(author))
		.append("; 译者: ").append(StringUtil.arrayToString(translator))
		.append("; 定价: ").append(price)
		.append("; 出版社: ").append(publisher)
		.append("; 出版日期: ").append(pubdate)
		.append("; 页数: ").append(pages)
		.append("; 装帧: ").append(binding)
		.append("; ISBN: ").append(isbn13)
		.append("; 评级: ").append(rating)
		.append("}");
		return info.toString();
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer info = new StringBuffer();
		info.append("{书名: ").append(title)
		.append("; 原名: ").append(origin_title)
		.append("; 作者: ").append(StringUtil.arrayToString(author))
		.append("; 译者: ").append(StringUtil.arrayToString(translator))
		.append("; 定价: ").append(price)
		.append("; 出版社: ").append(publisher)
		.append("; 出版日期: ").append(pubdate)
		.append("; 页数: ").append(pages)
		.append("; 装帧: ").append(binding)
		.append("; ISBN: ").append(isbn13)
		.append("; 评级: ").append(rating)
		.append("; 标签: ").append(StringUtil.arrayToString(tags))
		.append("; 作者简介: ").append(author_intro)
		.append("; 作品简介: ").append(summary)
		.append("}");
		return info.toString();
	}

}
