package com.coofee.assistant.util.translate;

import java.lang.ref.WeakReference;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import com.coofee.assistant.AssistantApp;

/**
 * 异步翻译
 * @author zhao
 *
 */
public class TranslateApi {
	private ProgressDialog mProgressDialog;
	private TranslateHandler mTranslateHandler;

	public void translateZhToEn(Context context, final String input,
			TranslateListener listener) {
		init(context, listener);
		
		mTranslateHandler = new TranslateHandler(TranslateApi.this);
		mTranslateHandler.addTranslateListener(listener);
		AssistantApp.getInstance().mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String result = TranslateUtil.translateZhToEn(input);
					mTranslateHandler.obtainMessage(MSG_TRANSLATE_COMPLETED,
							result).sendToTarget();
				} catch (Exception e) {
					mTranslateHandler.obtainMessage(MSG_TRANSLATE_UNCOMPLETED)
							.sendToTarget();
				}
			}
		});
	}

	public void translateEnToZh(Context context, final String input,
			TranslateListener listener) {
		init(context, listener);
		
		AssistantApp.getInstance().mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String result = TranslateUtil.translateEnToZh(input);
					mTranslateHandler.obtainMessage(MSG_TRANSLATE_COMPLETED,
							result).sendToTarget();
				} catch (Exception e) {
					mTranslateHandler.obtainMessage(MSG_TRANSLATE_UNCOMPLETED)
							.sendToTarget();
				}
			}
		});
	}

	private void init(Context context, TranslateListener listener) {
		mTranslateHandler = new TranslateHandler(TranslateApi.this);
		mTranslateHandler.addTranslateListener(listener);
		
		mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setTitle("翻译中...");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(true);
		mProgressDialog
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						mTranslateHandler.obtainMessage(MSG_TRANSLATE_CANCELED)
								.sendToTarget();
					}
				});
		mProgressDialog.show();
	}

	private static final int MSG_TRANSLATE_COMPLETED = 0x0001;
	private static final int MSG_TRANSLATE_UNCOMPLETED = 0x0002;
	private static final int MSG_TRANSLATE_CANCELED = 0x0003;

	private static class TranslateHandler extends Handler {
		private WeakReference<TranslateApi> translateRef;
		private TranslateListener translateListener;

		public TranslateHandler(TranslateApi translate) {
			translateRef = new WeakReference<TranslateApi>(translate);
		}

		public void addTranslateListener(TranslateListener listener) {
			translateListener = listener;
		}

		@Override
		public void handleMessage(Message msg) {
			if (translateRef.get() != null
					&& translateRef.get().mProgressDialog != null) {
				translateRef.get().mProgressDialog.dismiss();
			}

			switch (msg.what) {
			case MSG_TRANSLATE_COMPLETED:
				TranslateUtil.executeTranslateResponse((String) msg.obj,
						translateListener);
				break;
			case MSG_TRANSLATE_UNCOMPLETED:
				translateListener.onError("翻译失败!");
				break;
			case MSG_TRANSLATE_CANCELED:
				break;
			}
		}
	}
}
