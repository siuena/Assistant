package com.coofee.assistant.util;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.coofee.assistant.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class ImageLoaderUtil {

	private static final String TAG = ImageLoaderUtil.class.getCanonicalName();

	/**
	 * 获取用户的头像.
	 */
	public static void displayPeopleIcon(ImageView peopleIcon, String peopleIconUrl) {
		displayImage(peopleIcon, peopleIconUrl, R.drawable.people_icon_default, 
				R.drawable.people_icon_default);
	}
	
	/**
	 * 获取书籍的封面
	 */
	public static void displayBookCover(ImageView bookCover, String bookCoverUrl) {
		displayImage(bookCover, bookCoverUrl, R.drawable.book_cover_default, 
				R.drawable.book_cover_default);
	}
	
	/**
	 * 获取图片.
	 * 如果在SD卡上有缓存的话,则从缓存获取图片; 
	 * 否则的话就从网络下载并显示图片.
	 */
	public static void displayImage(ImageView imageView, String imageUrl, 
			int defaultImageResId, int downloadingImageResId) {
		File cachedFile = foundDiscCachedFile(imageUrl);
		if (cachedFile != null && cachedFile.exists()) {
			Log.d(TAG, "found cached file. " + cachedFile.getAbsolutePath());
			Bitmap bitmap = BitmapFactory.decodeFile(cachedFile.getAbsolutePath());
			if (bitmap != null) {
				Log.d(TAG, "decode file successed.");
				imageView.setImageBitmap(bitmap);
			} else {
				Log.d(TAG, "decode file failed, get it from internet.");
				displayImage(imageView, imageUrl, 
						getDiscOptions(defaultImageResId, downloadingImageResId));
			}
		} else {
			Log.d(TAG, "couldn't found cached file, download from " + imageUrl);
			displayImage(imageView, imageUrl, 
					getDiscOptions(defaultImageResId, downloadingImageResId));
		}
	}
	
	/**
	 * 获取一个缓存保存在SD卡的Options.
	 */
	public static DisplayImageOptions getDiscOptions(int defaultImageResId, 
			int downloadingImageResId) {
		return new DisplayImageOptions.Builder()
			.showStubImage(defaultImageResId) // 设置加载图片过程中时, 显示的图片.
			.showImageForEmptyUri(downloadingImageResId) // 设置当图片地址为null或空时, 显示的图片.
			.cacheOnDisc()
			.build();
	}
	
	public static void displayImage(final ImageView imageView, String imageUrl, 
			DisplayImageOptions options) {
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(imageUrl, imageView, options, new ImageLoadingListener() {
			private static final String TAG = "ImageLoadingListener";
			
			@Override
			public void onLoadingCancelled() {
				// TODO Auto-generated method stub
				Log.d(TAG + " onLoadingCancelled", "downloading image cancelled.");
			}

			@Override
			public void onLoadingComplete(Bitmap loadedImage) {
				// TODO Auto-generated method stub
				Log.d(TAG + " onLoadingComplete", "downloading image completed.");
			}

			@Override
			public void onLoadingFailed(FailReason failReason) {
				// TODO Auto-generated method stub
				Log.d(TAG + " onLoadingFailed", "" + failReason.toString());
				String msg = null;
				switch (failReason) {
				case IO_ERROR:
					msg = "I/O error.";
					Log.d(TAG + " onLoadingFailed", msg);
					break;
				case OUT_OF_MEMORY:
					msg = "Out of memory error.";
					Log.d(TAG + " onLoadingFailed", msg);
					break;
				case UNKNOWN:
					msg = "Unknown error.";
					Log.d(TAG + " onLoadingFailed", msg);
					break;
				}
				UiUtil.toastShort(imageView.getContext(), msg);
			}

			@Override
			public void onLoadingStarted() {
				// TODO Auto-generated method stub
				Log.d(TAG + " onLoadingStarted", "start downloading image.");
			}
			
		});
	}

	/**
	 * 根据文件的下载地址, 从SD卡缓存中获取缓存的文件.
	 * @param imageUrl 寻找的文件的下载地址
	 */
	public static File foundDiscCachedFile(String imageUrl) {
		return ImageLoader.getInstance().getDiscCache().get(imageUrl);
	}
}
