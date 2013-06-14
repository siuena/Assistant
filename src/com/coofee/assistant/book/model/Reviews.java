package com.coofee.assistant.book.model;

import java.util.List;

/**
 * 下面的连接表示获取从第2条评论开始的2条评论.(start-index的值默认从1开始.) 参数orderby表示按照score级按投票排序(默认);
 * time，按发布时间排序. max-results表示最多获取几条记录. start-index表示起始评论的索引值.
 * http://api.douban.
 * com/book/subject/isbn/9787503940361/reviews?start-index=2&max-results=2;
 * 
 * @author zhao
 * 
 */
public class Reviews {

	/**
	 * 表示对哪本书的评论
	 */
	private String title;
	/**
	 * 书评对应的网络地址
	 */
	private String url;

	/**
	 * 起始评论的索引.
	 */
	private int startIndex;

	/**
	 * 所有评论的总数目.
	 */
	private int totalResults;

	/**
	 * 每页中包含的评论数.
	 */
	private int itemsPerPage;

	private List<Review> reviewList;
	
	public List<Review> getReviewList() {
		return reviewList;
	}

	public void setReviewList(List<Review> reviewList) {
		this.reviewList = reviewList;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
	}

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer info = new StringBuffer();
		info.append("{title:").append(title)
			.append("; url:").append(url)
			.append("; startIndex:").append(startIndex)
			.append("; totalResults:").append(totalResults)
			.append("; itemsPerPage:").append(itemsPerPage)
			.append("; reviewList:").append(reviewList)
			.append("}");
		return info.toString();
	}
}
