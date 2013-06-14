package com.coofee.assistant;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.app.Application;

import com.baidu.location.LocationClient;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class AssistantApp extends Application {

	public Executor mExecutor = Executors.newFixedThreadPool(2);
	
	private static AssistantApp instance = null;
	private LocationClient mLocationClient;
	
	public void onCreate() {
		instance = this;
		mLocationClient = new LocationClient(this);
		
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
			.threadPoolSize(2)
			.threadPriority(Thread.NORM_PRIORITY - 2)
			.memoryCacheSize(500000) // 缓存500KB
			.denyCacheImageMultipleSizesInMemory()
			.discCacheFileNameGenerator(new Md5FileNameGenerator())
			.enableLogging()
			.build();
		ImageLoader.getInstance().init(configuration);
	}
	
	public static AssistantApp getInstance() {
		return instance;
	}
	
	public LocationClient getLocationClient() {
		return mLocationClient;
	}

}
