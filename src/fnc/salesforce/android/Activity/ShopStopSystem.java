package fnc.salesforce.android.Activity;

import fnc.salesforce.android.LoginPage;
import fnc.salesforce.android.Main_Page;
import fnc.salesforce.android.R;
import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.LIB.BrandActivity;
import fnc.salesforce.android.LIB.BrowserSetting;
import fnc.salesforce.android.LIB.CipherUtils;
import fnc.salesforce.android.LIB.KumaLog;
import fnc.salesforce.android.LIB.MenuActivity;
import fnc.salesforce.android.LIB.ProductDialog;
import fnc.salesforce.android.Product_Search.Product_Search_Main;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.HttpAuthHandler;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
//import android.webkit.WebResourceResponse;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ShopStopSystem extends Activity implements OnClickListener{
		
	@Override
	public void onBackPressed() {
		try {
			if( webSecond.getChildAt(0) == childView){
				if( childView.canGoBack() ){
					if( SecondPage.getVisibility() == View.VISIBLE ){
						if(SecondPage != null && SecondPage.isShown() ) {
							SecondPage.removeAllViews();
							SecondPage.setVisibility(View.GONE);
							mCustomViewCallback.onCustomViewHidden();
							childView.setVisibility(View.VISIBLE);
							childView.goBack();
							childView.clearHistory();
				    	}
					} else {
						childView.goBack();
					}
				} else {
					try {
						childView.loadUrl("about:blank");
//						web.onResume();
						web.resumeTimers();
						webSecond.removeView(childView);
						webSecond.addView(web);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				if( web.canGoBack() ){
					if( SecondPage.getVisibility() == View.VISIBLE ){
						if(SecondPage != null && SecondPage.isShown() ) {
							SecondPage.removeAllViews();
							SecondPage.setVisibility(View.GONE);
							mCustomViewCallback.onCustomViewHidden();
							web.setVisibility(View.VISIBLE);
							web.goBack();
							web.clearHistory();
				    	}
					} else {
						web.goBack();
					}
				} else {
					try {
						web.loadUrl("about:blank");
						Intent intent = new Intent( mContext, Main_Page.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						finish();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();

	}
		
	private Context mContext;
	private WebView web = null;
	private ProgressBar mProgressBar;
	
	static String pathURL = "", displayCode = "";
	private FrameLayout SecondPage;

	private RelativeLayout webSecond;
	
	private ImageView  btn_Brand_LogOut, btnMenu_Activity;
	
	private ImageButton btn_Brand_Activity;
	
	private MenuActivity mMenu;
	
	private BrandActivity mBrand;
	
	private AlertDialog.Builder dialogBuilder;
	
	private TextView txtCategoryName, txt_AdGallery_Logo;
	
	String strCategoryName = "", strBackState = "";
	
	private ImageView btnGo_Main, btnBack_Page;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView( R.layout.show_stop_webview );
		
		web					=  new WebView( this );
		
		mContext = this;
		
		mProductDetailDialog = new ProductDialog( this, R.style.Transparent);
		
		mProgressBar = (ProgressBar) findViewById(R.id.progress);
		SecondPage = (FrameLayout) findViewById(R.id.SecondPage);
		webSecond = (RelativeLayout) findViewById(R.id.webSecond);
				
		dialogBuilder = new AlertDialog.Builder(this);
		
		mMenu = new MenuActivity( this, R.style.Transparent);
		
		mBrand	= new BrandActivity( this, R.style.Transparent);
		
		btn_Brand_Activity	= ( ImageButton ) findViewById( R.id.btn_Brand_Activity );
		
		btn_Brand_LogOut    = (ImageView) findViewById( R.id.btn_Brand_LogOut );
		txt_AdGallery_Logo  = (TextView) findViewById( R.id.txt_AdGallery_Logo );
		btnMenu_Activity    = (ImageView) findViewById( R.id.btnMenu_Activity );
		
		btnGo_Main    		= (ImageView) findViewById( R.id.btnGo_Main );
		btnBack_Page    	= (ImageView) findViewById( R.id.btnBack_Page );
		
		txtCategoryName    = (TextView) findViewById( R.id.txtCategoryName );
		
		strCategoryName = "매장매출조회";
        txtCategoryName.setText( strCategoryName );
        
        try {
        	txt_AdGallery_Logo.setText( Constance.SHOPNAME );
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        btnMenu_Activity.setOnClickListener( this );
    	
    	btn_Brand_Activity.setOnClickListener( this );
    	btn_Brand_LogOut.setOnClickListener( this );
    	
    	btnGo_Main.setOnClickListener( this );
		btnBack_Page.setOnClickListener( this );
		
		web.setLayoutParams(new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
		web.setBackgroundColor(Color.parseColor("#00000000") );
		webSecond.addView( web );
		SecondPage.setBackgroundColor( Color.parseColor("#00000000") );

		BrowserSetting browserSetting = new BrowserSetting();
		browserSetting.Setting( web );

		web.setWebViewClient(new ListWebViewClient());

		web.setWebChromeClient( new testWebviewShrome() );

		web.requestFocus();
		web.setFocusable(true);
		web.setFocusableInTouchMode(true);

		web.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {


				switch (event.getAction()) {

				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_UP:
					if (!v.hasFocus()) {
						v.requestFocus();
					}
					break;
				}
				return false;
			}
		});
		String mURL = "http://msalesforce.kolon.com:8282/fncstop/kolonSignalLight.do?";
		if( Constance.HEAD_LOGIN  ){
			mURL = mURL + "staffG=1";
		} else {
			mURL = mURL + "staffG=2";
		}
		try {
			
			long timestamp = System.currentTimeMillis();
			
			mURL = mURL + "&usrNm=" + java.net.URLEncoder.encode(new String(Constance.userNm.getBytes("UTF-8")));
			mURL = mURL + "&deptC=" + Constance.BEANDCD;
			mURL = mURL + "&keyPram=" + mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
			
			
			if( Constance.HEAD_LOGIN  ){
				mURL = mURL + "&empNo=" + Constance.empNo;
				mURL = mURL + "&shopC=" + "";
			} else {
				mURL = mURL + "&empNo=" + Constance.SHOPCD;
				mURL = mURL + "&shopC=" + Constance.SHOPCD;
			}
			
			mURL = mURL + "&hiddenValue=1";
		} catch (Exception e) {
			e.printStackTrace();
		}


//		 
//		본사 || 매장    : staffG
//		EMP_NO         : empNo
//		USER_NM       : usrNm
//		부서코드         : deptC
//		매장코드         : shopC
//		기준년월         : yyyyMm
//		키값                : keyPram

		
		web.loadUrl( mURL );
		
	}
	
	private CipherUtils mCipher = new CipherUtils();

	WebView childView;
	private boolean SecondloadState = false;
	public class testWebviewShrome extends WebChromeClient {
		
		public testWebviewShrome() {
		}
		
		 private Bitmap mDefaultVideoPoster;
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				super.onProgressChanged(view, newProgress);
				if( mProgressBar != null ){
					mProgressBar.setProgress(newProgress);
				}
			}
			
			@Override
			public View getVideoLoadingProgressView() {
				// TODO Auto-generated method stub
				return super.getVideoLoadingProgressView();
			}

			@Override
			public void getVisitedHistory(ValueCallback<String[]> callback) {
				// TODO Auto-generated method stub
				super.getVisitedHistory(callback);
			}

			@Override
			public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
				// TODO Auto-generated method stub
				return super.onConsoleMessage(consoleMessage);
			}

			@Override
			public void onConsoleMessage(String message, int lineNumber,
					String sourceID) {
				// TODO Auto-generated method stub
				super.onConsoleMessage(message, lineNumber, sourceID);
			}

			@Override
			public void onExceededDatabaseQuota(String url,
					String databaseIdentifier, long currentQuota,
					long estimatedSize, long totalUsedQuota,
					QuotaUpdater quotaUpdater) {
				// TODO Auto-generated method stub
				super.onExceededDatabaseQuota(url, databaseIdentifier, currentQuota,
						estimatedSize, totalUsedQuota, quotaUpdater);
			}

			@Override
			public void onGeolocationPermissionsHidePrompt() {
				// TODO Auto-generated method stub
				super.onGeolocationPermissionsHidePrompt();
			}

			@Override
			public void onGeolocationPermissionsShowPrompt(String origin,
					Callback callback) {
				// TODO Auto-generated method stub
				super.onGeolocationPermissionsShowPrompt(origin, callback);
			}

			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				// TODO Auto-generated method stub
				return super.onJsAlert(view, url, message, result);
			}

			@Override
			public boolean onJsBeforeUnload(WebView view, String url,
					String message, JsResult result) {
				// TODO Auto-generated method stub
				return super.onJsBeforeUnload(view, url, message, result);
			}

			@Override
			public boolean onJsPrompt(WebView view, String url, String message,
					String defaultValue, JsPromptResult result) {
				// TODO Auto-generated method stub
				return super.onJsPrompt(view, url, message, defaultValue, result);
			}

			@Override
			public boolean onJsTimeout() {
				// TODO Auto-generated method stub
				return super.onJsTimeout();
			}

			@Override
			public void onReachedMaxAppCacheSize(long spaceNeeded,
					long totalUsedQuota, QuotaUpdater quotaUpdater) {
				// TODO Auto-generated method stub
				super.onReachedMaxAppCacheSize(spaceNeeded, totalUsedQuota, quotaUpdater);
			}

			@Override
			public void onReceivedIcon(WebView view, Bitmap icon) {
				// TODO Auto-generated method stub
				super.onReceivedIcon(view, icon);
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				// TODO Auto-generated method stub
				super.onReceivedTitle(view, title);
			}

			@Override
			public void onReceivedTouchIconUrl(WebView view, String url,
					boolean precomposed) {
				// TODO Auto-generated method stub
				super.onReceivedTouchIconUrl(view, url, precomposed);
			}

			@Override
			public void onRequestFocus(WebView view) {
				// TODO Auto-generated method stub
				super.onRequestFocus(view);
			}

//			@Override
//			public void onShowCustomView(View view, int requestedOrientation,
//					CustomViewCallback callback) {
//				// TODO Auto-generated method stub
//				super.onShowCustomView(view, requestedOrientation, callback);
//			}

			@Override
			public boolean onJsConfirm(WebView view, String url,
					String message, final JsResult result) {
				new AlertDialog.Builder( mContext ).setMessage(
						message).setPositiveButton(android.R.string.ok,
						new AlertDialog.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								
								result.confirm();
							}
						}).setNegativeButton(android.R.string.cancel,
						new AlertDialog.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								result.cancel();
							}
						}).setCancelable(false).create().show();

				return true;
			}
			// HJ
			@Override
			public void onShowCustomView(View view, CustomViewCallback callback) {
				if( SecondPage.isShown() ) {
					callback.onCustomViewHidden();
					return;
				}
				mCustomViewCallback = callback;
				SecondPage.addView(view);
				SecondPage.setVisibility(View.VISIBLE);
				if ( webSecond.getChildAt(0) == childView ){
					childView.setVisibility(View.GONE);
				} else {
					web.setVisibility(View.GONE);
				}
			}

			@Override
			public void onHideCustomView() {
				if( !SecondPage.isShown() )
					return;
				
				SecondPage.removeAllViews();
				SecondPage.setVisibility(View.GONE);
				mCustomViewCallback.onCustomViewHidden();
				if ( webSecond.getChildAt(0) == childView ){
					childView.setVisibility(View.VISIBLE);
					childView.goBack();
					childView.clearHistory();
				} else {
					web.setVisibility(View.VISIBLE);
					web.goBack();
					web.clearHistory();
				}
				
			}

			@Override
			public Bitmap getDefaultVideoPoster() {
				// TODO Auto-generated method stub
				if( mDefaultVideoPoster == null ){
					int resPosterId = getResources().getIdentifier("video", "drawable", mContext.getPackageName());
					mDefaultVideoPoster = BitmapFactory.decodeResource(getResources(), resPosterId);
				}
				return mDefaultVideoPoster;
			}
			
			
			@Override
			public void onCloseWindow(WebView window) {
				// TODO Auto-generated method stub
//				super.onCloseWindow(window);
//				window.loadUrl("about:blank");
//				webSecond.removeView( childView );

				if( childView != null ){
					childView.loadUrl("about:blank");
//					web.onResume();
					web.resumeTimers();
					webSecond.removeView(childView);
					webSecond.addView(web);
					web.requestFocus();
				}
			}
			
			@Override
         public boolean onCreateWindow(WebView view, boolean dialog,
                 boolean userGesture, Message resultMsg) {
//				web.onPause();
				web.pauseTimers();
				webSecond.removeAllViews();
				childView = null;
				childView = new WebView( mContext );

				BrowserSetting browserSetting = new BrowserSetting();
				browserSetting.Setting( childView );
				childView.setBackgroundColor(0);
				childView.requestFocus();
				childView.setWebChromeClient(new testWebviewShrome());
				
				childView.setWebViewClient(new WebViewClient(){
					
					
					@Override
					public void doUpdateVisitedHistory(WebView view,
							String url, boolean isReload) {
						// TODO Auto-generated method stub
						super.doUpdateVisitedHistory(view, url, isReload);
					}

					@Override
					public void onFormResubmission(WebView view,
							Message dontResend, Message resend) {
						// TODO Auto-generated method stub
						super.onFormResubmission(view, dontResend, resend);
					}

					@Override
					public void onLoadResource(WebView view, String url) {
						// TODO Auto-generated method stub
						
						super.onLoadResource(view, url);
					}

					@Override
					public void onPageFinished(WebView view, String url) {
						// TODO Auto-generated method stub
						super.onPageFinished(view, url);
//						String straaa = web.getUrl();
						SecondloadState = false;			
						if( mProgressBar != null ){
							mProgressBar.setVisibility(View.GONE);
						}
						
						try {
							CookieSyncManager.getInstance().sync();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					

					@Override
					public void onPageStarted(WebView view, String url,
							Bitmap favicon) {
						// TODO Auto-generated method stub
						SecondloadState = true;
						super.onPageStarted(view, url, favicon);
						try {
							CookieSyncManager.getInstance().sync();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onReceivedError(WebView view, int errorCode,
							String description, String failingUrl) {
						// TODO Auto-generated method stub
						super.onReceivedError(view, errorCode, description, failingUrl);
					}

					@Override
					public void onReceivedHttpAuthRequest(WebView view,
							HttpAuthHandler handler, String host, String realm) {
						// TODO Auto-generated method stub
						super.onReceivedHttpAuthRequest(view, handler, host, realm);
					}

//					@Override
//					public void onReceivedLoginRequest(WebView view,
//							String realm, String account, String args) {
//						// TODO Auto-generated method stub
//						super.onReceivedLoginRequest(view, realm, account, args);
//					}

					@Override
					public void onReceivedSslError(WebView view,
							SslErrorHandler handler, SslError error) {
						// TODO Auto-generated method stub
						super.onReceivedSslError(view, handler, error);
					}

					@Override
					public void onScaleChanged(WebView view, float oldScale,
							float newScale) {
						// TODO Auto-generated method stub
						super.onScaleChanged(view, oldScale, newScale);
					}

					@Override
					public void onTooManyRedirects(WebView view,
							Message cancelMsg, Message continueMsg) {
						// TODO Auto-generated method stub
						super.onTooManyRedirects(view, cancelMsg, continueMsg);
					}

					@Override
					public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
						// TODO Auto-generated method stub
						super.onUnhandledKeyEvent(view, event);
					}

//					@Override
//					public WebResourceResponse shouldInterceptRequest(
//							WebView view, String url) {
//						// TODO Auto-generated method stub
//						return super.shouldInterceptRequest(view, url);
//					}

					@Override
					public boolean shouldOverrideKeyEvent(WebView view,
							KeyEvent event) {
						// TODO Auto-generated method stub
						return super.shouldOverrideKeyEvent(view, event);
					}
					@Override
					public boolean shouldOverrideUrlLoading(WebView view,
							String url) {
						// TODO Auto-generated method stub			
						return super.shouldOverrideUrlLoading(view, url);
					}
				});
				
				childView.setLayoutParams(new LinearLayout.LayoutParams( 795, 645));				
								
				WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;  
				transport.setWebView( childView );      
				resultMsg.sendToTarget(); 

				webSecond.addView( childView );
		         return false;
			}
	}
	
	private WebChromeClient.CustomViewCallback mCustomViewCallback;
	private int backgroundColor = Color.BLACK;
	private String loadURL = "";
	
	private ProductDialog mProductDetailDialog;
	
	public class ListWebViewClient extends WebViewClient {

		public ListWebViewClient() {

		} 

		@Override
		public void onReceivedHttpAuthRequest(WebView view,
				HttpAuthHandler handler, String host, String realm) {
			// TODO Auto-generated method stub
			super.onReceivedHttpAuthRequest(view, handler, host, realm);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			KumaLog.LogD("test_1"," url : " + url );
			if( url.contains( "fnc:pcode:") ){
				String[] mSplit = url.toString().trim().split("fnc:pcode:");
				mProductDetailDialog.SetProduct_CD( mSplit[1] );
				mProductDetailDialog.show();
			}
			
//			11-18 14:36:54.910: D/test_1(19712):  url : fnc:pcode:KYPX31821
//			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);

			try {
				CookieSyncManager.getInstance().sync();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if( mProgressBar != null ){
					mProgressBar.setVisibility(View.VISIBLE);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// TODO Auto-generated method stub
			super.onReceivedError(view, errorCode, description, failingUrl);
			// �ε� ����
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			if( mProgressBar != null ){
				mProgressBar.setVisibility(View.INVISIBLE);
			}
						
			
			try {
				CookieSyncManager.getInstance().sync();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
				
		try {
			web.loadUrl("about:blank");
			if( childView != null ){
				childView.loadUrl("about:blank");
				webSecond.removeView(childView);
				childView.destroy();
			}
			web.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		try {
			if (SecondPage != null && SecondPage.isShown()) {
				SecondPage.removeAllViews();
				SecondPage.setVisibility(View.GONE);
				mCustomViewCallback.onCustomViewHidden();
				web.setVisibility(View.VISIBLE);
				web.clearHistory();
				web.loadUrl(pathURL);
			} else {
//				web.dispatchWindowVisibilityChanged(View.GONE);
//				web.onPause();
				web.pauseTimers();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	


	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if( Constance.TimeOverState ){
			Intent intent = new Intent( mContext, LoginPage.class );
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	}


	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}


	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		try {
			if (SecondPage != null && SecondPage.isShown()) {
				SecondPage.removeAllViews();
				SecondPage.setVisibility(View.GONE);
				mCustomViewCallback.onCustomViewHidden();
				web.setVisibility(View.VISIBLE);
				web.clearHistory();
				web.loadUrl(pathURL);
			} else {
	
			}
//			ATT.DestroyTimer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onClick(View v) {

		if( v == btnMenu_Activity ){
			mMenu.show();
		} else if( v == btn_Brand_Activity ) {
			mBrand.show();
		} else if( v == btn_Brand_LogOut ) {

		} else if( v == btnGo_Main ) {
			Intent intent = new Intent( mContext, Main_Page.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		} else if( v == btnBack_Page ) {
			try {
				Intent intent = new Intent( mContext, Main_Page.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}