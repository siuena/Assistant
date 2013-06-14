package com.coofee.assistant.book;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.jsoup.Jsoup;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import com.coofee.assistant.book.model.People;
import com.coofee.assistant.book.model.Rate;
import com.coofee.assistant.book.model.Review;
import com.coofee.assistant.book.model.Reviews;
import com.coofee.assistant.util.HttpUtil;
import com.coofee.assistant.util.StringUtil;

public class BookHelper {

	public static final String TAG = "HttpUtil";

	/**
	 * 通过ISBN搜索获取书籍信息(JSON格式);
	 */
	public static String getBookInfoByIsbn(String isbn) {
		if (TextUtils.isEmpty(isbn)) {
			Log.d(TAG + " getBookInfoByIsbn", "isbn is empty or null.");
			return null;
		}
		
		final String BOOK_SEARCH_BY_ISBN_API_URI = "http://api.douban.com/v2/book/isbn/";
		String bookUrl = BOOK_SEARCH_BY_ISBN_API_URI + isbn;
		
		return getContent(bookUrl);
	}

	/**
	 * 获取用户信息
	 * @param userUri
	 * @return
	 */
	public static People getPeopleInfo(String userUri) {
		InputStream in = getInputStream(userUri);
		
		if (in != null)
			return parsePeopleInfo(in, "UTF-8");
		
		return null;
	}
	
//	private static final String TITLE = "title";
//	private static final String LINK = "link";
	private static final String LINK_REL_ALTERNATE = "alternate";
	private static final String LINK_REL_ICON = "icon";
	private static final String CONTENT = "content";
	private static final String LOCATION = "location";
	private static final String SIGNATURE = "signature";
	private static final String UID = "uid";
//	private static final String URI = "uri";
	/**
	 * 解析获取的用户信息
	 */
	private static People parsePeopleInfo(InputStream in, String charset) {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(in, charset);
			People people = new People();
			for (int event = parser.next(); event != XmlPullParser.END_DOCUMENT; event = parser
					.next()) {
				if (event == XmlPullParser.START_TAG) {
					Assert.assertNotNull(people);
					String name = parser.getName();
					if (name.equals(TITLE)) {
						people.setTitle(parser.nextText());
					} else if (name.equals(LINK)) {
						// 下标从0开始
						String attrRel = parser.getAttributeValue(1);
						if (attrRel.equals(LINK_REL_ALTERNATE)) {
							people.setHomePageUrl(parser.getAttributeValue(0));
						} else if (attrRel.equals(LINK_REL_ICON)) {
							people.setIconUrl(parser.getAttributeValue(0));
						}
					} else if (name.equals(CONTENT)) {
						people.setContent(parser.nextText());
					} else if (name.equals(LOCATION)) {
						people.setLocation(parser.nextText());
					} else if (name.equals(SIGNATURE)) {
						people.setSignature(parser.nextText());
					} else if (name.equals(UID)) {
						people.setUid(parser.nextText());
					} else if (name.equals(URI)) {
						people.setApiUri(parser.nextText());
					}
				}
			}
			return people;
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	/**
	 * 获取书籍的价格信息; 返回的String[][]数组中保存了书籍的价格和其对应的网址, 索引值从0开始分别是当当, 亚马逊,
	 * 99书城等该书籍的价格和其网址. 其中第一列中保存的是书籍价格, 第二列中保存的是书籍的网址. 如果未能成功获取书籍的价格,
	 * 则在对应的索引值中写入空字符串.
	 * 
	 * @return 所有网店的书籍价格都没有找到, 则返回null; 否则返回String[].
	 */
	public static String[][] getBookPricesByIsbn(String isbn) {
		if (TextUtils.isEmpty(isbn)) {
			Log.d(TAG + " getBookPricesByIsbn", "isbn is empty or null.");
			return null;
		}

		String[][] priceUrls = new String[3][2];

		String[] priceUrl = getMinBookPriceFromDangdangByIsbn(isbn);
		if (priceUrl != null) {
			priceUrls[0][0] = priceUrl[0];
			priceUrls[0][1] = priceUrl[1];
		} else {
			priceUrls[0][0] = "获取价格失败!";
			priceUrls[0][1] = "";
		}

		priceUrl = getMinBookPriceFromAmazonByIsbn(isbn);
		if (priceUrl != null) {
			priceUrls[1][0] = priceUrl[0];
			priceUrls[1][1] = priceUrl[1];
		} else {
			priceUrls[1][0] = "获取价格失败!";
			priceUrls[1][1] = "";
		}

		priceUrl = getMinBookPriceFrom99ShuchengByIsbn(isbn);
		if (priceUrl != null) {
			priceUrls[2][0] = priceUrl[0];
			priceUrls[2][1] = priceUrl[1];
		} else {
			priceUrls[2][0] = "获取价格失败!";
			priceUrls[2][1] = "";
		}
		return priceUrls;
	}

	/**
	 * 通过书籍的ISBN来获取从startIndex开始的最多10条评论信息.
	 */
	public static Reviews getBookReviewsByIsbn(String isbn, int startIndex) {
		return getBookReviewsByIsbn(isbn, startIndex, 10);
	}

	/**
	 * 通过书籍的ISBN来获取从startIndex开始的最多maxResult条评论信息.
	 * 
	 * startIndex的值从1开始.
	 */
	public static Reviews getBookReviewsByIsbn(String isbn, int startIndex,
			int maxResults) {
		if (TextUtils.isEmpty(isbn)) {
			Log.d(TAG + " getBookReviewsByIsbn", "isbn is empty or null.");
			return null;
		}

		if (startIndex <= 0 || maxResults <= 0) {
			Log.d(TAG + " getBookReviewsByIsbn",
					"startIndex or maxResults must be greater than 0.");
			return null;
		}

		final String REVIEWS_URL = "http://api.douban.com/book/subject/isbn/"
				+ isbn + "/reviews?" + "start-index=" + startIndex
				+ "&max-results=" + maxResults;
		InputStream in = getInputStream(REVIEWS_URL);
		if (in != null)
			return parseReviews(in, "UTF-8");
		else
			return null;
	}

	public static String getContent(String url) {
		InputStream in = null;
		try {
			in = getInputStream(url);
			if (in == null) return null;
			BufferedReader reader = new BufferedReader(
										new InputStreamReader(
												new BufferedInputStream(in)));
			String line = null;
			StringBuilder content = new StringBuilder(200);
			while ((line = reader.readLine()) != null) {
				content.append(line);
			}
			
			return content.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
	
	private static InputStream getInputStream(String url) {
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setConnectTimeout(HttpUtil.TIME_OUT);
			conn.setReadTimeout(HttpUtil.TIME_OUT);
			conn.setRequestMethod("GET");
			
			conn.setRequestProperty("Connection", "keep-alive");
//			conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
//			conn.setRequestProperty("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
//			conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//			conn.setRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31");
			
			return conn.getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 从网络获取评论全文
	 */
	public static String getBookReviewContent(String reviewContentUrl) {
		Log.d(TAG + " getBookReviewContent", reviewContentUrl);
		try {
			org.jsoup.Connection conn = HttpUtil
					.getJsoupConnection(reviewContentUrl);
			org.jsoup.nodes.Document doc = conn.get();
			org.jsoup.select.Elements elements = doc
					.select("span[property=v:description]");
			if (!elements.isEmpty()) {
				org.jsoup.nodes.Element elem = elements.get(0);
				String reviewContent = elem.html();
				Log.d("requestReviewContent", reviewContent);
				return reviewContent;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 通过ISBN从当当获取某一本书籍的最低价格和其对应的网址. 参考当当网页源码.
	 */
	public static String[] getMinBookPriceFromDangdangByIsbn(String isbn) {
		final String SEARCH_URL = "http://m.dangdang.com/gw_search.php";
		try {
			org.jsoup.Connection conn = HttpUtil.getJsoupConnection(SEARCH_URL);
			org.jsoup.nodes.Document doc = conn.data("key", isbn).get();
			org.jsoup.select.Elements productList = doc.select("div.ddmlist");
			if (productList != null && !productList.isEmpty()) {
				// 遍历获取的所有书籍.
				org.jsoup.select.Elements products = productList.get(0)
						.getElementsByTag("p");
				// 用来保存获取的所有书籍的价格和网址.
				String[][] priceUrls = new String[products.size()][2];
				Iterator<org.jsoup.nodes.Element> productIter = products
						.iterator();
				final String WAP_DANGDANG_URL = "http://m.dangdang.com/";
				for (int productIndex = 0; productIter.hasNext(); productIndex++) {
					org.jsoup.nodes.Element product = productIter.next();
					Iterator<org.jsoup.nodes.Element> productItemIter = product
							.getElementsByTag("span").iterator();
					// 遍历获取的所有书籍中的每一项, 保存每一项的价格和网址.
					while (productItemIter.hasNext()) {
						org.jsoup.nodes.Element productItem = productItemIter
								.next();
						if (productItem.className().equals("prouct_name")) {
							// 找到书籍的网址
							org.jsoup.nodes.Element bookUrlElem = productItem
									.child(0);
							String bookUrl = bookUrlElem.attr("href");
							priceUrls[productIndex][1] = WAP_DANGDANG_URL
									+ bookUrl;
						} else if (productItem.text().startsWith("当当价：")) {
							// 获取书籍的价格, 将"当当价: "四个字符去掉.
							String bookPrice = productItem.text().substring(4)
									.trim();
							priceUrls[productIndex][0] = bookPrice;
						}
					}
				}

				// 从获取的所有书籍中找出价格最低的图书
				String[] minPriceUrl = priceUrls[0];
				float minPrice = Float.parseFloat(priceUrls[0][0]);
				for (int productIndex = 1; productIndex < priceUrls.length; productIndex++) {
					float currentPrice = Float
							.parseFloat(priceUrls[productIndex][0]);
					if (currentPrice < minPrice) {
						minPriceUrl = priceUrls[productIndex];
					}
				}
				minPriceUrl[0] = "￥" + minPriceUrl[0];
				return minPriceUrl;
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 通过ISBN从亚马逊获取某一本书籍的最低价格和其对应的网址. 亚马逊访问时会出现503错误.
	 */
	public static String[] getMinBookPriceFromAmazonByIsbn(String isbn) {
		final String SEARCH_URL = "http://www.amazon.cn/gp/aw/s/ref=is_box_";
		try {
			org.jsoup.Connection conn = Jsoup.connect(SEARCH_URL);
			org.jsoup.nodes.Document doc = conn
					.data("__mk_zh_CN",
							"%E4%BA%9A%E9%A9%AC%E9%80%8A%E7%BD%91%E7%AB%99")
					.data("k", isbn).get();
			org.jsoup.select.Elements products = doc.select("table.dpPrice");
			if (products != null && !products.isEmpty()) {
				org.jsoup.nodes.Element product = products.get(0);
				org.jsoup.select.Elements prices = product
						.select("span.dpOurPrice");
				if (prices != null && !prices.isEmpty()) {
					org.jsoup.nodes.Element price = prices.get(0);
					String[] bookPrices = new String[2];
					bookPrices[0] = price.text();
					bookPrices[1] = SEARCH_URL
							+ "?__mk_zh_CN=%E4%BA%9A%E9%A9%AC%E9%80%8A%E7%BD%91%E7%AB%99"
							+ "&k=" + isbn;
					return bookPrices;
				}
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 通过ISBN从99书城获取某一本书籍的最低价格和其对应的网址.
	 */
	public static String[] getMinBookPriceFrom99ShuchengByIsbn(String isbn) {
		/**
		 * <p class="ll">
		 * <b>￥20.5</b> <del>￥29.8</del> <i>69折</i> ...
		 * </p>
		 */
		final String SEARCH_URL = "http://search.bookuu.com/";
		try {
			org.jsoup.Connection conn = HttpUtil.getJsoupConnection(SEARCH_URL);
			org.jsoup.nodes.Document doc = conn.data("k", isbn).get();
			org.jsoup.select.Elements elems = doc.select("div.books-list");
			if (elems != null && !elems.isEmpty()) {
				org.jsoup.nodes.Element book = elems.get(0);
				String url = book.select("a.l").get(0).attr("href");
				String price = book.select("p.ll").get(0).getElementsByTag("b")
						.text();
				String[] priceUrl = new String[2];
				priceUrl[0] = price;
				priceUrl[1] = url;
				return priceUrl;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// 类型不可知
	private static final int TYPE_UNKNOWN = -1;
	// 处于FEED中
	private static final int TYPE_FEED = 0;
	// 处于ENTRY
	private static final int TYPE_ENTRY = 1;
	// 处于AUTHOR中
	private static final int TYPE_ENTRY_AUTHOR = 2;

	private static final String FEED = "feed";
	private static final String TITLE = "title";
	private static final String LINK = "link";
	private static final String START_INDEX = "startIndex";
	private static final String TOTAL_RESULTS = "totalResults";
	private static final String ENTRY = "entry";
	private static final String ITEMS_PER_PAGE = "itemsPerPage";
	private static final String LINK_ALTERNATE = "alternate";
	private static final String LINK_ICON = "icon";
	private static final String ID = "id";
	private static final String AUTHOR = "author";
	private static final String NAME = "name";
	private static final String URI = "uri";
	private static final String PUBLISHED = "published";
	private static final String UPDATED = "updated";
	private static final String SUMMARY = "summary";
	private static final String USELESS = "useless";
	private static final String VOTES = "votes";
	private static final String RATING = "rating";

	/**
	 * 解析获取的XML文件, 获取评论信息.
	 */
	public static Reviews parseReviews(InputStream in, String charset) {
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(in, charset);
			List<Review> reviewList = null;
			Reviews reviews = null;
			Review review = null;
			People author = null;
			String name = null;
			for (int event = parser.next(), type = TYPE_UNKNOWN; event != XmlPullParser.END_DOCUMENT; event = parser
					.next()) {
				switch (event) {
				case XmlPullParser.START_TAG:
					name = parser.getName();
					// Log.d("Parse", "start tag Name=" + name);
					if (name.equals(FEED)) {
						reviews = new Reviews();
						reviewList = new ArrayList<Review>();
						type = TYPE_FEED;
					} else {
						switch (type) {
						case TYPE_FEED:
							if (name.equals(TITLE)) {
								reviews.setTitle(parser.nextText());
							} else if (name.equals(LINK)) {
								reviews.setUrl(parser.getAttributeValue(0));
							} else if (name.equals(ENTRY)) {
								review = new Review();
								type = TYPE_ENTRY;
							} else if (name.equals(START_INDEX)) {
								reviews.setStartIndex(StringUtil
										.parseInt(parser.nextText()));
							} else if (name.equals(TOTAL_RESULTS)) {
								reviews.setTotalResults(StringUtil
										.parseInt(parser.nextText()));
							} else if (name.equals(ITEMS_PER_PAGE)) {
								reviews.setItemsPerPage(StringUtil
										.parseInt(parser.nextText()));
							}
							break;
						case TYPE_ENTRY:
							if (name.equals(ID)) {
								review.setApiUrl(parser.nextText());
							} else if (name.equals(TITLE)) {
								review.setTitle(parser.nextText());
							} else if (name.equals(AUTHOR)) {
								author = new People();
								type = TYPE_ENTRY_AUTHOR;
							} else if (name.equals(PUBLISHED)) {
								review.setPublishDate(StringUtil
										.parseToDateString(parser.nextText()));
							} else if (name.equals(UPDATED)) {
								review.setUpdated(StringUtil
										.parseToDateString(parser.nextText()));
							} else if (name.equals(LINK)) {
								if (parser.getAttributeValue(1).equals(
										LINK_ALTERNATE))
									review.setUrl(parser.getAttributeValue(0));
							} else if (name.equals(SUMMARY)) {
								review.setSummary(parser.nextText());
							} else if (name.equals(USELESS)) {
								review.setUseless(StringUtil.parseInt(parser
										.getAttributeValue(0)));
							} else if (name.equals(VOTES)) {
								review.setVotes(StringUtil.parseInt(parser
										.getAttributeValue(0)));
							} else if (name.equals(RATING)) {
								Rate rate = new Rate();
								rate.setMax(StringUtil.parseInt(parser
										.getAttributeValue(0)));
								rate.setMin(StringUtil.parseInt(parser
										.getAttributeValue(1)));
								rate.setValue(StringUtil.parseInt(parser
										.getAttributeValue(2)));
								review.setRating(rate);
							}
							break;
						case TYPE_ENTRY_AUTHOR:
							if (name.equals(LINK)) {
								String attrRel = parser.getAttributeValue(1);
								if (attrRel.equals(LINK_ALTERNATE))
									author.setHomePageUrl(parser
											.getAttributeValue(0));
								else if (attrRel.equals(LINK_ICON)) {
									author.setIconUrl(parser
											.getAttributeValue(0));
								}
							} else if (name.equals(NAME)) {
								author.setTitle(parser.nextText());
							} else if (name.equals(URI)) {
								author.setApiUri(parser.nextText());
							}
							break;
						}
					}
					break;
				case XmlPullParser.END_TAG:
					name = parser.getName();
					// Log.d("Parse", "end tag Name=" + name);
					if (name.equals(AUTHOR)) {
						Log.d("author", author.toString());
						review.setAuthor(author);
						type = TYPE_ENTRY;
					} else if (name.equals(ENTRY)) {
						Log.d("review", review.toString());
						reviewList.add(review);
						type = TYPE_FEED;
					} else if (name.equals(FEED)) {
						reviews.setReviewList(reviewList);
					}
					break;
				}
			}

			Log.d("Reviews", reviews.toString());
			return reviews;
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
